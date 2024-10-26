package com.example.demo.mapper;

import com.example.demo.dto.request.authenticationRequest.SignUpRequest;
import com.example.demo.entity.User;
import com.example.demo.entity.VerificationToken;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface VerificationTokenMapper {
    User toUser(VerificationToken verificationToken);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    VerificationToken updateVerificationToken(@MappingTarget VerificationToken verificationToken, SignUpRequest request);
}
