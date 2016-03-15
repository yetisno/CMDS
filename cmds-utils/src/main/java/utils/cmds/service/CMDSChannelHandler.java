package utils.cmds.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelDuplexHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.cmds.utils.Lazy;

/**
 * cmds
 * Created by yeti on 16/3/6.
 */
public abstract class CMDSChannelHandler extends ChannelDuplexHandler {
    protected static ObjectMapper JsonMapper = new ObjectMapper(new JsonFactory());
    private Lazy<Logger> lazyLogger = Lazy.method(() -> LoggerFactory.getLogger(this.getClass()));

    public Logger logger() {
        return this.lazyLogger.get();
    }
}
