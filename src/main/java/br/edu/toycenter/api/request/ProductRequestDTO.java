package br.edu.toycenter.api.request;

public record ProductRequestDTO(
		String name,
		String brand,
		String image,
		Double price,
		String description,
		String details)  {
	
}
