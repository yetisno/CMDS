package org.yetiz.utils.cmds;

import org.yetiz.utils.cmds.exception.InvalidPermitNameException;
import org.yetiz.utils.cmds.exception.InvalidValueException;
import org.yetiz.utils.cmds.exception.ThisCannotOccurException;

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
    private static final int BLOCK2_SIZE = 64;
    private static final int BLOCK2_SECRET_SIZE = 32;
    private String name;
    private int serial;
    private byte[] block2;

    public PermitPair(String name, byte[] data) {
        Block1Check(data);
        part1Decode(name, data);
    }

    private static void Block1Check(byte[] data) {
        if (data.length != TOTAL_MAX) {
            throw new InvalidValueException();
        }

        byte target = 0x00;
        for (int i = 0; i < TOTAL_MAX - CHECKSUM_SIZE; i++) {
            target ^= data[i];
        }

        if (target != data[TOTAL_MAX - CHECKSUM_SIZE]) {
            throw new InvalidValueException();
        }
    }

    private void part1Decode(String name, byte[] data) {
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
        this.block2 = new byte[BLOCK2_SIZE];

        for (int i = NAME_SIZE + SERIAL_SIZE; i < result.length; i++) {
            this.block2[i - (NAME_SIZE + SERIAL_SIZE)] = result[i];
        }
    }

    public byte[] block2Decode(byte[] secret) {
        if (secret.length != BLOCK2_SECRET_SIZE) {
            throw new InvalidValueException();
        }

        SecretKey secretKey = new SecretKeySpec(secret, "Blowfish");
        try {
            Cipher blowfish = Cipher.getInstance("Blowfish");
            blowfish.init(Cipher.DECRYPT_MODE, secretKey);
            return blowfish.doFinal(this.block2);
        } catch (Throwable throwable) {
            throw new ThisCannotOccurException();
        }
    }

    public int serial() {
        return serial;
    }

    public String name() {
        return name;
    }
}
