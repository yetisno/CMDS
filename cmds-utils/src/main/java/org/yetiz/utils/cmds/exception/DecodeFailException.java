package org.yetiz.utils.cmds.exception;

/**
 * cmds
 * Created by yeti on 16/3/6.
 */
public class DecodeFailException extends CMDSException {
    public DecodeFailException() {
    }

    public DecodeFailException(String message) {
        super(message);
    }

    public DecodeFailException(Throwable cause) {
        super(cause);
    }

    public DecodeFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
