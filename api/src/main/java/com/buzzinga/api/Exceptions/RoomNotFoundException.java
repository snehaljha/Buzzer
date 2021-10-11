package com.buzzinga.api.Exceptions;

public class RoomNotFoundException extends Exception {
    private String MSG;

    public RoomNotFoundException(String message) {
        super(message);
        this.MSG = message;
    }

    @Override
    public String toString() {
        return MSG;
    }
    
}
