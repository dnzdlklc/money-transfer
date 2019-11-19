package com.dnzdlklc.exception;

/**
 * Created by denizdalkilic on 2019-11-03 @ 16:15.
 */
public class InvalidAmountValueException extends IllegalArgumentException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public InvalidAmountValueException(String message) {
        super(message);
    }
}