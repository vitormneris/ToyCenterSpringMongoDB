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

import br.edu.toycenter.api.request.ClientRequestDTO;
import br.edu.toycenter.api.response.ClientResponseDTO;
import br.edu.toycenter.business.ClientService;

@RestController
@RequestMapping(value = "/clients")
public class ClientController {
	
	@Autowired
	ClientService service;
	
	@GetMapping
	public ResponseEntity<List<ClientResponseDTO>> findAll() {
		List<ClientResponseDTO> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ClientResponseDTO> findById(@PathVariable String id) {
		ClientResponseDTO obj = service.findById(id);
	    return ResponseEntity.ok().body(obj);
	}
	
	@GetMapping(value = "/email/{email}")
	public ResponseEntity<ClientResponseDTO> findByEmail(@PathVariable String email) {
		ClientResponseDTO obj = service.findByEmail(email);
	    return ResponseEntity.ok().body(obj);
	}
	
	@GetMapping(value = "/cpf/{cpf}")
	public ResponseEntity<ClientResponseDTO> findByCpf(@PathVariable String cpf) {
		ClientResponseDTO obj = service.findByCpf(cpf);
	    return ResponseEntity.ok().body(obj);
	}
	
	@PostMapping
	public ResponseEntity<ClientResponseDTO> insert(@RequestBody ClientRequestDTO clientRequestDTO) {
		ClientResponseDTO obj = service.insert(clientRequestDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.id()).toUri();
	    return ResponseEntity.created(uri).body(obj);
	}

	@PostMapping(value = "/login")
	public ResponseEntity<Void> login(@RequestBody ClientRequestDTO clientRequestDTO) {
		service.login(clientRequestDTO);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ClientResponseDTO> update(@PathVariable String id, @RequestBody ClientRequestDTO clientRequestDTO) {
		ClientResponseDTO obj = service.update(id, clientRequestDTO);
	    return ResponseEntity.ok().body(obj);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		service.delete(id);
	    return ResponseEntity.noContent().build();
	}
}
