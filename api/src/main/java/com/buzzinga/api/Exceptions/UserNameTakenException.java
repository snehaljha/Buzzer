package com.buzzinga.api.Exceptions;

public class UserNameTakenException extends Exception {

    private String MSG;

    public UserNameTakenException(String message) {
        super(message);
        this.MSG = message;
    }

    @Override
    public String toString() {
        return this.MSG;
    }
    
}
