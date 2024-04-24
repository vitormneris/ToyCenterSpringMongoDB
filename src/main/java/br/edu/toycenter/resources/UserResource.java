package br.edu.toycenter.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.toycenter.entities.User;
import br.edu.toycenter.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserResource {
	
	@Autowired
	UserService service;
	
	@GetMapping
	public ResponseEntity<List<User>> findAll() {
		List<User> list = service.findAll();
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
