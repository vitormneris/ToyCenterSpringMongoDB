package br.edu.toycenter.api.convert;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.toycenter.api.request.OrderItemRequestDTO;
import br.edu.toycenter.api.request.OrderRequestDTO;
import br.edu.toycenter.api.response.OrderItemResponseDTO;
import br.edu.toycenter.api.response.OrderResponseDTO;
import br.edu.toycenter.infrastructure.entities.Client;
import br.edu.toycenter.infrastructure.entities.Order;
import br.edu.toycenter.infrastructure.entities.OrderItem;

@Component
public class OrderConvert {
	
	@Autowired
	OrderItemConvert orderItemConvert;
	
	public Order forOrder(OrderRequestDTO orderDTO) {

		List<OrderItem> listOrdemItem = new ArrayList<>();
		
		for (OrderItemRequestDTO oird : orderDTO.orderItens()) {
			OrderItem oi = orderItemConvert.forOrderItem(oird);
			listOrdemItem.add(oi);
		}
				
		Order order = new Order.Builder()
				.id(orderDTO.id())
				.clientId(orderDTO.clientId())
				.orderItens(listOrdemItem)
				.build();

		return order;
	}
	
	public OrderResponseDTO forOrderResponseDTO(Order order, Client client, List<OrderItemResponseDTO> orderItemDTO) {
		OrderResponseDTO orderDTO = new OrderResponseDTO(				
				order.getId(),
				order.getMoment(),
				client,
				orderItemDTO);
				
		return orderDTO;
	}
}
