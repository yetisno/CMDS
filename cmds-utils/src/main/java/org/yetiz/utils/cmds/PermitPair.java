package org.yetiz.utils.cmds;

import org.yetiz.utils.cmds.exception.InvalidPermitNameException;
import org.yetiz.utils.cmds.exception.InvalidValueException;
import org.yetiz.utils.cmds.exception.ThisCannotOccurException;
import org.yetiz.utils.cmds.utils.Check;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * cmds
 * Created by yeti on 16/3/5.
 */
public class PermitPair {

    private static final int TOTAL_MAX = 77;
    private static final int NAME_SIZE = Long.BYTES;
    private static final int SERIAL_SIZE = Integer.BYTES;
    private static final int CHECKSUM_SIZE = 1;
    private static final int PAYLOAD_SIZE = 64;
    private static final int SECRET_SIZE = 32;
    private String name;
    private int serial;
    private byte[] payload;
    private byte[] secret;

    public PermitPair(String name, byte[] data, byte[] secret) {
        this.secret = secret;
        block1Check(data);
        block1Decode(name, data);
        payloadDecode();
    }

    public PermitPair(String name, int serial, byte[] payload, byte[] secret) {
        try {
            Long.parseLong(name);
        } catch (Throwable throwable) {
            throw new InvalidPermitNameException();
        }

        Check.byteLength(payload, PAYLOAD_SIZE, "payload");
        Check.byteLength(secret, SECRET_SIZE, "secret");

        this.name = name;
        this.serial = serial;
        this.payload = payload;
        this.secret = secret;
    }

    private static void block1Check(byte[] data) {
        Check.byteLength(data, TOTAL_MAX, "data");

        byte target = 0x00;
        for (int i = 0; i < TOTAL_MAX - CHECKSUM_SIZE; i++) {
            target ^= data[i];
        }

        if (target != data[TOTAL_MAX - CHECKSUM_SIZE]) {
            throw new InvalidValueException();
        }
    }

    private void block1Decode(String name, byte[] data) {
        byte secret = 0x00;
        byte[] nameData;
        try {
            nameData = ByteBuffer.allocate(Long.BYTES).putLong(Long.parseLong(name)).array();
            for (int i = 0; i < nameData.length; i++) {
                secret ^= nameData[i];
            }
        } catch (Throwable throwable) {
            throw new InvalidPermitNameException();
        }

        byte[] result = new byte[TOTAL_MAX - CHECKSUM_SIZE];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (data[i] ^ secret);
        }

        for (int i = 0; i < nameData.length; i++) {
            if (nameData[i] != result[i]) {
                throw new InvalidValueException();
            }
        }

        this.name = name;
        this.serial = ByteBuffer.wrap(result, NAME_SIZE, Integer.BYTES).order(ByteOrder.BIG_ENDIAN).getInt();
        this.payload = new byte[PAYLOAD_SIZE];

        for (int i = NAME_SIZE + SERIAL_SIZE; i < result.length; i++) {
            this.payload[i - (NAME_SIZE + SERIAL_SIZE)] = result[i];
        }
    }


    private void payloadDecode() {
        Check.byteLength(secret, SECRET_SIZE, "secret");

        SecretKey secretKey = new SecretKeySpec(secret, "Blowfish");
        try {
            Cipher blowfish = Cipher.getInstance("Blowfish/ECB/NoPadding");
            blowfish.init(Cipher.DECRYPT_MODE, secretKey);
            this.payload = blowfish.doFinal(this.payload);
        } catch (Throwable throwable) {
            throw new ThisCannotOccurException();
        }
    }

    public PermitPair secret(byte[] secret) {
        this.secret = secret;
        return this;
    }

    public String name() {
        return name;
    }

    public int serial() {
        return serial;
    }

    public byte[] payload() {
        return payload;
    }

    public byte[] secret() {
        return secret;
    }
}
