package br.edu.toycenter.api.convert;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.toycenter.api.request.OrderItemRequestDTO;
import br.edu.toycenter.api.response.OrderItemResponseDTO;
import br.edu.toycenter.infrastructure.entities.OrderItem;
import br.edu.toycenter.infrastructure.entities.Product;
import br.edu.toycenter.infrastructure.repositories.ProductRepository;

@Component
public class OrderItemConvert {
	
	@Autowired
	private ProductRepository productRepository;
	
	public OrderItem forOrderItem(OrderItemRequestDTO orderItemRequest) {
		Optional<Product> obj = null;
		if (orderItemRequest.productId() != null) 
			obj = productRepository.findById(orderItemRequest.productId());
	
		OrderItem OrderItem = new OrderItem.Builder()
				.quantity(orderItemRequest.quantity())
				.product(obj.get())
				.build();
				
		return OrderItem;
	}
	
	public OrderItemResponseDTO forOrderItemResponseDTO(OrderItem orderItem) {
		OrderItemResponseDTO OrderItemDTO = new OrderItemResponseDTO(				
				orderItem.getQuantity(),
				orderItem.getPrice(),
				orderItem.getProduct(),
				orderItem.getSubTotal());
				
		return OrderItemDTO;
	}
}
