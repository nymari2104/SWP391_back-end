package com.example.demo.mapper;

import com.example.demo.dto.request.authenticationRequest.SignUpRequest;
import com.example.demo.dto.request.userRequest.UserUpdateRequest;
import com.example.demo.dto.response.userResponse.UserResponse;
import com.example.demo.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface UserMapper {
    User toUser(SignUpRequest request);
//    @Mapping(source = "lastname", target = "username")  //map field lastname to field username
//    @Mapping(target = "lastname", ignore = true)  //ignore field lastname
    UserResponse toUserResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);


}
