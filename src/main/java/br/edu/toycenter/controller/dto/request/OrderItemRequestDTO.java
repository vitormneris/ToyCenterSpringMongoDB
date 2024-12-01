package br.edu.toycenter.controller.dto.request;

public record OrderItemRequestDTO(
		Integer quantity,
		String productId
) {
}