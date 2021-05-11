package com.blr19c.common.remote.remoting.netty;

import com.blr19c.common.remote.remoting.AbstractRemotingHandler;
import com.blr19c.common.remote.serialization.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 转换Result
 *
 * @author blr
 * @since 2021.5.11
 */
public class NettyRemotingHandler extends AbstractRemotingHandler {

    protected NettyRemotingHandler(Serialization serialization) {
        super(serialization);
    }

    ByteBuf conversionInvocation(ByteBuf byteBuf) throws IOException {
        ByteArrayOutputStream fn = new ByteArrayOutputStream();
        serialization.serialize(fn, super.invocation(new ByteBufInputStream(byteBuf)));
        return Unpooled.wrappedBuffer(fn.toByteArray());
    }
}