package NAK.MatchSport_API.Mapper;

import NAK.MatchSport_API.Dto.request.MessageRequest;
import NAK.MatchSport_API.Dto.response.MessageResponse;
import NAK.MatchSport_API.Entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ParticipantMapper.class})
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "messageStatus", ignore = true)
    @Mapping(target = "edited", ignore = true)
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "chatRoom", ignore = true)
    Message messageRequestToMessage(MessageRequest messageRequest);

    MessageResponse messageToMessageResponse(Message message);

    List<MessageResponse> messagesToMessageResponses(List<Message> messages);

    void updateMessageFromMessageRequest(MessageRequest messageRequest, @MappingTarget Message message);
}
