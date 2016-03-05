package org.yetiz.utils.cmds.service;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * cmds
 * Created by yeti on 16/3/6.
 */
public class ServerChannelWorkerConfig {
    EventLoopGroup listenGroup;
    EventLoopGroup childGroup;
    Class serverSocketClass;

    public ServerChannelWorkerConfig() {
        this(0, 0);
    }

    public ServerChannelWorkerConfig(int listenWorkerCount, int childWorkerCount) {
        if (listenWorkerCount == 0) {
            listenWorkerCount = Runtime.getRuntime().availableProcessors();
        }

        if (childWorkerCount == 0) {
            childWorkerCount = Runtime.getRuntime().availableProcessors() * 4;
        }

        try {
            createEpollGroup(listenWorkerCount, childWorkerCount);
        } catch (Throwable throwable) {
            createNioGroup(listenWorkerCount, childWorkerCount);
        }
    }

    private void createEpollGroup(int listenWorkerCount, int childWorkerCount) {
        listenGroup = new EpollEventLoopGroup(listenWorkerCount);
        childGroup = new EpollEventLoopGroup(childWorkerCount);
        serverSocketClass = EpollServerSocketChannel.class;
    }

    private void createNioGroup(int listenWorkerCount, int childWorkerCount) {
        listenGroup = new NioEventLoopGroup(listenWorkerCount);
        childGroup = new NioEventLoopGroup(childWorkerCount);
        serverSocketClass = NioServerSocketChannel.class;
    }

    public EventLoopGroup listenGroup() {
        return listenGroup;
    }

    public EventLoopGroup childGroup() {
        return childGroup;
    }

    public Class serverSocketClass() {
        return serverSocketClass;
    }

    public void shutdownGracefully() {
        listenGroup.shutdownGracefully()
            .addListener((GenericFutureListener) future -> childGroup.shutdownGracefully());
    }
}
