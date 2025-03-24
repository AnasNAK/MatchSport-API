package NAK.MatchSport_API.Mapper;

import NAK.MatchSport_API.Dto.request.UserRequest;
import NAK.MatchSport_API.Dto.response.AdminResponse;
import NAK.MatchSport_API.Entity.Admin;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);

    @Mapping(target = "id", ignore = true)
    Admin userRequestToAdmin(UserRequest userRequest);

    AdminResponse adminToAdminResponse(Admin admin);

    void updateAdminFromUserRequest(UserRequest userRequest, @MappingTarget Admin admin);
}
