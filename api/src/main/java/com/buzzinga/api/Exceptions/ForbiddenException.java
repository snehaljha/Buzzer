package com.buzzinga.api.Exceptions;

public class ForbiddenException extends Exception {
    private String MSG;

    public ForbiddenException(String message) {
        super(message);
        this.MSG = message;
    }

    @Override
    public String toString() {
        return this.MSG;
    }
    
}
