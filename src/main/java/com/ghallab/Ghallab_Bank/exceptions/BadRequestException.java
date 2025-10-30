package com.ghallab.Ghallab_Bank.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String error) {
        super(error);
    }
}
