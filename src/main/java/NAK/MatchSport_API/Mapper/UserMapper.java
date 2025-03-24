package NAK.MatchSport_API.Mapper;

import NAK.MatchSport_API.Dto.request.RegisterRequest;
import NAK.MatchSport_API.Dto.request.UserRequest;
import NAK.MatchSport_API.Dto.response.UserResponse;
import NAK.MatchSport_API.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    User userRequestToUser(UserRequest userRequest);

    @Mapping(target = "id", ignore = true)
    User registerRequestToUser(RegisterRequest registerRequest);

    UserResponse userToUserResponse(User user);

    void updateUserFromUserRequest(UserRequest userRequest, @MappingTarget User user);
}
