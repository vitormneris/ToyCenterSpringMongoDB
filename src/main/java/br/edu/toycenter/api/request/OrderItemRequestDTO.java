package br.edu.toycenter.api.request;

public record OrderItemRequestDTO(
		Integer quantity,
		String productId)  {
}
