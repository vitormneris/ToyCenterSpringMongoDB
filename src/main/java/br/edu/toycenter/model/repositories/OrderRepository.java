package br.edu.toycenter.model.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.edu.toycenter.model.entities.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
	List<Order> findByClientId(String clientId);
	Optional<Order> findByClientIdAndId(String clientId, String id);
}
