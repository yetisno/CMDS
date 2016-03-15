package utils.cmds;

import utils.cmds.exception.InvalidPermitNameException;
import utils.cmds.exception.InvalidValueException;
import utils.cmds.exception.ThisCannotOccurException;
import utils.cmds.utils.Check;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;

/**
 * cmds
 * Created by yeti on 16/3/5.
 */
public class PermitPair {

    private static final int TOTAL_MAX = 73;
    private static final int NAME_SIZE = Long.BYTES;
    private static final int CHECKSUM_SIZE = 1;
    private static final int PAYLOAD_SIZE = 64;
    private static final int SECRET_SIZE = 32;
    private String name;
    private byte[] cipher;
    private byte[] payload;
    private byte[] secret;

    public PermitPair(byte[] data) {
        block1Check(data);
        block1Decode(data);
    }

    public PermitPair(byte[] data, byte[] secret) {
        this(data);
        this.secret = secret;
        payloadDecode();
    }

    public PermitPair(String name, byte[] payload, byte[] secret) {
        try {
            Long.parseLong(name);
        } catch (Throwable throwable) {
            throw new InvalidPermitNameException();
        }

        Check.byteLength(payload, PAYLOAD_SIZE, "payload");
        Check.byteLength(secret, SECRET_SIZE, "secret");

        this.name = name;
        this.payload = payload;
        this.secret = secret;
    }

    private static void block1Check(byte[] data) {
        Check.byteLength(data, TOTAL_MAX, "data");

        byte checksum = data[TOTAL_MAX - CHECKSUM_SIZE];
        byte[] tmpData = new byte[TOTAL_MAX - CHECKSUM_SIZE];
        System.arraycopy(data, 0, tmpData, 0, TOTAL_MAX - CHECKSUM_SIZE);

        for (int i = 0; i < TOTAL_MAX - CHECKSUM_SIZE; i++) {
            tmpData[i] ^= checksum;
        }

        byte fold = tmpData[0];
        for (int i = 0; i < TOTAL_MAX - CHECKSUM_SIZE; i++) {
            fold ^= tmpData[i];
        }

        if (fold != checksum) {
            throw new InvalidValueException();
        }
    }

    private void block1Decode(byte[] data) {
        byte checksum = data[TOTAL_MAX - CHECKSUM_SIZE];
        byte[] tmpData = new byte[TOTAL_MAX - CHECKSUM_SIZE];
        System.arraycopy(data, 0, tmpData, 0, TOTAL_MAX - CHECKSUM_SIZE);

        for (int i = 0; i < TOTAL_MAX - CHECKSUM_SIZE; i++) {
            tmpData[i] ^= checksum;
        }

        try {
            this.name = String.valueOf(ByteBuffer.wrap(tmpData, 0, NAME_SIZE).getLong());
        } catch (Throwable throwable) {
            throw new InvalidValueException("name");
        }

        this.cipher = ByteBuffer.allocate(PAYLOAD_SIZE)
            .put(tmpData, 8, PAYLOAD_SIZE)
            .array();
    }


    private void payloadDecode() {
        Check.byteLength(secret, SECRET_SIZE, "secret");

        SecretKey secretKey = new SecretKeySpec(secret, "Blowfish");
        try {
            Cipher blowfish = Cipher.getInstance("Blowfish/ECB/NoPadding");
            blowfish.init(Cipher.DECRYPT_MODE, secretKey);
            this.payload = blowfish.doFinal(this.cipher);
        } catch (Throwable throwable) {
            throw new ThisCannotOccurException();
        }
    }

    private void payloadEncode() {
        Check.byteLength(secret, SECRET_SIZE, "secret");

        SecretKey secretKey = new SecretKeySpec(secret, "Blowfish");
        try {
            Cipher blowfish = Cipher.getInstance("Blowfish/ECB/NoPadding");
            blowfish.init(Cipher.ENCRYPT_MODE, secretKey);
            this.cipher = blowfish.doFinal(this.payload);
        } catch (Throwable throwable) {
            throw new ThisCannotOccurException(throwable);
        }
    }

    public byte[] encode() {
        if (cipher == null) {
            payloadEncode();
        }

        byte[] tmpData = ByteBuffer.allocate(NAME_SIZE + PAYLOAD_SIZE)
            .putLong(Long.parseLong(name))
            .put(this.cipher)
            .array();

        byte checksum = tmpData[0];
        for (int i = 1; i < tmpData.length; i++) {
            checksum ^= tmpData[i];
        }

        return ByteBuffer.allocate(NAME_SIZE + PAYLOAD_SIZE + CHECKSUM_SIZE)
            .put(tmpData)
            .put(checksum)
            .array();
    }

    public PermitPair secret(byte[] secret) {
        this.secret = secret;
        return this;
    }

    public String name() {
        return name;
    }

    public byte[] payload() {
        return payload;
    }

    public byte[] secret() {
        return secret;
    }
}
