package com.dnzdlklc.exception;

/**
 * Created by denizdalkilic on 2019-11-03 @ 16:15.
 */
public class AccountNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public AccountNotFoundException(String message) {
        super(message);
    }
}
