package br.edu.toycenter.api.response;

import java.util.List;

import br.edu.toycenter.infrastructure.entities.Category;

public record ProductResponseDTO(
		String id,
		String name,
		String image,
		String brand,
		String price,
		String description,
		String details,
		List<Category> categories) {
}
