package br.edu.toycenter.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.toycenter.api.response.UserResponseDTO;
import br.edu.toycenter.business.UserService;
import br.edu.toycenter.infrastructure.entities.User;

@RestController
@RequestMapping(value = "/users")
public class UserController {
	
	@Autowired
	UserService service;
	
	@GetMapping
	public ResponseEntity<List<UserResponseDTO>> findAll() {
		List<UserResponseDTO> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/email/{email}")
	public ResponseEntity<User> findByEmail(@PathVariable String email) {
	    User obj = service.findByEmail(email);
	    return ResponseEntity.ok().body(obj);
	}
	
	@GetMapping(value = "/cpf/{cpf}")
	public ResponseEntity<User> findByCpf(@PathVariable String cpf) {
	    User obj = service.findByCpf(cpf);
	    return ResponseEntity.ok().body(obj);
	}
}
