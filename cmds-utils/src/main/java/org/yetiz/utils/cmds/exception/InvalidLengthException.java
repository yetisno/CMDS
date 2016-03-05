package org.yetiz.utils.cmds.exception;

/**
 * cmds
 * Created by yeti on 16/3/5.
 */
public class InvalidLengthException extends CMDSException {
    public InvalidLengthException() {
    }

    public InvalidLengthException(String message) {
        super(message);
    }

    public InvalidLengthException(Throwable cause) {
        super(cause);
    }

    public InvalidLengthException(String message, Throwable cause) {
        super(message, cause);
    }
}
