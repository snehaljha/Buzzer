package com.buzzinga.api.Exceptions;

public class PlayerNotFoundException extends Exception {
    private String MSG;

    public PlayerNotFoundException(String message) {
        super(message);
        this.MSG = message;
    }

    @Override
    public String toString() {
        return this.MSG;
    }
}
