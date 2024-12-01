package br.edu.toycenter.model.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.edu.toycenter.model.entities.Administrator;

public interface AdministratorRepository extends MongoRepository<Administrator, String> {
	Optional<Administrator> findByEmail(String email);
}
