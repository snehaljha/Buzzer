package com.buzzinga.api.Exceptions;

public class BuzzerStatePresentException extends Exception {

    private String MSG;

    public BuzzerStatePresentException(String message) {
        super(message);
        this.MSG = message;
    }

    @Override
    public String toString() {
        return this.MSG;
    }
    
}
