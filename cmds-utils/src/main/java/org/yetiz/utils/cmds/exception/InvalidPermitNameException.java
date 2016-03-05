package org.yetiz.utils.cmds.exception;

/**
 * cmds
 * Created by yeti on 16/3/5.
 */
public class InvalidPermitNameException extends CMDSException {
    public InvalidPermitNameException() {
    }

    public InvalidPermitNameException(String message) {
        super(message);
    }

    public InvalidPermitNameException(Throwable cause) {
        super(cause);
    }

    public InvalidPermitNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
