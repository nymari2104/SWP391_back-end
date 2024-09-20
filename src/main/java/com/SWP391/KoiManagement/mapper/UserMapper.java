package com.SWP391.KoiManagement.mapper;

import com.SWP391.KoiManagement.dto.request.SignUpRequest;
import com.SWP391.KoiManagement.dto.request.UserUpdateRequest;
import com.SWP391.KoiManagement.dto.response.UserResponse;
import com.SWP391.KoiManagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface UserMapper {
    User toUser(SignUpRequest request);

//    @Mapping(source = "lastname", target = "username")  //map field lastname to field username
//    @Mapping(target = "lastname", ignore = true)  //ignore field lastname
    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);


}
