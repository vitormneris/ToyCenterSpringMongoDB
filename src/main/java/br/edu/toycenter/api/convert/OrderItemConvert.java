package br.edu.toycenter.api.convert;

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
		Product product = null;
		if (orderItemRequest.productId() != null)
			product = productRepository.findById(orderItemRequest.productId()).orElseThrow();

        OrderItem orderItem = new OrderItem.Builder()
				.quantity(orderItemRequest.quantity())
				.product(product)
				.build();
				
		return orderItem;
	}
	
	public OrderItemResponseDTO forOrderItemResponseDTO(OrderItem orderItem) {

		String price = String.format("%.2f", orderItem.getPrice());
		String subtTotal = String.format("%.2f", orderItem.getSubTotal());

		OrderItemResponseDTO OrderItemDTO = new OrderItemResponseDTO(				
				orderItem.getQuantity(),
				price,
				orderItem.getProduct(),
				subtTotal);
				
		return OrderItemDTO;
	}
	
	public OrderItemRequestDTO forOrderItemRequestDTO(OrderItemResponseDTO orderItemResponseDTO) {
		OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO(				
				orderItemResponseDTO.quantity(),
				orderItemResponseDTO.product().getId());
				
		return orderItemRequestDTO;
	}

	public OrderItemRequestDTO forOrderItemRequestDTO(OrderItem orderItem) {
		OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO(
				orderItem.getQuantity(),
				orderItem.getProduct().getId());

		return orderItemRequestDTO;
	}
}
