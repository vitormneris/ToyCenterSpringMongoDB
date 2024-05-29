package br.edu.toycenter.api.convert;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.edu.toycenter.api.request.CategoryRequestDTO;
import br.edu.toycenter.api.response.CategoryResponseDTO;
import br.edu.toycenter.infrastructure.entities.Category;
import br.edu.toycenter.infrastructure.entities.Product;

@Component
public class CategoryConvert {
	
	public Category forCategory(CategoryRequestDTO categoryDTO) {
		Category category = new Category.Builder()
				.id(categoryDTO.id())
				.name(categoryDTO.name())
				.image(categoryDTO.image())
				.productsId(categoryDTO.productsId())
				.build();

		return category;
	}
	
	public CategoryResponseDTO forCategoryResponseDTO(Category category, List<Product> listProduct) {
		CategoryResponseDTO categoryDTO = new CategoryResponseDTO(		
				category.getId(),
				category.getName(),
				category.getImage(),
				listProduct);
				
		return categoryDTO;
	}

	public CategoryRequestDTO forCategoryRequestDTO(CategoryResponseDTO categoryResponseDTO) {
		List<String> productsId = new ArrayList<>();
		for (Product product : categoryResponseDTO.products()) {
			productsId.add(product.getId());
		}

		CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO(
				categoryResponseDTO.id(),
				categoryResponseDTO.name(),
				categoryResponseDTO.image(),
				productsId);
		return categoryRequestDTO;

	}
}
