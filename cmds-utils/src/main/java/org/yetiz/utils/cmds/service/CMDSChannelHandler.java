package org.yetiz.utils.cmds.service;

import io.netty.channel.ChannelDuplexHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yetiz.utils.cmds.utils.Lazy;

/**
 * cmds
 * Created by yeti on 16/3/6.
 */
public abstract class CMDSChannelHandler extends ChannelDuplexHandler {
    private Lazy<Logger> lazyLogger = Lazy.method(() -> LoggerFactory.getLogger(this.getClass()));

    public Logger logger() {
        return this.lazyLogger.get();
    }
}
