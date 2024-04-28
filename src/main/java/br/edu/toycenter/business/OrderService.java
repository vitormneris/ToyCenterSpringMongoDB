package br.edu.toycenter.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.toycenter.api.convert.OrderConvert;
import br.edu.toycenter.api.response.OrderResponseDTO;
import br.edu.toycenter.infrastructure.entities.Order;
import br.edu.toycenter.infrastructure.entities.User;
import br.edu.toycenter.infrastructure.repositories.OrderRepository;
import br.edu.toycenter.infrastructure.repositories.UserRepository;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository repository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderConvert orderConvert; 

	
	public List<OrderResponseDTO> findAll() {
		List<Order> listOrder = repository.findAll();
		
		List<OrderResponseDTO> listOrderDTO = new ArrayList<>();
	
		for (Order order : listOrder) {
			Optional<User> obj = userRepository.findById(order.getUserId());
			OrderResponseDTO orderDTO = orderConvert.forOrderResponseDTO(order, obj.get());
			listOrderDTO.add(orderDTO);
		}
		
		return listOrderDTO;
	}
}
