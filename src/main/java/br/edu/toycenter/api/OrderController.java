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

import br.edu.toycenter.api.request.OrderRequestDTO;
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
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<OrderResponseDTO> findById(@PathVariable String id) {
		OrderResponseDTO obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}

	@GetMapping(value = "/client/{clientId}")
	public ResponseEntity<List<OrderResponseDTO>> findByClientId(@PathVariable String clientId) {
		List<OrderResponseDTO> obj = service.findByClientId(clientId);
		return ResponseEntity.ok().body(obj);
	}
	
	@PostMapping
	public ResponseEntity<OrderResponseDTO> insert(@RequestBody OrderRequestDTO orderDTO) {
		OrderResponseDTO obj = service.insert(orderDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.id()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}
	
	@PutMapping(value = "/{clientId}/{orderId}")
	public ResponseEntity<OrderResponseDTO> updateByClientId(@PathVariable String clientId, @PathVariable String orderId, @RequestBody OrderRequestDTO orderDTO) {
		OrderResponseDTO obj = service.updateByClientId(clientId, orderId, orderDTO);
		return ResponseEntity.ok().body(obj);
	}
	
	@DeleteMapping(value = "/{clientId}/{orderId}")
	public ResponseEntity<Void> deleteByClientId(@PathVariable String clientId, @PathVariable String orderId) {
		service.deleteByClientIdAndId(clientId, orderId);
		return ResponseEntity.noContent().build();
	}
}
