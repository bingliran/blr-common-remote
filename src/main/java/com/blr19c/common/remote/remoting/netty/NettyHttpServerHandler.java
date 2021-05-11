package com.blr19c.common.remote.remoting.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.io.IOException;

import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

/**
 * 只接收数据的handler
 *
 * @author blr
 * @since 2021.5.10
 */
public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * unused {@link Sharable}
     */
    private final CompositeByteBuf source = ByteBufAllocator.DEFAULT.compositeBuffer();
    private final HttpHeaders httpHeaders = new DefaultHttpHeaders();
    private final NettyRemotingHandler nettyRemotingHandler;

    public NettyHttpServerHandler(NettyRemotingHandler nettyRemotingHandler) {
        this.nettyRemotingHandler = nettyRemotingHandler;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        if (msg instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) msg;
            if (HttpUtil.is100ContinueExpected(httpRequest)) {
                ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
                return;
            }
            //存活
            if (HttpUtil.isKeepAlive(httpRequest))
                httpHeaders.set(CONNECTION, KEEP_ALIVE);
            return;
        }
        if (msg instanceof HttpContent) {
            if (!(appendContent(msg) instanceof LastHttpContent))
                return;
            FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    nettyRemotingHandler.conversionInvocation(source)
            );
            httpHeaders.set(CONTENT_LENGTH, fullHttpResponse.content().readableBytes());
            httpHeaders.set(CONTENT_TYPE, APPLICATION_OCTET_STREAM_VALUE);
            fullHttpResponse.headers().setAll(httpHeaders);
            ctx.write(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 多个HttpContent
     */
    private <T> T appendContent(T msg) {
        if (msg instanceof HttpContent) {
            ByteBuf content = ((HttpContent) msg).content();
            source.writeBytes(content);
        }
        return msg;
    }
}
