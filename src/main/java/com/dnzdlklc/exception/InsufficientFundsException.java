package com.dnzdlklc.exception;

/**
 * Created by denizdalkilic on 2019-11-03 @ 16:15.
 */
public class InsufficientFundsException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public InsufficientFundsException(String message) {
        super(message);
    }
}