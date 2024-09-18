package com.example.demo.mapper;

import com.example.demo.dto.request.SignUpRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
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
