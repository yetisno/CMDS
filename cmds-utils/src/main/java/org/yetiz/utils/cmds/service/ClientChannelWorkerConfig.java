package org.yetiz.utils.cmds.service;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * cmds
 * Created by yeti on 16/3/6.
 */
public class ClientChannelWorkerConfig {
    EventLoopGroup clientGroup;
    Class clientSocketClass;

    public ClientChannelWorkerConfig() {
        this(0);
    }

    public ClientChannelWorkerConfig(int clientWorkerCount) {
        if (clientWorkerCount == 0) {
            clientWorkerCount = Runtime.getRuntime().availableProcessors() * 4;
        }

        try {
            createEpollGroup(clientWorkerCount);
        } catch (Throwable throwable) {
            createNioGroup(clientWorkerCount);
        }
    }

    private void createEpollGroup(int clientWorkerCount) {
        clientGroup = new EpollEventLoopGroup(clientWorkerCount);
        clientSocketClass = EpollSocketChannel.class;
    }

    private void createNioGroup(int clientWorkerCount) {
        clientGroup = new NioEventLoopGroup(clientWorkerCount);
        clientSocketClass = NioSocketChannel.class;
    }

    public EventLoopGroup clientGroup() {
        return clientGroup;
    }

    public Class clientSocketClass() {
        return clientSocketClass;
    }

    public void shutdownGracefully() {
        clientGroup.shutdownGracefully();
    }
}
