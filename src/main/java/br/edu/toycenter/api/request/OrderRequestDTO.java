package br.edu.toycenter.api.request;

import java.util.List;

public record OrderRequestDTO(
		String id,
		String userId,
		List<OrderItemRequestDTO> orderItens)  {
}
