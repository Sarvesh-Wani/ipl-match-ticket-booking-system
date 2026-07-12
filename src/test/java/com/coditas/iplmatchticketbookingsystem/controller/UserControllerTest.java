package com.coditas.iplmatchticketbookingsystem.controller;

import com.coditas.iplmatchticketbookingsystem.dto.stadium.StadiumRequest;
import com.coditas.iplmatchticketbookingsystem.dto.stadium.StadiumResponse;
import com.coditas.iplmatchticketbookingsystem.dto.user.LoginRequest;
import com.coditas.iplmatchticketbookingsystem.dto.user.LoginResponse;
import com.coditas.iplmatchticketbookingsystem.dto.user.RegisterRequest;
import com.coditas.iplmatchticketbookingsystem.dto.user.RegisterResponse;
import com.coditas.iplmatchticketbookingsystem.service.UserService;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    @Test
    public void registerUserTest(){
        RegisterRequest request = new RegisterRequest("Yash", "yash123", "ya@gmail.com", "y@123");

        RegisterResponse mockResponse =
                new RegisterResponse(1, "Yash", "yash123","ya@gmail.com", "ya@gmail.com");

        Mockito.when(userService.registerUser(request)).thenReturn(mockResponse);
        ResponseEntity<RegisterResponse> response = userController.registerUser(request);

        Assert.notNull(response);
        Assertions.assertEquals("yash123", response.getBody().getUsername());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Mockito.verify(userService, Mockito.times(1)).registerUser(request);

    }

    @Test
    public void registerAdminTest(){
        RegisterRequest request = new RegisterRequest("Yash", "yash123", "ya@gmail.com", "y@123");

        RegisterResponse mockResponse =
                new RegisterResponse(1, "Yash", "yash123","ya@gmail.com", "ya@gmail.com");

        Mockito.when(userService.registerAdmin(request)).thenReturn(mockResponse);
        ResponseEntity<RegisterResponse> response = userController.registerAdmin(request);

        Assert.notNull(response);
        Assertions.assertEquals("yash123", response.getBody().getUsername());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Mockito.verify(userService, Mockito.times(1)).registerAdmin(request);

    }

    @Test
    public void login(){
        LoginRequest request = new LoginRequest("yash123", "y@123");

        LoginResponse mockResponse =
                new LoginResponse("rtyuifghbj.sdyuidfghj.etrijsxdhjusfry",
                        "rtyuifghbj.sdyuidfghj.etrijsxdhjusfry");

        Mockito.when(userService.login(request)).thenReturn(mockResponse);
        ResponseEntity<LoginResponse> response = userController.login(request);

        Assert.notNull(response);
        Assertions.assertEquals("rtyuifghbj.sdyuidfghj.etrijsxdhjusfry",
                response.getBody().getAccessToken());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(userService, Mockito.times(1)).login(request);

    }
}
