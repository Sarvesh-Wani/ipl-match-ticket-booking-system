package com.coditas.iplmatchticketbookingsystem.exception;

public final class ExceptionMessage {

    private ExceptionMessage() {
    }

    public final static String MatchNotFound = "MATCH_NOT_FOUND_FOR_GIVEN_ID";
    public final static String StadiumFullException = "NO_SEATS_ARE_AVAILABLE_IN_STADIUM";
    public final static String StadiumNotFound = "STADIUM_NOT_FOUND_FOR_GIVEN_ID";

}
