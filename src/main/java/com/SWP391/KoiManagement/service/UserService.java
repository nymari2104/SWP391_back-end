package com.SWP391.KoiManagement.service;

import com.SWP391.KoiManagement.dto.request.SignUpRequest;
import com.SWP391.KoiManagement.dto.request.UserUpdateRequest;
import com.SWP391.KoiManagement.dto.response.UserResponse;
import com.SWP391.KoiManagement.entity.User;
import com.SWP391.KoiManagement.enums.Role;
import com.SWP391.KoiManagement.exception.AppException;
import com.SWP391.KoiManagement.exception.ErrorCode;
import com.SWP391.KoiManagement.mapper.UserMapper;
import com.SWP391.KoiManagement.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(SignUpRequest request){


        //Map data from UserCreationRequest to User
        User user = userMapper.toUser(request);
        //Encoder password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        //Set role default
        user.setRole(Role.USER.toString());
        // Check username
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXISTED);

        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }


    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();//when user login success, user info will be store in SecurityContextHolder
        String email = context.getAuthentication().getName();//get email of user

        User user = userRepository.findByEmail(email)//get User object by email
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));//user not existed

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateMyInfo(UserUpdateRequest request){
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        log.info(email);
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));
        userMapper.updateUser(user, request);
        //set password after encode
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers(){
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @PostAuthorize("returnObject.username == authentication.name")//check if username is match with who is login
    public UserResponse getUser(String Id){
        return userMapper.toUserResponse(userRepository.findById(Id).orElseThrow(() -> new AppException(ErrorCode.USER_ID_NOT_EXISTED)));
    }

}
