package NAK.MatchSport_API.Mapper;

import NAK.MatchSport_API.Dto.request.VenueRequest;
import NAK.MatchSport_API.Dto.response.VenueResponse;
import NAK.MatchSport_API.Entity.Venue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MediaMapper.class})
public interface VenueMapper {
    VenueMapper INSTANCE = Mappers.getMapper(VenueMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "venueImagesList", ignore = true)
    Venue venueRequestToVenue(VenueRequest venueRequest);

    @Mapping(target = "images", source = "venueImagesList")
    VenueResponse venueToVenueResponse(Venue venue);

    List<VenueResponse> venuesToVenueResponses(List<Venue> venues);

    void updateVenueFromVenueRequest(VenueRequest venueRequest, @MappingTarget Venue venue);
}