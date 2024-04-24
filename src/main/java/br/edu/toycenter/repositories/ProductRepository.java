package br.edu.toycenter.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.edu.toycenter.entities.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
	
}
