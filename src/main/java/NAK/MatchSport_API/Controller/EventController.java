package NAK.MatchSport_API.Controller;


import NAK.MatchSport_API.Dto.request.EventRequest;
import NAK.MatchSport_API.Dto.response.EventResponse;
import NAK.MatchSport_API.Service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Page<EventResponse>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        return ResponseEntity.ok(eventService.getAllEvents(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventResponse>> searchEvents(@RequestParam String keyword) {
        return ResponseEntity.ok(eventService.searchEvents(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('PARTICIPANT') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest eventRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(eventRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PARTICIPANT') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequest eventRequest) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PARTICIPANT') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/join")
    @PreAuthorize("hasRole('PARTICIPANT') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<EventResponse> joinEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.joinEvent(id));
    }

    @PostMapping("/{id}/leave")
    @PreAuthorize("hasRole('PARTICIPANT') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<EventResponse> leaveEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.leaveEvent(id));
    }
}
