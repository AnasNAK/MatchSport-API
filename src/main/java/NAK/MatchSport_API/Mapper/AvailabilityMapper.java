package NAK.MatchSport_API.Mapper;

import NAK.MatchSport_API.Dto.request.AvailabilityRequest;
import NAK.MatchSport_API.Dto.response.AvailabilityResponse;
import NAK.MatchSport_API.Entity.Availability;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ParticipantMapper.class})
public interface AvailabilityMapper {
    AvailabilityMapper INSTANCE = Mappers.getMapper(AvailabilityMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "availabilityParticipantList", ignore = true)
    Availability availabilityRequestToAvailability(AvailabilityRequest availabilityRequest);

    @Mapping(target = "participants", ignore = true) // Custom mapping needed
    AvailabilityResponse availabilityToAvailabilityResponse(Availability availability);

    List<AvailabilityResponse> availabilitiesToAvailabilityResponses(List<Availability> availabilities);

    void updateAvailabilityFromAvailabilityRequest(AvailabilityRequest availabilityRequest, @MappingTarget Availability availability);
}
