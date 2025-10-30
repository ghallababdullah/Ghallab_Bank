package com.ghallab.Ghallab_Bank.exceptions;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String error) {
        super(error);
    }
}
