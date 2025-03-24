package NAK.MatchSport_API.Mapper;

import NAK.MatchSport_API.Dto.request.EventRequest;
import NAK.MatchSport_API.Dto.response.EventResponse;
import NAK.MatchSport_API.Entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {VenueMapper.class, MediaMapper.class, ParticipantMapper.class, ChatRoomMapper.class, ReservationMapper.class})
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eventCreator", ignore = true)
    @Mapping(target = "reservationList", ignore = true)
    @Mapping(target = "chatRoom", ignore = true)
    @Mapping(target = "venue", ignore = true)
    @Mapping(target = "eventImage", ignore = true)
    Event eventRequestToEvent(EventRequest eventRequest);

    @Mapping(target = "reservations", source = "reservationList")
    EventResponse eventToEventResponse(Event event);

    List<EventResponse> eventsToEventResponses(List<Event> events);

    void updateEventFromEventRequest(EventRequest eventRequest, @MappingTarget Event event);
}

