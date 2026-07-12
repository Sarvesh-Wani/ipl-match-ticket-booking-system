package com.coditas.iplmatchticketbookingsystem.controller;

import com.coditas.iplmatchticketbookingsystem.dto.match.MatchRequest;
import com.coditas.iplmatchticketbookingsystem.dto.match.MatchResponse;
import com.coditas.iplmatchticketbookingsystem.dto.ticketbooking.TicketBookingResponse;
import com.coditas.iplmatchticketbookingsystem.entity.Match;
import com.coditas.iplmatchticketbookingsystem.service.MatchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MatchControllerTest {

    @InjectMocks
    MatchController matchController;

    @Mock
    MatchService matchService;

    @Test
    public void createMatchTest() {

        MatchRequest request = new MatchRequest();
        request.setTicketPrice(2000);

        MatchResponse mockResponse = new MatchResponse();
        mockResponse.setTicketPrice(2000);

        Mockito.when(matchService.createMatch(request)).thenReturn(mockResponse);
        ResponseEntity<MatchResponse> response = matchController.createMatch(request);

        assert response.getBody() != null;
        Assertions.assertEquals(2000, response.getBody().getTicketPrice());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    @Test
    public void updateMatchTest() {

        MatchRequest request = new MatchRequest();
        request.setTicketPrice(2000);

        MatchResponse mockResponse = new MatchResponse();
        mockResponse.setId(1);
        mockResponse.setTicketPrice(2000);

        int mid = 1;
        Mockito.when(matchService.updateMatch(mid, request)).thenReturn(mockResponse);
        ResponseEntity<MatchResponse> response = matchController.updateMatch(mid, request);

        assert response.getBody() != null;
        Assertions.assertEquals(1, response.getBody().getId());
        Assertions.assertEquals(2000, response.getBody().getTicketPrice());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getMatchByIdTest() {

        int mid = 1;

        MatchResponse mockResponse = new MatchResponse();
        mockResponse.setId(1);
        mockResponse.setTicketPrice(2000);

        Mockito.when(matchService.getMatchById(mid)).thenReturn(mockResponse);
        ResponseEntity<MatchResponse> response = matchController.getMatch(mid);

        assert response.getBody() != null;
        Assertions.assertEquals(1, response.getBody().getId());
        Assertions.assertEquals(2000, response.getBody().getTicketPrice());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deleteMatchTest() {

        int mid = 1;
        String mockResponse = "deleted successfully";

        Mockito.when(matchService.deleteMatch(mid)).thenReturn(mockResponse);
        ResponseEntity<String> response = matchController.deleteMatch(mid);

        assert response.getBody() != null;
        Assertions.assertEquals("deleted successfully", response.getBody());
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void getAllMatchesTest() {

        List<Match> mockResponse = List.of(new Match("MI", 2000),
                new Match("CSK", 2000));

        Mockito.when(matchService.getAllMatches()).thenReturn(mockResponse);
        ResponseEntity<List<Match>> response = matchController.getAllMatches();

        assert response.getBody() != null;
        Assertions.assertEquals("MI", response.getBody().getFirst().getTeamA());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void ticketBookingTest() {

        int uid = 1;
        int mid = 2;
        int seats = 5;

        TicketBookingResponse mockResponse = new TicketBookingResponse();
        mockResponse.setName("hello");
        mockResponse.setNumberOfSeats(5);

        Mockito.when(matchService.ticketBooking(uid, mid, seats)).thenReturn(mockResponse);
        ResponseEntity<TicketBookingResponse> response = matchController.ticketBooking(uid, mid, seats);

        assert response.getBody() != null;
        Assertions.assertEquals(5, response.getBody().getNumberOfSeats());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

}
