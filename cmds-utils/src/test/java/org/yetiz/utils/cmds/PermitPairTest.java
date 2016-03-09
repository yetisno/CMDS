package org.yetiz.utils.cmds;

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
            "8e8e8e8e8e8ea42ac07719cbef1653f6c07719cbef1653f6c07719cbef1653f6c07719cbef1653f6c07719cbef1653f6c07719cbef1653f6c07719cbef1653f6c07719cbef1653f68e";
        byte[] data = DatatypeConverter.parseHexBinary(dataHex);
        byte[] secret = DatatypeConverter.parseHexBinary
            ("0000000000000000000000000000000000000000000000000000000000000000");
        byte[] payload = DatatypeConverter.parseHexBinary
            ("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");

        PermitPair permitPair = new PermitPair(data, secret);

        Assert.assertEquals("10916", permitPair.name());
        Assert.assertArrayEquals(payload, permitPair.payload());
    }
}