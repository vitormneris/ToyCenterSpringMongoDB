package br.edu.toycenter.infrastructure.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.edu.toycenter.infrastructure.entities.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {
	
}
