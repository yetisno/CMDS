package org.yetiz.utils.cmds.messages;

import com.google.protobuf.ByteString;
import org.yetiz.utils.cmds.exception.InvalidValueException;
import org.yetiz.utils.cmds.messages.proto.Proto;

/**
 * cmds
 * Created by yeti on 16/3/5.
 */
public class ForwardMessage extends DefaultMessage {
    private String name;

    private byte[] rc;

    private byte[] lm;

    private byte[] device;

    private byte[] data;

    protected ForwardMessage() {
    }

    public ForwardMessage(String name, byte[] rc, byte[] lm, byte[] device, byte[] data) {
        this();
        this.name = name;
        this.rc = rc;
        this.lm = lm;
        this.device = device;
        this.data = data;
    }

    public static ForwardMessage from(byte[] data) {
        try {
            Proto.Message.Forward message = Proto.Message.Forward.parseFrom(data);
            return new Builder()
                .id(message.getId())
                .name(message.getName())
                .rc(message.getRc().toByteArray())
                .lm(message.getLm().toByteArray())
                .device(message.getDevice().toByteArray())
                .data(message.getMessage().toByteArray())
                .build();
        } catch (Throwable throwable) {
            throw new InvalidValueException("data");
        }
    }

    public byte[] toByteArray() {
        return
            Proto.Message.Forward.newBuilder()
                .setId(this.id())
                .setName(this.name())
                .setRc(ByteString.copyFrom(this.rc()))
                .setLm(ByteString.copyFrom(this.lm()))
                .setDevice(ByteString.copyFrom(this.device()))
                .setMessage(ByteString.copyFrom(this.data()))
                .build()
                .toByteArray();
    }

    public String name() {
        return name;
    }

    public byte[] rc() {
        return rc;
    }

    public byte[] lm() {
        return lm;
    }

    public byte[] device() {
        return device;
    }

    public byte[] data() {
        return data;
    }

    @Override
    public Byte format() {
        return 3;
    }

    public static class Builder {
        private ForwardMessage message = new ForwardMessage();

        public Builder id(long id) {
            this.message.id = id;
            return this;
        }

        public Builder name(String name) {
            this.message.name = name;
            return this;
        }

        public Builder rc(byte[] rc) {
            this.message.rc = rc;
            return this;
        }

        public Builder lm(byte[] lm) {
            this.message.lm = lm;
            return this;
        }

        public Builder device(byte[] device) {
            this.message.device = device;
            return this;
        }

        public Builder data(byte[] data) {
            this.message.data = data;
            return this;
        }

        public ForwardMessage build() {
            return this.message;
        }
    }
}
