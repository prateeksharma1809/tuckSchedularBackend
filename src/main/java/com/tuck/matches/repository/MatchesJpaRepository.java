package com.tuck.matches.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tuck.matches.entities.Matches;
import com.tuck.matches.entities.MatchesId;

public interface MatchesJpaRepository extends JpaRepository<Matches, MatchesId > {
	
	@Query("SELECT count(f) FROM Matches f WHERE f.matchesId.email_mentor =:emailmentor AND f.matchesId.email_mentee =:emailmentee")
	long countMatchesOfMentorAndMentee(@Param("emailmentor") String emailmentor, @Param("emailmentee") String emailmentee);

}
