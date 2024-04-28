package br.edu.toycenter.infrastructure.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.edu.toycenter.infrastructure.entities.User;

public interface UserRepository extends MongoRepository<User, String> {
	
	public Optional<User> findByCpf(String cpf);
	
	public Optional<User> findByEmail(String email);
}
