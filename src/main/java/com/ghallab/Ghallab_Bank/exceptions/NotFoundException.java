package com.ghallab.Ghallab_Bank.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String error) {
        super(error);
    }
}
