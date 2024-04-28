package br.edu.toycenter.api.convert;

import java.util.List;

import org.springframework.stereotype.Component;

import br.edu.toycenter.api.response.ProductResponseDTO;
import br.edu.toycenter.infrastructure.entities.Category;
import br.edu.toycenter.infrastructure.entities.Product;

@Component
public class ProductConvert {
	

	public ProductResponseDTO forProductResponseDTO(Product product, List<Category> listCategory) {
		ProductResponseDTO productDTO = new ProductResponseDTO(				
				product.getId(),
				product.getName(),
				product.getBrand(),
				product.getPrice(),
				product.getDescription(),
				product.getDetails(),
				listCategory);
				
		return productDTO;
	}
}
