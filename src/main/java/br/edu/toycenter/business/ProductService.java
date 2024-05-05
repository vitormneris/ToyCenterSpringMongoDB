package br.edu.toycenter.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.toycenter.api.convert.ProductConvert;
import br.edu.toycenter.api.response.ProductResponseDTO;
import br.edu.toycenter.infrastructure.entities.Category;
import br.edu.toycenter.infrastructure.entities.Product;
import br.edu.toycenter.infrastructure.repositories.CategoryRepository;
import br.edu.toycenter.infrastructure.repositories.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductConvert productConvert;
	
	public List<ProductResponseDTO> findAll() {
		List<Product> listProduct = repository.findAll();		
		List<ProductResponseDTO> listProductDTO = new ArrayList<>();
		
		for (Product product : listProduct) {
			listProductDTO.add(productToProductResponseDTO(product));
		}
		
		return listProductDTO;
	}
	
	public ProductResponseDTO findById(String id) {
		Optional<Product> obj = repository.findById(id);
		ProductResponseDTO responseDTO = productToProductResponseDTO(obj.orElseThrow());
		return responseDTO;
	}
	
	public ProductResponseDTO productToProductResponseDTO(Product product) {
			List<Category> listCategory = new ArrayList<>();
			
			for (String id : product.getCategoriesId()) {
				Optional<Category> obj = categoryRepository.findById(id);
				listCategory.add(obj.orElseThrow());
			}
			
			ProductResponseDTO productDTO = productConvert.forProductResponseDTO(product, listCategory);
			return productDTO;
	}
}
