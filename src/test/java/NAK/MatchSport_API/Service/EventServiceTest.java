//package NAK.MatchSport_API.Service;
//
//import NAK.MatchSport_API.Entity.*;
//import NAK.MatchSport_API.Entity.Embedded.EventParticipantIds;
//import NAK.MatchSport_API.Enum.EventType;
//import NAK.MatchSport_API.Exception.BadRequestException;
//import NAK.MatchSport_API.Mapper.EventMapper;
//import NAK.MatchSport_API.Repository.EventRepository;
//import NAK.MatchSport_API.Repository.ParticipantRepository;
//import NAK.MatchSport_API.Repository.ReservationRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class EventServiceTest {
//
//    @Mock
//    private EventRepository eventRepository;
//
//    @Mock
//    private ParticipantRepository participantRepository;
//
//    @Mock
//    private ReservationRepository reservationRepository;
//
//    @Mock
//    private EventMapper eventMapper;
//
//    @Mock
//    private SecurityContext securityContext;
//
//    @Mock
//    private Authentication authentication;
//
//    @InjectMocks
//    private EventService eventService;
//
//    private Event event;
//    private Participant participant;
//
//    @BeforeEach
//    void setUp() {
//        // Setup security context mock
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//        when(authentication.getName()).thenReturn("test@example.com");
//
//        // Setup test data
//        participant = new Participant();
//        participant.setId(1L);
//        participant.setEmail("test@example.com");
//        participant.setFullName("Test User");
//        participant.setReservationList(new ArrayList<>());
//
//        event = new Event();
//        event.setId(1L);
//        event.setName("Test Event");
//        event.setDate(LocalDateTime.now().plusDays(1));
//        event.setEventType(EventType.TRAINING);
//        event.setMaxParticipants(5);
//        event.setReservationList(new ArrayList<>());
//
//        Participant eventCreator = new Participant();
//        eventCreator.setId(2L);
//        eventCreator.setEmail("creator@example.com");
//        event.setEventCreator(eventCreator);
//    }
//
//    @Test
//    void joinEvent_Success() {
//        // Arrange
//        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
//        when(participantRepository.findByEmail("test@example.com")).thenReturn(Optional.of(participant));
//        when(eventRepository.save(any(Event.class))).thenReturn(event);
//
//        // Act
//        eventService.joinEvent(1L);
//
//        // Assert
//        verify(reservationRepository, times(1)).save(any(Reservation.class));
//        assertEquals(1, event.getReservationList().size());
//        assertEquals(1, participant.getReservationList().size());
//    }
//
//    @Test
//    void joinEvent_EventIsFull() {
//        // Arrange
//        event.setMaxParticipants(1);
//
//        // Add an existing reservation
//        Reservation existingReservation = new Reservation();
//        EventParticipantIds ids = new EventParticipantIds();
//        ids.setEventId(1L);
//        ids.setParticipantId(3L);
//        existingReservation.setEventParticipantIds(ids);
//
//        Participant otherParticipant = new Participant();
//        otherParticipant.setId(3L);
//        existingReservation.setParticipant(otherParticipant);
//
//        event.getReservationList().add(existingReservation);
//
//        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
//        when(participantRepository.findByEmail("test@example.com")).thenReturn(Optional.of(participant));
//
//        // Act & Assert
//        assertThrows(BadRequestException.class, () -> eventService.joinEvent(1L));
//        verify(reservationRepository, never()).save(any(Reservation.class));
//    }
//
//    @Test
//    void joinEvent_AlreadyJoined() {
//        // Arrange
//        // Create a reservation for the current participant
//        Reservation existingReservation = new Reservation();
//        EventParticipantIds ids = new EventParticipantIds();
//        ids.setEventId(1L);
//        ids.setParticipantId(1L);
//        existingReservation.setEventParticipantIds(ids);
//        existingReservation.setParticipant(participant);
//
//        event.getReservationList().add(existingReservation);
//
//        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
//        when(participantRepository.findByEmail("test@example.com")).thenReturn(Optional.of(participant));
//
//        // Act & Assert
//        assertThrows(BadRequestException.class, () -> eventService.joinEvent(1L));
//        verify(reservationRepository, never()).save(any(Reservation.class));
//    }
//
//    @Test
//    void leaveEvent_Success() {
//        // Arrange
//        // Create a reservation for the current participant
//        Reservation existingReservation = new Reservation();
//        EventParticipantIds ids = new EventParticipantIds();
//        ids.setEventId(1L);
//        ids.setParticipantId(1L);
//        existingReservation.setEventParticipantIds(ids);
//        existingReservation.setParticipant(participant);
//
//        event.getReservationList().add(existingReservation);
//        participant.getReservationList().add(existingReservation);
//
//        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
//        when(participantRepository.findByEmail("test@example.com")).thenReturn(Optional.of(participant));
//        when(eventRepository.save(any(Event.class))).thenReturn(event);
//
//        // Act
//        eventService.leaveEvent(1L);
//
//        // Assert
//        verify(reservationRepository, times(1)).delete(any(Reservation.class));
//        assertEquals(0, event.getReservationList().size());
//        assertEquals(0, participant.getReservationList().size());
//    }
//}
//
