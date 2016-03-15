package utils.cmds.utils;

import utils.cmds.exception.InvalidLengthException;
import utils.cmds.exception.InvalidValueException;

/**
 * cmds
 * Created by yeti on 16/3/5.
 */
public class Check {

    public static void byteLength(byte[] val, int length, String name) {
        try {
            if (val.length != length) {
                throw new InvalidLengthException(name == null ?
                    String.format("length is %d, but expect %d", val.length, length) :
                    String.format("%s length is %d, but expect %d", name, val.length, length));
            }
        } catch (InvalidLengthException ex) {
            throw ex;
        } catch (Throwable throwable) {
            throw new InvalidValueException(name == null ?
                String.format("Null val") :
                String.format("%s is null", name));
        }
    }
}
