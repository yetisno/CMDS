package org.yetiz.utils.cmds.messages;

import java.util.concurrent.atomic.AtomicLong;

/**
 * cmds
 * Created by yeti on 16/3/5.
 */
public class DefaultMessage implements Message {

    public static AtomicLong Counter = new AtomicLong(Long.MAX_VALUE - 1);
    protected Format format = Format.Undefined;
    protected long id;

    public DefaultMessage() {
        id = Counter.getAndUpdate(operand -> operand < Long.MAX_VALUE ? operand + 1 : 0);
    }

    public static DefaultMessage from(Format format, byte[] data) {
        switch (format) {
            case Undefined:
                break;
            case Generic:
                return GenericMessage.from(data);
            case Broadcast:
                return BroadcastMessage.from(data);
            case Forward:
                return ForwardMessage.from(data);
        }
        return new DefaultMessage();
    }

    public long id() {
        return id;
    }

    public enum Format {
        Undefined, Generic, Broadcast, Forward
    }
}
