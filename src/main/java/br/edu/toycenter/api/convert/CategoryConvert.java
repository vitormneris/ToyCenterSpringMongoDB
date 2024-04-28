package br.edu.toycenter.api.convert;

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
				.build();
		
		for (String productId: categoryDTO.products()) {
			category.getProductsId().add(productId);
		}
		
		return category;
	}
	
	public CategoryResponseDTO forCategoryResponseDTO(Category category, List<Product> listProduct) {
		CategoryResponseDTO categoryDTO = new CategoryResponseDTO(		
				category.getId(),
				category.getName(),
				listProduct);
				
		return categoryDTO;
	}
}
