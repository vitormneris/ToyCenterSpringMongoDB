package br.edu.toycenter.infrastructure.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.edu.toycenter.infrastructure.entities.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
	
}
