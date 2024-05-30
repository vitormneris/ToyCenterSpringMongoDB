package br.edu.toycenter.infrastructure.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.edu.toycenter.infrastructure.entities.Client;

public interface ClientRepository extends MongoRepository<Client, String> {
	
	public Optional<Client> findByCpf(String cpf);
	
	public Optional<Client> findByEmail(String email);
}
