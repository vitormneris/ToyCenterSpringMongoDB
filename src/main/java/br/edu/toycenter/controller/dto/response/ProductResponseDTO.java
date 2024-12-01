package br.edu.toycenter.controller.dto.response;

import java.util.List;

import br.edu.toycenter.model.entities.Category;

public record ProductResponseDTO(
		String id,
		String name,
		String image,
		String brand,
		String price,
		String description,
		String details,
		List<Category> categories
) {
}
