package br.edu.toycenter.business;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.toycenter.infrastructure.entities.Category;
import br.edu.toycenter.infrastructure.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	public List<Category> findAll() {
		return repository.findAll();
	}
	
	public Category findById(String id) {
		Optional<Category> obj = repository.findById(id);
		return obj.orElseThrow();
	}
}
