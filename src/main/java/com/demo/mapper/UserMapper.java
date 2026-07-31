package com.demo.mapper;

import org.mapstruct.Mapper;
import com.demo.dto.RoleDto;
import com.demo.dto.UserDto;
import com.demo.model.Role;
import com.demo.model.User;

import java.util.Collection;

/**
 * Map User/Role & UserDto/RoleDto using mapstruct
 */
@Mapper(componentModel = "jakarta-cdi")
public interface UserMapper {
    Role toRole(RoleDto roleDto);

    RoleDto toRoleDto(Role role);

    Collection<RoleDto> toRoleDtos(Collection<Role> roles);

    User toUser(UserDto userDto);

    UserDto toUserDto(User user);

    Collection<Role> toRoles(Collection<RoleDto> roleDtos);

}
