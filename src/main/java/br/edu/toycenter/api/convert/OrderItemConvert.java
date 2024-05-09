package br.edu.toycenter.api.convert;

import org.springframework.stereotype.Component;

import br.edu.toycenter.api.response.OrderItemResponseDTO;
import br.edu.toycenter.infrastructure.entities.OrderItem;

@Component
public class OrderItemConvert {
	
	public OrderItemResponseDTO forOrderItemResponseDTO(OrderItem orderItem) {
		OrderItemResponseDTO OrderItemDTO = new OrderItemResponseDTO(				
				orderItem.getQuantity(),
				orderItem.getPrice(),
				orderItem.getProduct());
				
		return OrderItemDTO;
	}
}
