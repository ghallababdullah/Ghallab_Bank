package com.ghallab.Ghallab_Bank.exceptions;

public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException(String error) {
        super(error);
    }
}
