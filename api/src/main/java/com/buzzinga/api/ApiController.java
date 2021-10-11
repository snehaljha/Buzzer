package com.buzzinga.api;

import com.buzzinga.api.Exceptions.BuzzerStatePresentException;
import com.buzzinga.api.Exceptions.ForbiddenException;
import com.buzzinga.api.Exceptions.PlayerNotFoundException;
import com.buzzinga.api.Exceptions.RoomNotFoundException;
import com.buzzinga.api.Exceptions.UserNameTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequestMapping("/buzzinga/api")
public class ApiController {
    
    @CrossOrigin
    @GetMapping(path = "/hostConnect", produces = MediaType.ALL_VALUE)
    public SseEmitter hostConnect(@RequestParam int roomId) {
        try {
            return ApiService.registerHost(roomId);
        } catch(RoomNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @CrossOrigin
    @GetMapping(path = "/playerConnect", produces = MediaType.ALL_VALUE)
    public SseEmitter playerConnect(@RequestParam int roomId, String username) {
        try {
            return ApiService.registerPlayer(roomId, username);
        } catch (ForbiddenException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/joinRoom")
    public ResponseEntity<?> joinRoom(@RequestParam String username, @RequestParam int roomId) {
        try {
            return ResponseEntity.ok(ApiService.joinRoom(username, roomId));
        } catch(RoomNotFoundException e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
        } catch(UserNameTakenException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_ACCEPTABLE);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Server error :(", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createRoom")
    public ResponseEntity<?> createRoom(@RequestParam String username, @RequestParam String roomName) {
        try {
            return ResponseEntity.ok(ApiService.generateRoom(username, roomName).toString());
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Server error :(", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/freezeBuzzer")
    public ResponseEntity<String> freezeBuzzer(@RequestParam int roomId, @RequestParam String username) {
        try {
            ApiService.freezeBuzzer(roomId, username);
            return ResponseEntity.ok("Done");
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        } catch (BuzzerStatePresentException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Server error :(", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/unfreezeBuzzer")
    public ResponseEntity<String> unfreezeBuzzer(@RequestParam int roomId, @RequestParam String username) {
        try {
            ApiService.unfreezeBuzzer(roomId, username);
            return ResponseEntity.ok("Done");
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        } catch (BuzzerStatePresentException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Server error :(", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/resetBuzzer")
    public ResponseEntity<String> resetBuzzer(@RequestParam int roomId, @RequestParam String username) {
        try {
            ApiService.resetBuzzer(roomId, username);
            return ResponseEntity.ok("Done");
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Server error :(", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/buzz")
    public ResponseEntity<String> buzz(@RequestParam int roomId, @RequestParam String username) {
        try {
            ApiService.buzz(roomId, username);
            return ResponseEntity.ok("Done");
        } catch (RoomNotFoundException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
        } catch (PlayerNotFoundException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Server error :(", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
