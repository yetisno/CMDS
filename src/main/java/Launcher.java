import org.yetiz.utils.cmds.exception.ThisCannotOccurException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

/**
 * cmds
 * Created by yeti on 16/2/29.
 */
public class Launcher {

    public static void main(String... args) throws NoSuchAlgorithmException {
        byte[] secret = new byte[8];
        for (int i = 0; i < secret.length; i++) {
            secret[i] = 0x00;
        }

        byte[] payload = new byte[56];
        for (int i = 0; i < payload.length; i++) {
            payload[i] = 0x00;
        }

        SecretKey secretKey = new SecretKeySpec(secret, "Blowfish");
        try {
            Cipher blowfish = Cipher.getInstance("Blowfish/ECB/NoPadding");
            blowfish.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] data = blowfish.doFinal(payload);
            System.out.println(data.length);
            blowfish.init(Cipher.DECRYPT_MODE, secretKey);
            data = blowfish.doFinal(data);
            System.out.println(data.length);
        } catch (Throwable throwable) {
            throw new ThisCannotOccurException();
        }

    }
}
