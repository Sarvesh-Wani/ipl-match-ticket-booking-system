package com.coditas.iplmatchticketbookingsystem.service;

import com.coditas.iplmatchticketbookingsystem.dto.stadium.StadiumRequest;
import com.coditas.iplmatchticketbookingsystem.dto.stadium.StadiumResponse;
import com.coditas.iplmatchticketbookingsystem.entity.Stadium;
import com.coditas.iplmatchticketbookingsystem.repository.StadiumRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class StadiumServiceTest {

    @InjectMocks
    StadiumService stadiumService;

    @Mock
    StadiumRepository stadiumRepository;

    @Test
    public void createStadiumTest(){
        StadiumRequest request = new StadiumRequest(
                "chepox","chennai", 1000
        );

        Stadium savedStadium = new Stadium(1, "chipox", null, "chennai", 1000);

        StadiumResponse response = new StadiumResponse(
                1, "chepox","chennai", 1000
        );

        Mockito.when(stadiumRepository.save(Mockito.any())).thenReturn(savedStadium);
        StadiumResponse result = stadiumService.createStadium(request);

        Assert.notNull(response);
        Assertions.assertEquals("chepox", response.getName());
        Mockito.verify(stadiumRepository, Mockito.times(1)).save(Mockito.any());
    }
}
