package br.edu.toycenter.controller.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.edu.toycenter.controller.dto.request.ProductRequestDTO;
import br.edu.toycenter.controller.dto.response.ProductResponseDTO;
import br.edu.toycenter.model.entities.Category;
import br.edu.toycenter.model.entities.Product;

@Component
public class ProductMapper {

	public Product forProduct(ProductRequestDTO productDTO) {
        return Product.builder()
                .name(productDTO.name())
                .image(productDTO.image())
                .brand(productDTO.brand())
                .price(productDTO.price())
                .description(productDTO.description())
                .details(productDTO.details())
                .categoriesId(productDTO.categoriesId())
                .build();
	}

	public ProductResponseDTO forProductResponseDTO(Product product, List<Category> listCategory) {
		String price = String.format("%.2f", product.getPrice());

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getImage(),
                product.getBrand(),
                price,
                product.getDescription(),
                product.getDetails(),
                listCategory);
	}
	
	public ProductRequestDTO forProductRequestDTO(ProductResponseDTO productResponseDTO) {
		List<String> categoriesId = new ArrayList<>();
		for (Category category : productResponseDTO.categories()) {
			categoriesId.add(category.getId());
		}
		Double price = Double.parseDouble(productResponseDTO.price());
        return new ProductRequestDTO(
                productResponseDTO.id(),
                productResponseDTO.name(),
                productResponseDTO.image(),
                productResponseDTO.brand(),
                price,
                productResponseDTO.description(),
                productResponseDTO.details(),
                categoriesId);
	}
}
