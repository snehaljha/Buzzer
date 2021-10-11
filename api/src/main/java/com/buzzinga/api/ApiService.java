package com.buzzinga.api;

import java.util.HashMap;
import java.util.Random;

import com.buzzinga.api.Exceptions.BuzzerStatePresentException;
import com.buzzinga.api.Exceptions.ForbiddenException;
import com.buzzinga.api.Exceptions.PlayerNotFoundException;
import com.buzzinga.api.Exceptions.RoomNotFoundException;
import com.buzzinga.api.Exceptions.UserNameTakenException;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class ApiService {
    static HashMap<Integer, Room> roomSpace = new HashMap<>();

    private static int generateRoomId() {
        Random random = new Random();
        int id = 1000 + random.nextInt(9000);
        while(roomSpace.containsKey(id)) {
            id = 1000 + random.nextInt(9000);
        }
        return id;
    }

    public static Room generateRoom(String username, String roomName) {
        int roomId = generateRoomId();
        Player host = new Player(username);
        Room room = new Room(host, roomName, roomId);
        roomSpace.put(roomId, room);
        return room;
    }

    public static SseEmitter registerHost(int roomId) throws RoomNotFoundException {
        if(!roomSpace.containsKey(roomId))
            throw new RoomNotFoundException("Room not found");
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        Room room = roomSpace.get(roomId);
        room.getHost().setSseEmitter(sseEmitter);
        return sseEmitter;
    }

    public static void freezeBuzzer(int roomId, String username) throws ForbiddenException, BuzzerStatePresentException {
        if(!validateHost(roomId, username))
            throw new ForbiddenException("You don't have permisiion to change buzzer state");
        
        Room room = roomSpace.get(roomId);
        if(!room.isBuzzerActive())
            throw new BuzzerStatePresentException("Buzzer already freezed");
        
        room.setBuzzerActive(false);
    }

    public static void unfreezeBuzzer(int roomId, String username) throws ForbiddenException, BuzzerStatePresentException {
        if(!validateHost(roomId, username))
            throw new ForbiddenException("You don't have permisiion to change buzzer state");
        
        Room room = roomSpace.get(roomId);
        if(room.isBuzzerActive())
            throw new BuzzerStatePresentException("Buzzer already active");
        
        room.setBuzzerActive(true);
    }

    private static boolean validateHost(int roomId, String username) {
        if(!roomSpace.containsKey(roomId))
            return false;
        if(!roomSpace.get(roomId).getHost().getUsername().equals(username))
            return false;
        return true;
    }

    public static void resetBuzzer(int roomId, String username) throws ForbiddenException {
        if(!validateHost(roomId, username))
            throw new ForbiddenException("You don't have permission to reset buzzed players");
        
        roomSpace.get(roomId).clearBuzzer();
    }

    public static Player joinRoom(String username, int roomId) throws RoomNotFoundException, UserNameTakenException {
        if(!roomSpace.containsKey(roomId))
            throw new RoomNotFoundException("Room not found");
        
        Player player = new Player(username);
        roomSpace.get(roomId).addPlayer(player);
        return player;
    }

    public static SseEmitter registerPlayer(int roomId, String username) throws ForbiddenException {
        if(!validateUser(roomId, username))
            throw new ForbiddenException("Action not allowed");
        
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        roomSpace.get(roomId).registerPlayer(username, sseEmitter);
        return sseEmitter;
    }

    private static boolean validateUser(int roomId, String username) {
        if(!roomSpace.containsKey(roomId) || !roomSpace.get(roomId).playerExists(username))
            return false;
        return true;
    }

    public static void buzz(int roomId, String username) throws PlayerNotFoundException, RoomNotFoundException {
        if(!roomSpace.containsKey(roomId))
            throw new RoomNotFoundException("Not allowed");
        roomSpace.get(roomId).playerBuzz(username);
    }

}
