package org.yetiz.utils.cmds.messages;

import com.google.protobuf.ByteString;
import org.yetiz.utils.cmds.exception.InvalidValueException;
import org.yetiz.utils.cmds.messages.proto.Proto;

/**
 * cmds
 * Created by yeti on 16/3/5.
 */
public class BroadcastMessage extends DefaultMessage {
    private String name;

    private byte[] data;

    protected BroadcastMessage() {
        this.format = Format.Broadcast;
    }

    public BroadcastMessage(String name, byte[] data) {
        this();
        this.name = name;
        this.data = data;
    }

    public static BroadcastMessage from(byte[] data) {
        try {
            Proto.Message.Broadcast message = Proto.Message.Broadcast.parseFrom(data);
            return new Builder()
                .id(message.getId())
                .name(message.getName())
                .data(message.getMessage().toByteArray())
                .build();
        } catch (Throwable throwable) {
            throw new InvalidValueException("data");
        }
    }

    public byte[] toByteArray() {
        return
            Proto.Message.Broadcast.newBuilder()
                .setId(this.id())
                .setName(this.name())
                .setMessage(ByteString.copyFrom(this.data()))
                .build()
                .toByteArray();
    }

    public String name() {
        return name;
    }

    public byte[] data() {
        return data;
    }

    public static class Builder {
        private BroadcastMessage message = new BroadcastMessage();

        public Builder id(long id) {
            this.message.id = id;
            return this;
        }

        public Builder name(String name) {
            this.message.name = name;
            return this;
        }

        public Builder data(byte[] data) {
            this.message.data = data;
            return this;
        }

        public BroadcastMessage build() {
            return this.message;
        }
    }
}
