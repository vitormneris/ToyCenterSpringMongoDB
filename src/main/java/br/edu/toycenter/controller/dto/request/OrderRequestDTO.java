package br.edu.toycenter.controller.dto.request;

import java.util.List;

public record OrderRequestDTO(
		String id,
		String clientId,
		List<OrderItemRequestDTO> orderItems
) {
}