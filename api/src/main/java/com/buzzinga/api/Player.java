package com.buzzinga.api;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class Player {
    private SseEmitter sseEmitter;
    private String username;

    Player() {}

    Player(String username) {
        this.username = username;
    }

    public SseEmitter getSseEmitter() {
        return sseEmitter;
    }
    public void setSseEmitter(SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Player [username=" + username + "]";
    }

    public void register(SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
    }

    
}
