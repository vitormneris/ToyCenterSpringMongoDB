package br.edu.toycenter.api.request;

public record OrderItemRequestDTO(
		int quatity,
		Double price,
		String productId)  {
}
