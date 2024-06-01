package br.edu.toycenter.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.toycenter.api.request.CategoryRequestDTO;
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
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryResponseDTO> findById(@PathVariable String id) {
		CategoryResponseDTO obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}

	@PostMapping
	public ResponseEntity<CategoryResponseDTO> insert(@RequestBody CategoryRequestDTO categoryDTO) {
		CategoryResponseDTO obj = service.insert(categoryDTO);
		return ResponseEntity.ok().body(obj);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<CategoryResponseDTO> update(@PathVariable String id, @RequestBody CategoryRequestDTO categoryDTO) {
		CategoryResponseDTO obj = service.update(id, categoryDTO);
		return ResponseEntity.ok().body(obj);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
