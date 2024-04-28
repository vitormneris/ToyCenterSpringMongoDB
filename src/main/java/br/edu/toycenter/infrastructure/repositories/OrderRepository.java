package br.edu.toycenter.infrastructure.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.edu.toycenter.infrastructure.entities.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
	
	public Optional<Order> findByUserId(String userId);
}
