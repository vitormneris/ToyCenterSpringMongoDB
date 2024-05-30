package br.edu.toycenter.infrastructure.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.edu.toycenter.infrastructure.entities.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
	
	public List<Order> findByClientId(String clientId);

	public Optional<Order> findByClientIdAndId(String clientId, String id);

}
