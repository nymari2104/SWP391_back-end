package com.example.demo.mapper;

import com.example.demo.dto.request.authenticationRequest.SignUpRequest;
import com.example.demo.dto.response.authenticationResponse.SignUpResponse;
import com.example.demo.entity.User;
import com.example.demo.entity.VerificationToken;
import org.mapstruct.Mapper;

@Mapper
public interface VerificationMapper {
    VerificationToken  toVerificationToken(SignUpRequest request);

    User toUser(VerificationToken verificationToken);

    SignUpResponse toSignUpResponse(VerificationToken verificationToken);
}
