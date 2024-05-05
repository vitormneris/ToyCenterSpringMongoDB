package br.edu.toycenter.api.request;

public record ProductRequestDTO(
		String id,
		String name,
		String brand,
		Double price,
		String description,
		String details)  {
}
