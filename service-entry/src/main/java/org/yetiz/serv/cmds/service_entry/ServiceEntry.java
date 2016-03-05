package org.yetiz.serv.cmds.service_entry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.LoggerFactory;
import org.yetiz.serv.cmds.service_entry.handler.ServiceEntryChannelInitializer;
import org.yetiz.utils.cmds.exception.InvalidValueException;
import org.yetiz.utils.cmds.service.ServerChannelWorkerConfig;
import org.yetiz.utils.cmds.service.Service;
import org.yetiz.utils.cmds.utils.Run;

import java.util.Map;
import java.util.Optional;

/**
 * cmds
 * Created by yeti on 16/2/29.
 */
public class ServiceEntry implements Service {
    private Channel channel;
    private ServerChannelWorkerConfig serverChannelWorkerConfig;

    /**
     * first param is <code>port</code>
     *
     * @param params port
     * @return <code>true</code> when start success or <code>false</code> on error.
     */
    @Override
    public boolean start(Object... params) {
        if (params.length < 1) {
            throw new InvalidValueException("port not defined");
        }

        serverChannelWorkerConfig = new ServerChannelWorkerConfig();
        ServerBootstrap bootstrap = new ServerBootstrap();
        int port = (int) params[0];
        try {
            channel = bootstrap.group(serverChannelWorkerConfig.listenGroup(),
                serverChannelWorkerConfig.childGroup())
                .channel(serverChannelWorkerConfig.serverSocketClass())
                .option(ChannelOption.SO_BACKLOG, 20480)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_RCVBUF, 40960)
                .option(ChannelOption.SO_SNDBUF, 40960)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ServiceEntryChannelInitializer())
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .bind(port)
                .sync()
                .channel();
        } catch (Throwable throwable) {
            LoggerFactory.getLogger(getClass().getName()).error(throwable.getMessage());
            return false;
        } finally {
            stop();
        }
        return true;
    }

    @Override
    public boolean stop(Object... params) {
        try {
            Optional.ofNullable(channel).ifPresent(channel ->
                Run.withoutException(() -> channel.close()));
            Optional.ofNullable(serverChannelWorkerConfig).ifPresent(config ->
                Run.withoutException(() -> config.shutdownGracefully()));
        } catch (Throwable throwable) {
        } finally {
            return true;
        }
    }

    @Override
    public boolean reload(Object... params) {
        return false;
    }

    @Override
    public boolean restart(Object... params) {
        return false;
    }

    @Override
    public Map<String, Object> status(Object... params) {
        return null;
    }
}
