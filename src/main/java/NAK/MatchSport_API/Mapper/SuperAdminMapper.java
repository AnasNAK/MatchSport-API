package NAK.MatchSport_API.Mapper;

import NAK.MatchSport_API.Dto.request.UserRequest;
import NAK.MatchSport_API.Dto.response.SuperAdminResponse;
import NAK.MatchSport_API.Entity.SuperAdmin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SuperAdminMapper {
    SuperAdminMapper INSTANCE = Mappers.getMapper(SuperAdminMapper.class);

    @Mapping(target = "id", ignore = true)
    SuperAdmin userRequestToSuperAdmin(UserRequest userRequest);

    SuperAdminResponse superAdminToSuperAdminResponse(SuperAdmin superAdmin);

    void updateSuperAdminFromUserRequest(UserRequest userRequest, @MappingTarget SuperAdmin superAdmin);
}
