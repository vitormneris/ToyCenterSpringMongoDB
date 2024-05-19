package br.edu.toycenter.infrastructure.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.edu.toycenter.infrastructure.entities.Administrator;

public interface AdministratorRepository extends MongoRepository<Administrator, String> {
		
	public Optional<Administrator> findByEmail(String email);
}
