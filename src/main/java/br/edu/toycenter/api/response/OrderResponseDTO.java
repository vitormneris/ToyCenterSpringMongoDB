package br.edu.toycenter.api.response;

import java.time.Instant;
import java.util.List;

import br.edu.toycenter.infrastructure.entities.User;

public record OrderResponseDTO(
		String id,
		Instant moment,
		User user,
		List<OrderItemResponseDTO> orderItens) {
}
