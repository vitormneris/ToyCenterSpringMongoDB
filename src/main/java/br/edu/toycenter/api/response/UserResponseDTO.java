package br.edu.toycenter.api.response;

import java.util.List;

import br.edu.toycenter.infrastructure.entities.Order;

public record UserResponseDTO(
		String id,
		String cpf,
		String name,
		String email,
		String phone,
		String password,
		List<Order> orders)  {
}
