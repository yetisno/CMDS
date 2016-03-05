package org.yetiz.utils.cmds.exception;

/**
 * cmds
 * Created by yeti on 16/3/5.
 */
public class CMDSException extends RuntimeException {
    public CMDSException() {
    }

    public CMDSException(String message) {
        super(message);
    }

    public CMDSException(Throwable cause) {
        super(cause);
    }

    public CMDSException(String message, Throwable cause) {
        super(message, cause);
    }
}
