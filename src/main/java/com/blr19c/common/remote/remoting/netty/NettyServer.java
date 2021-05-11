package com.blr19c.common.remote.remoting.netty;

import com.blr19c.common.remote.config.RemoteConfigProperties;
import com.blr19c.common.remote.remoting.RemotingServer;
import com.blr19c.common.remote.serialization.Serialization;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * netty 远程服务
 */
public class NettyServer implements RemotingServer {
    private final EventLoopGroup recipientEventLoopGroup = new NioEventLoopGroup();
    private final EventLoopGroup clientEventLoopGroup = new NioEventLoopGroup();
    private final ServerBootstrap bootstrap = new ServerBootstrap();
    private final NettyRemotingHandler nettyRemotingHandler;
    private final RemoteConfigProperties properties;

    public NettyServer(Serialization serialization, RemoteConfigProperties properties) {
        this.nettyRemotingHandler = new NettyRemotingHandler(serialization);
        this.properties = properties;
    }

    @Override
    public RemotingServer start() throws Exception {
        //暂未使用epoll
        bootstrap.group(recipientEventLoopGroup, clientEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        // 将HTTP消息的多个部分合成一条完整的HTTP消息(虽然合并了也可能依旧不完整)
                        ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(1024 * 1024));
                        // 请求/响应转码器
                        ch.pipeline().addLast("http-encoder", new HttpResponseEncoder())
                                .addLast("http-decoder", new HttpRequestDecoder());
                        // 多数据流
                        ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                        ch.pipeline().addLast("http-server", new NettyHttpServerHandler(nettyRemotingHandler));
                    }
                });
        bootstrap.bind(properties.getPort()).sync();
        return this;
    }
}
