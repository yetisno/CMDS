package org.yetiz.utils.cmds.messages;

import com.google.protobuf.ByteString;
import org.yetiz.utils.cmds.exception.InvalidValueException;
import org.yetiz.utils.cmds.messages.proto.Proto;

import java.util.ArrayList;
import java.util.List;

/**
 * cmds
 * Created by yeti on 16/3/5.
 */
public class NormalMessage extends DefaultMessage {
    private String name;

    private List<byte[]> dids;

    private byte[] data;

    private NormalMessage() {
    }

    public NormalMessage(String name, List<byte[]> dids, byte[] data) {
        this.name = name;
        this.dids = dids;
        this.data = data;
    }

    public static NormalMessage from(byte[] data) {
        try {
            Proto.Message.Normal message = Proto.Message.Normal.parseFrom(data);
            Builder builder = new Builder()
                .id(message.getId())
                .name(message.getName())
                .data(message.getMessage().toByteArray());
            List<byte[]> didList = message
                .getDidList()
                .stream()
                .collect(ArrayList::new, (list, byteString) -> list.add(byteString.toByteArray()), ArrayList::addAll);

            return builder.dids(didList)
                .build();
        } catch (Throwable throwable) {
            throw new InvalidValueException("data");
        }
    }

    public byte[] toByteArray() {
        return
            Proto.Message.Normal.newBuilder()
                .setId(this.id())
                .setName(this.name())
                .setMessage(ByteString.copyFrom(this.data()))
                .addAllDid(dids()
                        .stream()
                        .collect(ArrayList::new,
                            (list, byteArray) -> list.add(ByteString.copyFrom(byteArray)),
                            ArrayList::addAll)
                )
                .build()
                .toByteArray();
    }

    public String name() {
        return name;
    }

    public byte[] data() {
        return data;
    }

    public List<byte[]> dids() {
        return dids;
    }

    public static class Builder {
        private NormalMessage message = new NormalMessage();

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

        public Builder dids(List<byte[]> dids) {
            this.message.dids = dids;
            return this;
        }

        public NormalMessage build() {
            return this.message;
        }
    }
}
