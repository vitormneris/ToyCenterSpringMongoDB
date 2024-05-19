package br.edu.toycenter.api.response;

import java.time.Instant;
import java.util.List;

import br.edu.toycenter.infrastructure.entities.Client;

public record OrderResponseDTO(
		String id,
		Instant moment,
		Client client,
		List<OrderItemResponseDTO> orderItens) {
}
