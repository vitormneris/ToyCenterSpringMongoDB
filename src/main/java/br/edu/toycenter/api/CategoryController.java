package br.edu.toycenter.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.toycenter.business.CategoryService;
import br.edu.toycenter.infrastructure.entities.Category;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {
	
	@Autowired
	CategoryService service;
	
	@GetMapping
	public ResponseEntity<List<Category>> findAll() {
		List<Category> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
}
