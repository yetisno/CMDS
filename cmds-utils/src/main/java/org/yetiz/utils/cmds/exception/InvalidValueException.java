package org.yetiz.utils.cmds.exception;

/**
 * cmds
 * Created by yeti on 16/3/5.
 */
public class InvalidValueException extends CMDSException {
    public InvalidValueException() {
    }

    public InvalidValueException(String message) {
        super(message);
    }

    public InvalidValueException(Throwable cause) {
        super(cause);
    }

    public InvalidValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
