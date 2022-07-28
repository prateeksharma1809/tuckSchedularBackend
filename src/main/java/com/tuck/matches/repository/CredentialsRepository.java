package com.tuck.matches.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuck.matches.entities.Credentials;

public interface CredentialsRepository extends JpaRepository<Credentials, String> {

}
