package br.edu.toycenter.api.response;

import java.time.Instant;

import br.edu.toycenter.infrastructure.entities.User;

public record OrderResponseDTO(
		String id,
		Instant moement,
		User user) {
}
