package NAK.MatchSport_API.Mapper;

import NAK.MatchSport_API.Dto.request.MediaRequest;
import NAK.MatchSport_API.Dto.response.MediaResponse;
import NAK.MatchSport_API.Entity.Media;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MediaMapper {
    MediaMapper INSTANCE = Mappers.getMapper(MediaMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userImage", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "venue", ignore = true)
    Media mediaRequestToMedia(MediaRequest mediaRequest);

    MediaResponse mediaToMediaResponse(Media media);

    List<MediaResponse> mediasToMediaResponses(List<Media> medias);

    void updateMediaFromMediaRequest(MediaRequest mediaRequest, @MappingTarget Media media);
}
