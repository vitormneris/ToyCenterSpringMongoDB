package br.edu.toycenter.model.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.edu.toycenter.model.entities.Client;

public interface ClientRepository extends MongoRepository<Client, String> {
	Optional<Client> findByCpf(String cpf);
	Optional<Client> findByEmail(String email);
}
