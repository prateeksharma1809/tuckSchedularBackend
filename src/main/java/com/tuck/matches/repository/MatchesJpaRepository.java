package com.tuck.matches.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuck.matches.entities.Matches;
import com.tuck.matches.entities.MatchesId;

public interface MatchesJpaRepository extends JpaRepository<Matches, MatchesId > {

}
