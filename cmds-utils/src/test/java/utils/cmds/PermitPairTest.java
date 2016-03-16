package utils.cmds;

import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;

/**
 * cmds
 * Created by yeti on 16/3/9.
 */
public class PermitPairTest {

    @Test
    public void parse() throws Exception {
        String dataHex =
            "8E8E8E8E8E8EA42AC07719CBEF1653F6C07719CBEF1653F6C07719CBEF1653F6C07719CBEF1653F6C07719CBEF1653F6C07719CBEF1653F6C07719CBEF1653F6C07719CBEF1653F68E";
        byte[] data = DatatypeConverter.parseHexBinary(dataHex);
        byte[] secret = DatatypeConverter.parseHexBinary
            ("0000000000000000000000000000000000000000000000000000000000000000");
        byte[] payload = DatatypeConverter.parseHexBinary
            ("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");

        PermitPair permitPair = new PermitPair("10916", payload, secret);
        PermitPair enPermitPair = new PermitPair(data, secret);
        permitPair.encode();

        Assert.assertEquals(enPermitPair.name(), permitPair.name());
        Assert.assertArrayEquals(enPermitPair.payload(), permitPair.payload());
        Assert.assertArrayEquals(enPermitPair.secret(), permitPair.secret());
    }
}