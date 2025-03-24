package NAK.MatchSport_API.Mapper;

import NAK.MatchSport_API.Dto.request.RatingRequest;
import NAK.MatchSport_API.Dto.response.RatingResponse;
import NAK.MatchSport_API.Entity.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ParticipantMapper.class})
public interface RatingMapper {
    RatingMapper INSTANCE = Mappers.getMapper(RatingMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rater", ignore = true)
    @Mapping(target = "rated", ignore = true)
    Rating ratingRequestToRating(RatingRequest ratingRequest);

    RatingResponse ratingToRatingResponse(Rating rating);

    List<RatingResponse> ratingsToRatingResponses(List<Rating> ratings);

    void updateRatingFromRatingRequest(RatingRequest ratingRequest, @MappingTarget Rating rating);
}
