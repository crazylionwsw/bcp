package com.fuze.bcp.exception;

/**
 * Created by CJ on 2017/7/18.
 */
public class BusinessMQException extends Exception {

    public BusinessMQException() {
    }

    public BusinessMQException(String message) {
        super(message);
    }

    public BusinessMQException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessMQException(Throwable cause) {
        super(cause);
    }
}
