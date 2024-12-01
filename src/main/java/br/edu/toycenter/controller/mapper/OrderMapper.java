package br.edu.toycenter.controller.mapper;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import br.edu.toycenter.controller.dto.request.OrderItemRequestDTO;
import br.edu.toycenter.controller.dto.request.OrderRequestDTO;
import br.edu.toycenter.controller.dto.response.OrderItemResponseDTO;
import br.edu.toycenter.controller.dto.response.OrderResponseDTO;
import br.edu.toycenter.model.entities.Client;
import br.edu.toycenter.model.entities.Order;
import br.edu.toycenter.model.entities.OrderItem;
import br.edu.toycenter.model.repositories.ClientRepository;

@Component
@RequiredArgsConstructor
public class OrderMapper {
	private final OrderItemMapper orderItemMapper;
	private final ClientRepository clientRepository;
	
	public Order forOrder(OrderRequestDTO orderDTO) {

		List<OrderItem> listOrdemItem = new ArrayList<>();
		
		for (OrderItemRequestDTO oird : orderDTO.orderItems()) {
			OrderItem oi = orderItemMapper.forOrderItem(oird);
			listOrdemItem.add(oi);
		}

        return Order.builder()
                .id(orderDTO.id())
                .clientId(orderDTO.clientId())
                .orderItens(listOrdemItem)
                .build();
	}
	
	public OrderResponseDTO forOrderResponseDTO(Order order, Client client, List<OrderItemResponseDTO> orderItemDTO) {
		String formattedDateTime = "NO DATE";
		if (order.getMoment() != null) {
			Instant instant = order.getMoment();
			ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
			formattedDateTime = formatter.format(instant.atZone(zoneId));
		}

		String total = String.format("%.2f", order.getTotal());

        return new OrderResponseDTO(
                order.getId(),
                formattedDateTime,
                total,
                client,
                orderItemDTO);
	}

	public OrderResponseDTO forOrderResponseDTO(Order order) {
		String formattedDateTime = "NO DATE";
		if (order.getMoment() == null) {
			Instant instant = order.getMoment();
			ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
			formattedDateTime = formatter.format(instant.atZone(zoneId));
		}
		Client client = clientRepository.findById(order.getClientId()).orElseThrow();

		List<OrderItemResponseDTO> orderItemResponseDTOS = new ArrayList<>();
		for (OrderItem orderItem : order.getOrderItens()) {
			orderItemResponseDTOS.add(orderItemMapper.forOrderItemResponseDTO(orderItem));
		}

		String total = String.format("%.2f", order.getTotal());

        return new OrderResponseDTO(
                order.getId(),
                formattedDateTime,
                total,
                client,
                orderItemResponseDTOS);
	}

	public OrderRequestDTO forOrderRequestDTO(Order order) {
		List<OrderItemRequestDTO> orderItemRequestDTOList = new ArrayList<>();
		for (OrderItem orderItem : order.getOrderItens()) {
			orderItemRequestDTOList.add(orderItemMapper.forOrderItemRequestDTO(orderItem));
		}

        return new OrderRequestDTO(
                order.getId(),
                order.getClientId(),
                orderItemRequestDTOList);
	}
}
