package NAK.MatchSport_API.Mapper;

import NAK.MatchSport_API.Dto.request.ReservationRequest;
import NAK.MatchSport_API.Dto.response.ReservationResponse;
import NAK.MatchSport_API.Entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ParticipantMapper.class})
public interface ReservationMapper {
    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    @Mapping(target = "eventParticipantIds", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "participant", ignore = true)
    Reservation reservationRequestToReservation(ReservationRequest reservationRequest);

    @Mapping(target = "eventId", source = "eventParticipantIds.eventId")
    @Mapping(target = "participantId", source = "eventParticipantIds.participantId")
    ReservationResponse reservationToReservationResponse(Reservation reservation);

    List<ReservationResponse> reservationsToReservationResponses(List<Reservation> reservations);
}
