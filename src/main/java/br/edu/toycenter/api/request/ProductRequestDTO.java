package br.edu.toycenter.api.request;

import java.util.List;

public record ProductRequestDTO(
		String id,
		String name,
		String image,
		String brand,
		Double price,
		String description,
		String details,
 		List<String> categoriesId)  {
}
