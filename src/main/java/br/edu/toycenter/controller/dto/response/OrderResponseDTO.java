package br.edu.toycenter.controller.dto.response;

import java.util.List;

import br.edu.toycenter.model.entities.Client;

public record OrderResponseDTO(
		String id,
		String moment,
		String total,
		Client client,
		List<OrderItemResponseDTO> orderItens
) {
}
