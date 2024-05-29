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
import br.edu.toycenter.infrastructure.repositories.ClientRepository;

@Component
public class OrderConvert {
	
	@Autowired
	OrderItemConvert orderItemConvert;

	@Autowired
	ClientRepository clientRepository;
	
	public Order forOrder(OrderRequestDTO orderDTO) {

		List<OrderItem> listOrdemItem = new ArrayList<>();
		
		for (OrderItemRequestDTO oird : orderDTO.orderItems()) {
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
				order.getTotal(),
				client,
				orderItemDTO);
				
		return orderDTO;
	}

	public OrderResponseDTO forOrderResponseDTO(Order order) {
		Client client = clientRepository.findById(order.getClientId()).orElseThrow();

		List<OrderItemResponseDTO> orderItemResponseDTOS = new ArrayList<>();
		for (OrderItem orderItem : order.getOrderItens()) {
			orderItemResponseDTOS.add(orderItemConvert.forOrderItemResponseDTO(orderItem));
		}

		OrderResponseDTO orderDTO = new OrderResponseDTO(
				order.getId(),
				order.getMoment(),
				order.getTotal(),
				client,
				orderItemResponseDTOS);

		return orderDTO;
	}
	
	public OrderRequestDTO forOrderRequestDTO(OrderResponseDTO orderResponseDTO) {
		List<OrderItemRequestDTO> ListOrderItemRequestDTO = new ArrayList<>();
		for (OrderItemResponseDTO dto : orderResponseDTO.orderItens()) {
			ListOrderItemRequestDTO.add(orderItemConvert.forOrderItemRequestDTO(dto));
		}
		
		OrderRequestDTO orderRequestDTO = new OrderRequestDTO(				
				orderResponseDTO.id(),
				orderResponseDTO.client().getId(),
				ListOrderItemRequestDTO);
				
		return orderRequestDTO;
	}

	public OrderRequestDTO forOrderRequestDTO(Order order) {
		List<OrderItemRequestDTO> orderItemRequestDTOList = new ArrayList<>();
		for (OrderItem orderItem : order.getOrderItens()) {
			orderItemRequestDTOList.add(orderItemConvert.forOrderItemRequestDTO(orderItem));
		}

		OrderRequestDTO orderRequestDTO = new OrderRequestDTO(
				order.getId(),
				order.getClientId(),
				orderItemRequestDTOList);

		return orderRequestDTO;
	}
}
