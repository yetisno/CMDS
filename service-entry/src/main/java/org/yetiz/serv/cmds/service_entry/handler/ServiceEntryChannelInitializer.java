package org.yetiz.serv.cmds.service_entry.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * cmds
 * Created by yeti on 16/3/6.
 */
public class ServiceEntryChannelInitializer extends ChannelInitializer<SocketChannel> {
    private LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
            .addLast(loggingHandler)
            .addLast(new HttpServerCodec())
            .addLast(new HttpObjectAggregator(5120))
            .addLast(new RequestHandler());
    }
}
