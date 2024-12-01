package br.edu.toycenter.controller.dto.response;

import br.edu.toycenter.model.entities.Product;

public record OrderItemResponseDTO(
		Integer quantity,
		String price,
		Product product,
		String subTotal
)  {

	@Override
	public String toString() {
		return "\n   Produto: " + product.getName() +
			   "\n   Quantidade: " + quantity +
			   "\n   Preço: " + price +
			   "\n   Subtotal: " + subTotal +
			   "\n";
	}
}
