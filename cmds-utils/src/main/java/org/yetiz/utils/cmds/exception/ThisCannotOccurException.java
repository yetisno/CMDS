package org.yetiz.utils.cmds.exception;

/**
 * cmds
 * Created by yeti on 16/3/5.
 */
public class ThisCannotOccurException extends CMDSException {
    public ThisCannotOccurException() {
    }

    public ThisCannotOccurException(String message) {
        super(message);
    }

    public ThisCannotOccurException(Throwable cause) {
        super(cause);
    }

    public ThisCannotOccurException(String message, Throwable cause) {
        super(message, cause);
    }
}
