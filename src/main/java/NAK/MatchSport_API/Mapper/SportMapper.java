package NAK.MatchSport_API.Mapper;

import NAK.MatchSport_API.Dto.request.SportRequest;
import NAK.MatchSport_API.Dto.response.SportResponse;
import NAK.MatchSport_API.Entity.Sport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SportMapper {
    SportMapper INSTANCE = Mappers.getMapper(SportMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "participantSports", ignore = true)
    Sport sportRequestToSport(SportRequest sportRequest);

    SportResponse sportToSportResponse(Sport sport);

    List<SportResponse> sportsToSportResponses(List<Sport> sports);

    void updateSportFromSportRequest(SportRequest sportRequest, @MappingTarget Sport sport);
}

