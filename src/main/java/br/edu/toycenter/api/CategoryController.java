package br.edu.toycenter.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.toycenter.api.response.CategoryResponseDTO;
import br.edu.toycenter.business.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {
	
	@Autowired
	CategoryService service;
	
	@GetMapping
	public ResponseEntity<List<CategoryResponseDTO>> findAll() {
		List<CategoryResponseDTO> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
}
