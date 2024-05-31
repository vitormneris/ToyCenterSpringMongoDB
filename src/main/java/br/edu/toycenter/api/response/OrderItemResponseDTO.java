package br.edu.toycenter.api.response;

import br.edu.toycenter.infrastructure.entities.Product;

public record OrderItemResponseDTO(
		Integer quantity,
		Double price, 
		Product product,
		Double subTotal)  {

	@Override
	public String toString() {
		return "\n   Produto: " + product.getName() +
			   "\n   Quantidade: " + quantity +
			   "\n   Preço: " + price +
			   "\n   Subtotal: " + subTotal +
			   "\n";
	}
}
