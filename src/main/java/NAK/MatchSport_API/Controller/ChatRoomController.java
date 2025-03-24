package NAK.MatchSport_API.Controller;


import NAK.MatchSport_API.Dto.request.ChatRoomRequest;
import NAK.MatchSport_API.Dto.response.ChatRoomResponse;
import NAK.MatchSport_API.Service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<ChatRoomResponse>> getAllChatRooms() {
        return ResponseEntity.ok(chatRoomService.getAllChatRooms());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ChatRoomResponse> getChatRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(chatRoomService.getChatRoomById(id));
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ChatRoomResponse> getChatRoomByEventId(@PathVariable Long eventId) {
        return ResponseEntity.ok(chatRoomService.getChatRoomByEventId(eventId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ChatRoomResponse> createChatRoom(@Valid @RequestBody ChatRoomRequest chatRoomRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomService.createChatRoom(chatRoomRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ChatRoomResponse> updateChatRoom(@PathVariable Long id, @Valid @RequestBody ChatRoomRequest chatRoomRequest) {
        return ResponseEntity.ok(chatRoomService.updateChatRoom(id, chatRoomRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long id) {
        chatRoomService.deleteChatRoom(id);
        return ResponseEntity.noContent().build();
    }
}
