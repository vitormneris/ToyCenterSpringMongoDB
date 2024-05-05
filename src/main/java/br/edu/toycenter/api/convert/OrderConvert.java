package br.edu.toycenter.api.convert;

import java.util.List;

import org.springframework.stereotype.Component;

import br.edu.toycenter.api.request.OrderRequestDTO;
import br.edu.toycenter.api.response.OrderItemResponseDTO;
import br.edu.toycenter.api.response.OrderResponseDTO;
import br.edu.toycenter.infrastructure.entities.Order;
import br.edu.toycenter.infrastructure.entities.User;

@Component
public class OrderConvert {
	
	public Order forOrder(OrderRequestDTO orderDTO) {
		Order order = new Order.Builder()
				.id(orderDTO.id())
				.moment(orderDTO.moment())
				.userId(orderDTO.userId())
				.orderItens(orderDTO.orderItens())
				.build();

		return order;
	}
	
	public OrderResponseDTO forOrderResponseDTO(Order order, User user, List<OrderItemResponseDTO> orderItemDTO) {
		OrderResponseDTO orderDTO = new OrderResponseDTO(				
				order.getId(),
				order.getMoment(),
				user,
				orderItemDTO);
				
		return orderDTO;
	}
}
