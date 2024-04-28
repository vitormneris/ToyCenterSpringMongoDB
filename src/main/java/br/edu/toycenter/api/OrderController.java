package br.edu.toycenter.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.toycenter.api.response.OrderResponseDTO;
import br.edu.toycenter.business.OrderService;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {
	
	@Autowired
	OrderService service;
	
	@GetMapping
	public ResponseEntity<List<OrderResponseDTO>> findAll() {
		List<OrderResponseDTO> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
}
