package com.tuck.matches.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tuck.matches.entities.Availabilities;
import com.tuck.matches.entities.AvailabilitiesId;

public interface AvailabilitiesRepository extends JpaRepository<Availabilities, AvailabilitiesId> {

	@Query("SELECT f FROM Availabilities f WHERE f.availabilitiesId.username =:email")
	List<Availabilities> retrieveByEmail(@Param("email") String email);
	
	@Query("SELECT f FROM Availabilities f WHERE f.isMentor =true")
	List<Availabilities> retrieveMentorAvalabilities();

	@Query("SELECT f FROM Availabilities f WHERE f.isMentor =false")
	List<Availabilities> retrieveMenteeAvalabilities();

}
