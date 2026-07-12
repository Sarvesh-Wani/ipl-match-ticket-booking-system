package com.coditas.iplmatchticketbookingsystem.service;

import com.coditas.iplmatchticketbookingsystem.dto.match.MatchRequest;
import com.coditas.iplmatchticketbookingsystem.dto.match.MatchResponse;
import com.coditas.iplmatchticketbookingsystem.dto.ticketbooking.TicketBookingResponse;
import com.coditas.iplmatchticketbookingsystem.entity.Match;
import com.coditas.iplmatchticketbookingsystem.entity.Stadium;
import com.coditas.iplmatchticketbookingsystem.entity.TicketBookings;
import com.coditas.iplmatchticketbookingsystem.entity.User;
import com.coditas.iplmatchticketbookingsystem.exception.ExceptionMessage;
import com.coditas.iplmatchticketbookingsystem.exception.MatchNotFoundException;
import com.coditas.iplmatchticketbookingsystem.exception.StadiumFullException;
import com.coditas.iplmatchticketbookingsystem.exception.StadiumNotFoundException;
import com.coditas.iplmatchticketbookingsystem.repository.MatchRepository;
import com.coditas.iplmatchticketbookingsystem.repository.StadiumRepository;
import com.coditas.iplmatchticketbookingsystem.repository.TicketBookingRepository;
import com.coditas.iplmatchticketbookingsystem.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchService {

    private MatchRepository matchRepository;
    private StadiumRepository stadiumRepository;
    private UserRepository userRepository;
    private TicketBookingRepository ticketBookingRepository;

    public MatchService(MatchRepository matchRepository,
                        StadiumRepository stadiumRepository,
                        UserRepository userRepository,
                        TicketBookingRepository ticketBookingRepository
    ) {
        this.matchRepository = matchRepository;
        this.stadiumRepository = stadiumRepository;
        this.userRepository = userRepository;
        this.ticketBookingRepository = ticketBookingRepository;
    }

    public MatchResponse createMatch(MatchRequest request) {

        Stadium stadium = stadiumRepository.findById(request.getStadium_id()).orElseThrow(
                () -> new StadiumNotFoundException(ExceptionMessage.StadiumFullException)
        );

        Match match = new Match();
        match.setMatchDate(request.getMatchDate());
        match.setTeamA(request.getTeamA());
        match.setTeamB(request.getTeamB());
        match.setTicketPrice(request.getTicketPrice());
        match.setStadium(stadium);

        Match savedMatch = matchRepository.save(match);

        return MatchResponse.builder()
                .id(savedMatch.getId())
                .matchDate(savedMatch.getMatchDate())
                .teamA(savedMatch.getTeamA())
                .teamB(savedMatch.getTeamB())
                .ticketPrice(savedMatch.getTicketPrice())
                .stadiumName(savedMatch.getStadium().getName())
                .build();
    }

    public MatchResponse updateMatch(int mid, MatchRequest request) {

        Match match = matchRepository.findById(mid).orElseThrow(
                () -> new MatchNotFoundException(ExceptionMessage.MatchNotFound)
        );

        if (request.getStadium_id() != null) {
            Stadium stadium = stadiumRepository.findById(request.getStadium_id()).orElseThrow(
                    () -> new StadiumNotFoundException(ExceptionMessage.StadiumNotFound)
            );
            match.setStadium(stadium);
        }

        if (request.getMatchDate() != null) {
            match.setMatchDate(request.getMatchDate());
        }
        if (request.getTeamA() != null) {
            match.setTeamA(request.getTeamA());
        }

        if (request.getTeamB() != null) {
            match.setTeamB(request.getTeamB());
        }
        if (request.getTicketPrice() != null) {
            match.setTicketPrice(request.getTicketPrice());
        }

        Match savedMatch = matchRepository.save(match);

        return MatchResponse.builder()
                .id(savedMatch.getId())
                .matchDate(savedMatch.getMatchDate())
                .teamA(savedMatch.getTeamA())
                .teamB(savedMatch.getTeamB())
                .ticketPrice(savedMatch.getTicketPrice())
                .stadiumName(savedMatch.getStadium().getName())
                .build();

    }

    public String deleteMatch(int mid) {
        Match match = matchRepository.findById(mid).orElseThrow(
                () -> new MatchNotFoundException(ExceptionMessage.MatchNotFound)
        );
        matchRepository.delete(match);
        return "Match successfully deleted!";
    }

    public MatchResponse getMatchById(int mid) {
        Match match = matchRepository.findById(mid).orElseThrow(
                () -> new MatchNotFoundException(ExceptionMessage.MatchNotFound + " " + mid)
        );

        return MatchResponse.builder()
                .id(match.getId())
                .matchDate(match.getMatchDate())
                .teamA(match.getTeamA())
                .teamB(match.getTeamB())
                .ticketPrice(match.getTicketPrice())
                .stadiumName(match.getStadium().getName())
                .build();

    }

    public List<Match> getAllMatches() {

        List<Match> matchList = matchRepository.findAll();
        return matchList;
    }

    //to make sure only atomic operations happens
    @Transactional
    public TicketBookingResponse ticketBooking(int uid, int mid, int seats) {

        Match match = matchRepository.findById(mid).orElseThrow(
                () -> new MatchNotFoundException(ExceptionMessage.MatchNotFound + " " + mid)
        );

        User user = userRepository.findById(uid).orElseThrow(
                () -> new RuntimeException("USER_DOES_NOT_EXIST")
        );

        Stadium stadium = stadiumRepository.findById(match.getStadium().getId()).orElseThrow(
                () -> new StadiumNotFoundException(ExceptionMessage.StadiumNotFound)
        );

        TicketBookings ticketBooking = new TicketBookings();
        ticketBooking.setUserId(uid);
        ticketBooking.setMatchId(mid);

//        int available_seats = match.getStadium().getCapacity();
        int available_seats = stadium.getCapacity();

        if (seats > available_seats) {
            throw new StadiumFullException(ExceptionMessage.StadiumFullException);
        } else {
            ticketBooking.setNumberOfSeats(seats);
            available_seats -= seats;
            stadium.setCapacity(available_seats);
            stadiumRepository.save(stadium);
        }

//        if (1 > 0) {
//            throw new RuntimeException("Exception occur here!");
//        }

        ticketBooking.setTotalAmount(seats * match.getTicketPrice());

        TicketBookings savedticketBookings = ticketBookingRepository.save(ticketBooking);

        return TicketBookingResponse.builder()
                .id(savedticketBookings.getId())
                .name(user.getName())
                .match(match.getTeamA() + " vs " + match.getTeamB())
                .NumberOfSeats(seats)
                .TotalAmount(savedticketBookings.getTotalAmount())
                .build();
    }
}
