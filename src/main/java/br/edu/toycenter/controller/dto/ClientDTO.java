package br.edu.toycenter.controller.dto;

import java.util.List;

import br.edu.toycenter.model.entities.Order;

public record ClientDTO(
		String id,
		String cpf,
		String name,
		String email,
		String phone,
		String password,
		List<Order> orders
)  {
}
