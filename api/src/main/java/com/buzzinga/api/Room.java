package com.buzzinga.api;

import java.util.ArrayList;
import java.util.List;

import com.buzzinga.api.Exceptions.PlayerNotFoundException;
import com.buzzinga.api.Exceptions.UserNameTakenException;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class Room {

    private Player host;
    private String roomName;
    private List<Player> players;
    private List<Player> buzzed;
    private boolean isBuzzerActive;
    private int roomId;

    

    public boolean isBuzzerActive() {
        return isBuzzerActive;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setBuzzerActive(boolean isBuzzerActive) {
        this.isBuzzerActive = isBuzzerActive;
    }

    Room(int id) {
        players = new ArrayList<>();
        buzzed = new ArrayList<>();
        this.roomId = id;
    }

    Room(Player host, String roomName, int id) {
        this.roomId = id;
        this.host = host;
        this.roomName = roomName;
        players = new ArrayList<>();
        buzzed = new ArrayList<>();
    }

    public List<Player> playerBuzz(String username) throws PlayerNotFoundException {
        int ind = userExists(username);
        if(ind >= 0) {
            buzzed.add(players.get(ind));
            return buzzed;            
        }
        throw new PlayerNotFoundException("Player does not exist");
    }

    private int userExists(String username) {
        for(int i=0; i<players.size(); i++) {
            if(players.get(i).getUsername().equals(username))
                return i;
        }
        return -1;
    }

    public void clearBuzzer() {
        this.buzzed = new ArrayList<>();
    }

    public List<Player> getBuzzed() {
        return buzzed;
    }

    public void setBuzzed(List<Player> buzzed) {
        this.buzzed = buzzed;
    }

    public void addPlayer(Player player) throws UserNameTakenException {
        if(userExists(player.getUsername()) < 0)
            players.add(player);
        throw new UserNameTakenException("username already taken");
    }

    public Player getHost() {
        return host;
    }
    public void setHost(Player host) {
        this.host = host;
    }
    public String getRoomName() {
        return roomName;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return "Room [host=" + host.getUsername() + ", players=" + players + ", roomName=" + roomName + "]";
    }

    public void registerPlayer(String username, SseEmitter sseEmitter) {
        for(Player player: players) {
            if(username.equals(player.getUsername())) {
                player.register(sseEmitter);
                break;
            }
        }
    }
    
    public boolean playerExists(String username) {
        for(Player player: players) {
            if(player.getUsername().equals(username))
                return true;
        }
        return false;
    }
    
}
