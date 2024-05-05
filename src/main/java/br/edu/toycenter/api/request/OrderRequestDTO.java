package br.edu.toycenter.api.request;

import java.time.Instant;
import java.util.List;

import br.edu.toycenter.infrastructure.entities.OrderItem;

public record OrderRequestDTO(
		String id,
		Instant moment,
		String userId,
		List<OrderItem> orderItens)  {
}
