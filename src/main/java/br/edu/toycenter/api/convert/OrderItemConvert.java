package br.edu.toycenter.api.convert;

import org.springframework.stereotype.Component;

import br.edu.toycenter.api.response.OrderItemResponseDTO;
import br.edu.toycenter.infrastructure.entities.OrderItem;
import br.edu.toycenter.infrastructure.entities.Product;

@Component
public class OrderItemConvert {
	
	public OrderItemResponseDTO forOrderItemResponseDTO(OrderItem orderItem, Product product) {
		OrderItemResponseDTO OrderItemDTO = new OrderItemResponseDTO(				
				orderItem.getQuantity(),
				orderItem.getPrice(),
				product);
				
		return OrderItemDTO;
	}
}
