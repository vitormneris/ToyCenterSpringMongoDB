package br.edu.toycenter.model.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.edu.toycenter.model.entities.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
}
