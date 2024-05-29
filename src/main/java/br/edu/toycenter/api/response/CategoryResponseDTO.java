package br.edu.toycenter.api.response;

import java.util.List;

import br.edu.toycenter.infrastructure.entities.Product;

public record CategoryResponseDTO(
		String id,
		String name,
		String image,
		List<Product> products) {
}
