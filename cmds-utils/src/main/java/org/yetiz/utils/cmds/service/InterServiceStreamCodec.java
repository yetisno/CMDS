package org.yetiz.utils.cmds.service;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yetiz.utils.cmds.exception.DecodeFailException;
import org.yetiz.utils.cmds.utils.Lazy;

import java.net.SocketAddress;
import java.util.List;

/**
 * cmds
 * Created by yeti on 16/3/6.
 */
@ChannelHandler.Sharable
public class InterServiceStreamCodec extends ReplayingDecoder<InterServiceStreamCodec.State> implements
    ChannelOutboundHandler {
    private final static String KEY_TYPE_NAME = "D-TyPe";
    private final static String KEY_LENGTH_NAME = "D-LeNgTh";
    private final static int TYPE_SIZE = Byte.BYTES;
    private final static int LENGTH_SIZE = Short.BYTES;
    private final static int HEADER_SIZE = TYPE_SIZE + LENGTH_SIZE;
    private final static AttributeKey<Type> KEY_TYPE = AttributeKey.<Type>newInstance(KEY_TYPE_NAME);
    private final static AttributeKey<Integer> KEY_LENGTH = AttributeKey.<Integer>newInstance(KEY_LENGTH_NAME);
    private static Lazy<Logger> lazyLogger = Lazy.method(() -> LoggerFactory.getLogger(InterServiceStreamCodec.class));

    private static ByteBuf encode(ChannelHandlerContext ctx, ByteBuf in) {
        return ctx.channel()
            .alloc()
            .directBuffer(HEADER_SIZE + in.capacity())
            .writeByte(0x00)
            .writeShort(in.capacity())
            .writeBytes(in);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
            case Type:
                switch (in.readByte()) {
                    case 0x00:
                        ctx.attr(KEY_TYPE).set(Type.Default);
                        checkpoint(State.Length);
                        break;
                    default:
                        lazyLogger.get().error("Error Decode Type, Close Channel.");
                        ctx.channel().close();
                        throw new DecodeFailException("Jump into Error Type");
                }
            case Length:
                if (ctx.attr(KEY_TYPE).getAndRemove().equals(Type.Default)) {
                    ctx.attr(KEY_LENGTH).set(in.readUnsignedShort());
                }

                checkpoint(State.Content);
            case Content:
                out.add(in.readBytes(ctx.attr(KEY_LENGTH).getAndRemove()));
                checkpoint(State.Type);
                break;
        }

        decode(ctx, in, out);
    }

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress,
                        ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.disconnect(promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.close(promise);
    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        ctx.read();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(encode(ctx, (ByteBuf) msg), promise);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    enum State {
        Type, Length, Content
    }

    enum Type {
        Default
    }
}
