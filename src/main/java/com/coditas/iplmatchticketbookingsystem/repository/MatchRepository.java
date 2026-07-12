package com.coditas.iplmatchticketbookingsystem.repository;

import com.coditas.iplmatchticketbookingsystem.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {

//    @Query("Select distinct m from match m")
//    List<Match> findAllMatches();


}
