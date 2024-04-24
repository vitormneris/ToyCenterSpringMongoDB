package br.edu.toycenter.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.edu.toycenter.entities.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {
	
}
