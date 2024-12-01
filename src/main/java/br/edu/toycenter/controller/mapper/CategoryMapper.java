package br.edu.toycenter.controller.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.edu.toycenter.controller.dto.request.CategoryRequestDTO;
import br.edu.toycenter.controller.dto.response.CategoryResponseDTO;
import br.edu.toycenter.model.entities.Category;
import br.edu.toycenter.model.entities.Product;

@Component
public class CategoryMapper {
	
	public Category forCategory(CategoryRequestDTO categoryDTO) {
        return Category.builder()
                .id(categoryDTO.id())
                .name(categoryDTO.name())
                .image(categoryDTO.image())
                .productsId(categoryDTO.productsId())
                .build();
	}
	
	public CategoryResponseDTO forCategoryResponseDTO(Category category, List<Product> listProduct) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getImage(),
                listProduct);
	}

	public CategoryRequestDTO forCategoryRequestDTO(CategoryResponseDTO categoryResponseDTO) {
		List<String> productsId = new ArrayList<>();
		for (Product product : categoryResponseDTO.products()) {
			productsId.add(product.getId());
		}

        return new CategoryRequestDTO(
                categoryResponseDTO.id(),
                categoryResponseDTO.name(),
                categoryResponseDTO.image(),
                productsId);

	}
}
