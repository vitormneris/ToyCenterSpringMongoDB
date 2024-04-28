package br.edu.toycenter.api.convert;

import org.springframework.stereotype.Component;

import br.edu.toycenter.api.response.OrderItemResponseDTO;
import br.edu.toycenter.infrastructure.entities.OrderItem;
import br.edu.toycenter.infrastructure.entities.Product;

@Component
public class OrderItemConvert {
	
//	public Product forProduct(ProductRequestDTO productDTO) {
//		Product product = new Product.Builder()
//				.id(productDTO.id())
//				.name(productDTO.name())
//				.brand(productDTO.brand())
//				.price(productDTO.price())
//				.description(productDTO.description())
//				.details(productDTO.details())
//				.build();
//		
//		for (String categoryId: productDTO.categories()) {
//			product.getCategoriesId().add(categoryId);
//		}
//		
//		return product;
//	}
	
	public OrderItemResponseDTO forOrderItemResponseDTO(OrderItem orderItem, Product product) {
		OrderItemResponseDTO OrderItemDTO = new OrderItemResponseDTO(				
				orderItem.getQuantity(),
				orderItem.getPrice(),
				product);
				
		return OrderItemDTO;
	}
}
