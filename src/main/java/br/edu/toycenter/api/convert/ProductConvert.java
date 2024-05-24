package br.edu.toycenter.api.convert;

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
				.build();

		return product;
	}

	public ProductResponseDTO forProductResponseDTO(Product product, List<Category> listCategory) {
		ProductResponseDTO productDTO = new ProductResponseDTO(				
				product.getId(),
				product.getName(),
				product.getImage(),
				product.getBrand(),
				product.getPrice(),
				product.getDescription(),
				product.getDetails(),
				listCategory);
				
		return productDTO;
	}
	
	
}
