package br.edu.toycenter.api.convert;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.edu.toycenter.api.request.ProductRequestDTO;
import br.edu.toycenter.api.response.ProductResponseDTO;
import br.edu.toycenter.infrastructure.entities.Category;
import br.edu.toycenter.infrastructure.entities.Product;

@Component
public class ProductConvert {

	public Product forProduct(ProductRequestDTO productDTO) {
		Product product = new Product.Builder()
				.name(productDTO.name())
				.image(productDTO.image())
				.brand(productDTO.brand())
				.price(productDTO.price())
				.description(productDTO.description())
				.details(productDTO.details())
				.categoriesId(productDTO.categoriesId())
				.build();

		return product;
	}

	public ProductResponseDTO forProductResponseDTO(Product product, List<Category> listCategory) {
		String price = String.format("%.2f", product.getPrice());

		ProductResponseDTO productDTO = new ProductResponseDTO(
				product.getId(),
				product.getName(),
				product.getImage(),
				product.getBrand(),
				price,
				product.getDescription(),
				product.getDetails(),
				listCategory);
				
		return productDTO;
	}
	
	public ProductRequestDTO forProductRequestDTO(ProductResponseDTO productResponseDTO) {
		List<String> categoriesId = new ArrayList<>();
		for (Category category : productResponseDTO.categories()) {
			categoriesId.add(category.getId());
		}
		Double price = Double.parseDouble(productResponseDTO.price());
		ProductRequestDTO productRequestDTO = new ProductRequestDTO(
				productResponseDTO.id(),
				productResponseDTO.name(),
				productResponseDTO.image(),
				productResponseDTO.brand(),
				price,
				productResponseDTO.description(),
				productResponseDTO.details(),
				categoriesId);
		return productRequestDTO;

	}
	
}
