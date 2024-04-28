package br.edu.toycenter.api.convert;

import java.util.List;

import org.springframework.stereotype.Component;

import br.edu.toycenter.api.response.UserResponseDTO;
import br.edu.toycenter.infrastructure.entities.Order;
import br.edu.toycenter.infrastructure.entities.User;

@Component
public class UserConvert {
	
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
	
	public UserResponseDTO forUserResponseDTO(User user, List<Order> listOrder) {
		UserResponseDTO userDTO = new UserResponseDTO(				
				user.getId(),
				user.getCpf(),
				user.getName(),
				user.getEmail(),
				user.getPhone(),
				user.getPassword(),
				listOrder);
				
		return userDTO;
	}
}
