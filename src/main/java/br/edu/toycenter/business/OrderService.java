package br.edu.toycenter.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.toycenter.api.convert.OrderConvert;
import br.edu.toycenter.api.convert.OrderItemConvert;
import br.edu.toycenter.api.response.OrderItemResponseDTO;
import br.edu.toycenter.api.response.OrderResponseDTO;
import br.edu.toycenter.infrastructure.entities.Order;
import br.edu.toycenter.infrastructure.entities.OrderItem;
import br.edu.toycenter.infrastructure.entities.Product;
import br.edu.toycenter.infrastructure.entities.User;
import br.edu.toycenter.infrastructure.repositories.OrderRepository;
import br.edu.toycenter.infrastructure.repositories.ProductRepository;
import br.edu.toycenter.infrastructure.repositories.UserRepository;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository repository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderConvert orderConvert; 

	@Autowired
	private OrderItemConvert orderItemConvert;
	
	public List<OrderResponseDTO> findAll() {
		List<Order> listOrder = repository.findAll();
		
		List<OrderResponseDTO> listOrderDTO = new ArrayList<>();
	
		for (Order order : listOrder) {
			Optional<User> userObj = userRepository.findById(order.getUserId());
			List<OrderItemResponseDTO> listOrderItemDTO = new ArrayList<>();
			
			for (OrderItem orderItem : order.getOrderItens()) {
				Optional<Product> productObj = productRepository.findById(orderItem.getProductId());
				OrderItemResponseDTO orderItemDTO = orderItemConvert.forOrderItemResponseDTO(orderItem, productObj.get());
				listOrderItemDTO.add(orderItemDTO);
			}	
			OrderResponseDTO orderDTO = orderConvert.forOrderResponseDTO(order, userObj.get(), listOrderItemDTO);
			listOrderDTO.add(orderDTO);
				
		}
		
		return listOrderDTO;
	}
}
