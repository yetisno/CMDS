package org.yetiz.utils.cmds.messages;

import java.util.concurrent.atomic.AtomicLong;

/**
 * cmds
 * Created by yeti on 16/3/5.
 */
public class DefaultMessage implements Message {

    public static AtomicLong Counter = new AtomicLong(Long.MAX_VALUE - 1);

    protected long id;

    public DefaultMessage() {
        id = Counter.getAndUpdate(operand -> operand < Long.MAX_VALUE ? operand + 1 : 0);
    }

    public long id() {
        return id;
    }
}
