package com.envechat.backend.controller;
 
import com.envechat.backend.model.Room;
import com.envechat.backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.security.Principal;
import java.util.List;
import java.util.Map;
 
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
 
    private final RoomRepository roomRepository;
 
    // GET /api/rooms — list all rooms
    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
 
    // POST /api/rooms — create a new room
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody Map<String, String> body,
                                         Principal principal) {
        String name = body.get("name");
        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body("Room name is required");
        }
 
        if (roomRepository.existsByName(name)) {
            return ResponseEntity.badRequest().body("Room already exists");
        }
 
        Room room = new Room();
        room.setName(name.toLowerCase().replaceAll("\\s+", "-"));
        room.setCreatedBy(principal.getName());
 
        return ResponseEntity.ok(roomRepository.save(room));
    }
 
    // GET /api/rooms/{id} — get a single room
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoom(@PathVariable Long id) {
        return roomRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}