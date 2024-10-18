package com.example.demo.controller;

import com.example.demo.dto.request.authenticationRequest.SignInRequest;
import com.example.demo.dto.response.authenticationResponse.SignInResponse;
import com.example.demo.dto.response.userResponse.UserResponse;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    private SignInRequest signInRequest;
    private SignInResponse signInResponse;

    @BeforeEach
    void initData(){
        signInRequest = SignInRequest.builder()
                .email("manhcat24@gmail.com")
                .password("12345678")
                .build();

        signInResponse = SignInResponse.builder()
                .user(UserResponse.builder()
                        .userId("4e89d266-ef31-4c65-9a55-2245d6abb1cd")
                        .email("manhcat24@gmail.com")
                        .phone("12345678")
                        .fullname("manh cat")
                        .role("USER")
                        .googleAccount(false)
                        .build())
                .build();
    }

    @Test
    void signIn_success() throws Exception {
        //GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(signInRequest);

        Mockito.when(authenticationService.authenticate((SignInRequest) ArgumentMatchers.any()))
                .thenReturn(signInResponse);

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("result.user.userId")
                        .value("4e89d266-ef31-4c65-9a55-2245d6abb1cd"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.user.email")
                        .value("manhcat24@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.user.phone")
                        .value("12345678"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.user.fullname")
                        .value("manh cat"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.user.role")
                        .value("USER"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.user.googleAccount")
                        .value(false)
        );
    }

    @Test
    void signIn_fail() throws Exception {
        //GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(signInRequest);

        Mockito.when(authenticationService.authenticate((SignInRequest) ArgumentMatchers.any()))
                .thenThrow(new AppException(ErrorCode.LOGIN_FAIL));

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Email or password incorrect")
                );
    }
}