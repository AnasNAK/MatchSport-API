package NAK.MatchSport_API.Mapper;

import NAK.MatchSport_API.Dto.response.SportWithLevelResponse;
import NAK.MatchSport_API.Entity.ParticipantSport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SportMapper.class})
public interface ParticipantSportMapper {
    ParticipantSportMapper INSTANCE = Mappers.getMapper(ParticipantSportMapper.class);

    @Mapping(target = "id", source = "sport.id")
    @Mapping(target = "name", source = "sport.name")
    @Mapping(target = "description", source = "sport.description")
    SportWithLevelResponse participantSportToSportWithLevelResponse(ParticipantSport participantSport);

    List<SportWithLevelResponse> participantSportsToSportWithLevelResponses(List<ParticipantSport> participantSports);
}

