package br.edu.toycenter.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.toycenter.entities.User;
import br.edu.toycenter.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	public List<User> findAll() {
		return repository.findAll();
	}
	
	public User findByCpf(String cpf) {
		Optional<User> obj = repository.findByCpf(cpf);
		return obj.orElseThrow();
	}
	
	public User findByEmail(String email) {
		Optional<User> obj = repository.findByEmail(email);
		return obj.orElseThrow();
	}
}