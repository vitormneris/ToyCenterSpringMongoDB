package br.edu.toycenter.controller.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import br.edu.toycenter.controller.dto.request.OrderItemRequestDTO;
import br.edu.toycenter.controller.dto.response.OrderItemResponseDTO;
import br.edu.toycenter.model.entities.OrderItem;
import br.edu.toycenter.model.entities.Product;
import br.edu.toycenter.model.repositories.ProductRepository;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {
	private final  ProductRepository productRepository;
	
	public OrderItem forOrderItem(OrderItemRequestDTO orderItemRequest) {
		Product product = null;
		if (orderItemRequest.productId() != null)
			product = productRepository.findById(orderItemRequest.productId()).orElseThrow();

        return OrderItem.builder()
				.quantity(orderItemRequest.quantity())
				.product(product)
				.build();
	}
	
	public OrderItemResponseDTO forOrderItemResponseDTO(OrderItem orderItem) {
		String price = String.format("%.2f", orderItem.getPrice());
		String subtTotal = String.format("%.2f", orderItem.getSubTotal());

        return new OrderItemResponseDTO(
                orderItem.getQuantity(),
                price,
                orderItem.getProduct(),
                subtTotal);
	}
	
	public OrderItemRequestDTO forOrderItemRequestDTO(OrderItemResponseDTO orderItemResponseDTO) {
        return new OrderItemRequestDTO(
                orderItemResponseDTO.quantity(),
                orderItemResponseDTO.product().getId());
	}

	public OrderItemRequestDTO forOrderItemRequestDTO(OrderItem orderItem) {
        return new OrderItemRequestDTO(
                orderItem.getQuantity(),
                orderItem.getProduct().getId());
	}
}
