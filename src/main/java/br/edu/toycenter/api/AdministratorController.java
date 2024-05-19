package br.edu.toycenter.api;

import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.toycenter.api.request.AdministratorRequestDTO;
import br.edu.toycenter.api.response.AdministratorResponseDTO;
import br.edu.toycenter.business.AdministratorService;

@RestController
@RequestMapping(value = "/administrators")
public class AdministratorController {
	
	@Autowired
	AdministratorService service;
	
	@GetMapping
	public ResponseEntity<List<AdministratorResponseDTO>> findAll() {
		List<AdministratorResponseDTO> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<AdministratorResponseDTO> findById(@PathVariable String id) {
		AdministratorResponseDTO obj = service.findById(id);
	    return ResponseEntity.ok().body(obj);
	}
	
	@GetMapping(value = "/email/{email}")
	public ResponseEntity<AdministratorResponseDTO> findByEmail(@PathVariable String email) {
		AdministratorResponseDTO obj = service.findByEmail(email);
	    return ResponseEntity.ok().body(obj);
	}
	
	@PostMapping
	public ResponseEntity<AdministratorResponseDTO> insert(@RequestBody AdministratorRequestDTO administratorRequestDTO) {
		AdministratorResponseDTO obj = service.insert(administratorRequestDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.id()).toUri();
	    return ResponseEntity.created(uri).body(obj);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<AdministratorResponseDTO> update(@PathVariable String id, @RequestBody AdministratorRequestDTO administratorRequestDTO) {
		AdministratorResponseDTO obj = service.update(id, administratorRequestDTO);
	    return ResponseEntity.ok().body(obj);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		service.delete(id);
	    return ResponseEntity.noContent().build();
	}
}
