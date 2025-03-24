package NAK.MatchSport_API.Service;

import NAK.MatchSport_API.Dto.request.EventRequest;
import NAK.MatchSport_API.Dto.response.EventResponse;
import NAK.MatchSport_API.Entity.*;
import NAK.MatchSport_API.Entity.Embedded.EventParticipantIds;

import NAK.MatchSport_API.Mapper.EventMapper;
import NAK.MatchSport_API.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final VenueRepository venueRepository;
    private final MediaRepository mediaRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final EventMapper eventMapper;

    public Page<EventResponse> getAllEvents(Pageable pageable) {
        Page<Event> events = eventRepository.findAll(pageable);
        return events.map(eventMapper::eventToEventResponse);
    }

    public List<EventResponse> searchEvents(String keyword) {
        List<Event> events = eventRepository.findByNameContainingIgnoreCase(keyword);
        return eventMapper.eventsToEventResponses(events);
    }

    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        return eventMapper.eventToEventResponse(event);
    }

    @Transactional
    public EventResponse createEvent(EventRequest eventRequest) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Participant creator = participantRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        Venue venue = venueRepository.findById(eventRequest.getVenueId())
                .orElseThrow(() -> new RuntimeException("Venue not found with id: " + eventRequest.getVenueId()));

        Event event = eventMapper.eventRequestToEvent(eventRequest);
        event.setEventCreator(creator);
        event.setVenue(venue);

        // Set event image if provided
        if (eventRequest.getEventImageId() != null) {
            Media eventImage = mediaRepository.findById(eventRequest.getEventImageId())
                    .orElseThrow(() -> new RuntimeException("Media not found with id: " + eventRequest.getEventImageId()));
            event.setEventImage(eventImage);
        }

        // Create chat room for the event
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(event.getName() + " Chat");
        chatRoom.setEvent(event);
        event.setChatRoom(chatRoom);

        Event savedEvent = eventRepository.save(event);
        return eventMapper.eventToEventResponse(savedEvent);
    }

    @Transactional
    public EventResponse updateEvent(Long id, EventRequest eventRequest) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        // Check if the current user is the creator of the event
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        if (!event.getEventCreator().getEmail().equals(currentUserEmail) &&
                !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            throw new RuntimeException("You are not authorized to update this event");
        }

        // Update venue if changed
        if (!event.getVenue().getId().equals(eventRequest.getVenueId())) {
            Venue venue = venueRepository.findById(eventRequest.getVenueId())
                    .orElseThrow(() -> new RuntimeException("Venue not found with id: " + eventRequest.getVenueId()));
            event.setVenue(venue);
        }

        // Update event image if provided
        if (eventRequest.getEventImageId() != null &&
                (event.getEventImage() == null || !event.getEventImage().getId().equals(eventRequest.getEventImageId()))) {
            Media eventImage = mediaRepository.findById(eventRequest.getEventImageId())
                    .orElseThrow(() -> new RuntimeException("Media not found with id: " + eventRequest.getEventImageId()));
            event.setEventImage(eventImage);
        }

        // Update basic properties
        event.setName(eventRequest.getName());
        event.setDate(eventRequest.getDate());
        event.setEventType(eventRequest.getEventType());
        event.setMaxParticipants(eventRequest.getMaxParticipants());

        // Update chat room name if event name changed
        if (event.getChatRoom() != null && !event.getChatRoom().getName().equals(eventRequest.getName() + " Chat")) {
            event.getChatRoom().setName(eventRequest.getName() + " Chat");
        }

        Event updatedEvent = eventRepository.save(event);
        return eventMapper.eventToEventResponse(updatedEvent);
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        // Check if the current user is the creator of the event
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        if (!event.getEventCreator().getEmail().equals(currentUserEmail) &&
                !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            throw new RuntimeException("You are not authorized to delete this event");
        }

        eventRepository.delete(event);
    }

    @Transactional
    public EventResponse joinEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Participant participant = participantRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        // Check if the event is full
        if (event.getReservationList().size() >= event.getMaxParticipants()) {
            throw new RuntimeException("Event is already full");
        }

        // Check if the participant is already joined
        boolean alreadyJoined = event.getReservationList().stream()
                .anyMatch(r -> r.getParticipant().getId().equals(participant.getId()));

        if (alreadyJoined) {
            throw new RuntimeException("You have already joined this event");
        }

        // Create reservation
        Reservation reservation = new Reservation();
        EventParticipantIds ids = new EventParticipantIds();
        ids.setEventId(event.getId());
        ids.setParticipantId(participant.getId());

        reservation.setEventParticipantIds(ids);
        reservation.setEvent(event);
        reservation.setParticipant(participant);
        reservation.setDate(LocalDateTime.now());
        reservation.setStartTime(event.getDate());
        reservation.setEndTime(event.getDate().plusHours(2)); // Assuming 2 hours duration

        event.getReservationList().add(reservation);
        participant.getReservationList().add(reservation);

        Event updatedEvent = eventRepository.save(event);
        return eventMapper.eventToEventResponse(updatedEvent);
    }

    @Transactional
    public EventResponse leaveEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Participant participant = participantRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        // Check if the participant has joined the event
        boolean hasJoined = event.getReservationList().stream()
                .anyMatch(r -> r.getParticipant().getId().equals(participant.getId()));

        if (!hasJoined) {
            throw new RuntimeException("You have not joined this event");
        }

        // Remove reservation
        event.getReservationList().removeIf(r -> r.getParticipant().getId().equals(participant.getId()));
        participant.getReservationList().removeIf(r -> r.getEvent().getId().equals(event.getId()));

        Event updatedEvent = eventRepository.save(event);
        return eventMapper.eventToEventResponse(updatedEvent);
    }
}
