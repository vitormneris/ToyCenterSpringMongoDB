package br.edu.toycenter.controller.dto.response;

import java.util.List;

import br.edu.toycenter.model.entities.Product;

public record CategoryResponseDTO(
		String id,
		String name,
		String image,
		List<Product> products
) {
}
