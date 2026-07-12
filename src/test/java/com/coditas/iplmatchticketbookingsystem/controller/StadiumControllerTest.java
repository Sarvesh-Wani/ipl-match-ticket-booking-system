package com.coditas.iplmatchticketbookingsystem.controller;


import com.coditas.iplmatchticketbookingsystem.dto.stadium.StadiumRequest;
import com.coditas.iplmatchticketbookingsystem.dto.stadium.StadiumResponse;
import com.coditas.iplmatchticketbookingsystem.service.StadiumService;
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
public class StadiumControllerTest {

    @InjectMocks
    StadiumController stadiumController;

    @Mock
    StadiumService stadiumService;

    @Test
    public void createStadiumTest() {

        StadiumRequest request = new StadiumRequest();
        request.setName("wankhede");
        request.setLocation("Mumbai");
        request.setCapacity(1000);

        StadiumResponse mockResponse =
                new StadiumResponse(1, "wankhede", "Mumbai", 1000);

        Mockito.when(stadiumService.createStadium(request)).thenReturn(mockResponse);
        ResponseEntity<StadiumResponse> response = stadiumController.createStadium(request);

        Assert.notNull(response);
        Assertions.assertEquals(1000, response.getBody().getCapacity());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Mockito.verify(stadiumService, Mockito.times(1)).createStadium(request);

    }
}
