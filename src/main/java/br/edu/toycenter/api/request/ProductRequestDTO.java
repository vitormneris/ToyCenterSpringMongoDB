package br.edu.toycenter.api.request;

public record ProductRequestDTO(
		String name,
		String brand,
		Double price,
		String description,
		String details)  {
}
