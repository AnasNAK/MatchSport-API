package NAK.MatchSport_API.Mapper;

import NAK.MatchSport_API.Dto.request.RegisterRequest;
import NAK.MatchSport_API.Dto.request.UserRequest;
import NAK.MatchSport_API.Dto.response.ParticipantResponse;
import NAK.MatchSport_API.Entity.Participant;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MediaMapper.class, ParticipantSportMapper.class})
public interface ParticipantMapper {
    ParticipantMapper INSTANCE = Mappers.getMapper(ParticipantMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "participantSports", ignore = true)
    @Mapping(target = "availabilityParticipantList", ignore = true)
    @Mapping(target = "eventList", ignore = true)
    @Mapping(target = "reservationList", ignore = true)
    @Mapping(target = "sendermessageList", ignore = true)
    @Mapping(target = "profileImage", ignore = true)
    @Mapping(target = "raterList", ignore = true)
    @Mapping(target = "ratedList", ignore = true)
    Participant userRequestToParticipant(UserRequest userRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "participantSports", ignore = true)
    @Mapping(target = "availabilityParticipantList", ignore = true)
    @Mapping(target = "eventList", ignore = true)
    @Mapping(target = "reservationList", ignore = true)
    @Mapping(target = "sendermessageList", ignore = true)
    @Mapping(target = "profileImage", ignore = true)
    @Mapping(target = "raterList", ignore = true)
    @Mapping(target = "ratedList", ignore = true)
    Participant registerRequestToParticipant(RegisterRequest registerRequest);

    @Mapping(target = "sports", source = "participantSports")
    ParticipantResponse participantToParticipantResponse(Participant participant);

    List<ParticipantResponse> participantsToParticipantResponses(List<Participant> participants);

    void updateParticipantFromUserRequest(UserRequest userRequest, @MappingTarget Participant participant);
}


