package br.edu.toycenter.infrastructure.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.edu.toycenter.infrastructure.entities.Order;

public interface OrderRepository extends MongoRepository<Order, String> {

}
