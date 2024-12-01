package br.edu.toycenter.model.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.edu.toycenter.model.entities.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
