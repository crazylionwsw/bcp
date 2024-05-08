package com.fuze.bcp.exception;

/**
 * Created by CJ on 2017/9/19.
 */
public class MessageMqException extends Exception {

    public MessageMqException() {
    }

    public MessageMqException(String message) {
        super(message);
    }

    public MessageMqException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageMqException(Throwable cause) {
        super(cause);
    }

    public MessageMqException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
