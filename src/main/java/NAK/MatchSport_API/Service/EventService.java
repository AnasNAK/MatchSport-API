package NAK.MatchSport_API.Service;

import NAK.MatchSport_API.Dto.request.EventRequest;
import NAK.MatchSport_API.Dto.response.EventResponse;
import NAK.MatchSport_API.Entity.*;
import NAK.MatchSport_API.Entity.Embedded.EventParticipantIds;
import NAK.MatchSport_API.Exception.BadRequestException;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final VenueRepository venueRepository;
    private final MediaRepository mediaRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ReservationRepository reservationRepository;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Participant creator = participantRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        Venue venue = venueRepository.findById(eventRequest.getVenueId())
                .orElseThrow(() -> new RuntimeException("Venue not found with id: " + eventRequest.getVenueId()));

        Event event = eventMapper.eventRequestToEvent(eventRequest);
        event.setEventCreator(creator);
        event.setVenue(venue);

        if (eventRequest.getEventImageId() != null) {
            Media eventImage = mediaRepository.findById(eventRequest.getEventImageId())
                    .orElseThrow(() -> new RuntimeException("Media not found with id: " + eventRequest.getEventImageId()));
            event.setEventImage(eventImage);
        }

        Event savedEvent = eventRepository.save(event);

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(event.getName() + " Chat");
        chatRoom.setEvent(savedEvent);
        chatRoom = chatRoomRepository.save(chatRoom);

        savedEvent.setChatRoom(chatRoom);
        eventRepository.save(savedEvent);

        if (!reservationRepository.existsByEventIdAndParticipantId(savedEvent.getId(), creator.getId())) {
            Reservation reservation = new Reservation();
            reservation.setEventParticipantIds(new EventParticipantIds(savedEvent.getId(), creator.getId()));
            reservation.setEvent(savedEvent);
            reservation.setParticipant(creator);
            reservation.setDate(LocalDateTime.now());
            reservation.setStartTime(savedEvent.getDate());
            reservation.setEndTime(savedEvent.getDate().plusHours(2));

            reservationRepository.save(reservation);
        }

        return eventMapper.eventToEventResponse(savedEvent);
    }


    @Transactional
    public EventResponse updateEvent(Long id, EventRequest eventRequest) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        if (!event.getEventCreator().getEmail().equals(currentUserEmail) &&
                authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            throw new RuntimeException("You are not authorized to update this event");
        }

        if (!event.getVenue().getId().equals(eventRequest.getVenueId())) {
            Venue venue = venueRepository.findById(eventRequest.getVenueId())
                    .orElseThrow(() -> new RuntimeException("Venue not found with id: " + eventRequest.getVenueId()));
            event.setVenue(venue);
        }

        if (eventRequest.getEventImageId() != null &&
                (event.getEventImage() == null || !event.getEventImage().getId().equals(eventRequest.getEventImageId()))) {
            Media eventImage = mediaRepository.findById(eventRequest.getEventImageId())
                    .orElseThrow(() -> new RuntimeException("Media not found with id: " + eventRequest.getEventImageId()));
            event.setEventImage(eventImage);
        }

        event.setName(eventRequest.getName());
        event.setDate(eventRequest.getDate());
        event.setEventType(eventRequest.getEventType());
        event.setMaxParticipants(eventRequest.getMaxParticipants());

        if (event.getChatRoom() != null && !event.getChatRoom().getName().equals(eventRequest.getName() + " Chat")) {
            event.getChatRoom().setName(eventRequest.getName() + " Chat");
        }

        return eventMapper.eventToEventResponse(eventRepository.save(event));
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        if (!event.getEventCreator().getEmail().equals(currentUserEmail) &&
                authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            throw new RuntimeException("You are not authorized to delete this event");
        }

        eventRepository.delete(event);
    }

    @Transactional
    public EventResponse joinEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Participant participant = participantRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        if (event.getReservationList().size() >= event.getMaxParticipants()) {
            throw new BadRequestException("Event is full.");
        }

        if (reservationRepository.existsByEventIdAndParticipantId(event.getId(), participant.getId())) {
            throw new BadRequestException("You have already joined this event");
        }

        Reservation reservation = new Reservation(new EventParticipantIds(event.getId(), participant.getId()), LocalDateTime.now(), event.getDate(), event.getDate().plusHours(2), event, participant);
        reservationRepository.save(reservation);

        return eventMapper.eventToEventResponse(event);
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
        Optional<Reservation> reservationOpt = event.getReservationList().stream()
                .filter(r -> r.getParticipant().getId().equals(participant.getId()))
                .findFirst();

        if (reservationOpt.isEmpty()) {
            throw new BadRequestException("You have not joined this event");
        }

        // Check if the user is the event creator
        if (event.getEventCreator().getId().equals(participant.getId())) {
            throw new BadRequestException("Event creator cannot leave the event. Delete the event instead.");
        }

        // Remove reservation
        Reservation reservation = reservationOpt.get();
        event.getReservationList().remove(reservation);
        participant.getReservationList().remove(reservation);

        reservationRepository.delete(reservation);
        Event updatedEvent = eventRepository.save(event);
        return eventMapper.eventToEventResponse(updatedEvent);
    }

    public boolean isParticipantInEvent(Long eventId, Long participantId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        return event.getReservationList().stream()
                .anyMatch(r -> r.getParticipant().getId().equals(participantId));
    }
}



