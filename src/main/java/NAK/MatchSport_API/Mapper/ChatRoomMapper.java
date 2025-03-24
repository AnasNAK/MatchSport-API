package NAK.MatchSport_API.Mapper;

import NAK.MatchSport_API.Dto.request.ChatRoomRequest;
import NAK.MatchSport_API.Dto.response.ChatRoomResponse;
import NAK.MatchSport_API.Entity.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MessageMapper.class})
public interface ChatRoomMapper {
    ChatRoomMapper INSTANCE = Mappers.getMapper(ChatRoomMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "messageList", ignore = true)
    @Mapping(target = "event", ignore = true)
    ChatRoom chatRoomRequestToChatRoom(ChatRoomRequest chatRoomRequest);

    @Mapping(target = "messages", source = "messageList")
    ChatRoomResponse chatRoomToChatRoomResponse(ChatRoom chatRoom);

    List<ChatRoomResponse> chatRoomsToChatRoomResponses(List<ChatRoom> chatRooms);

    void updateChatRoomFromChatRoomRequest(ChatRoomRequest chatRoomRequest, @MappingTarget ChatRoom chatRoom);
}
