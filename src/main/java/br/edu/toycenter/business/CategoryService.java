package br.edu.toycenter.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.toycenter.api.convert.CategoryConvert;
import br.edu.toycenter.api.response.CategoryResponseDTO;
import br.edu.toycenter.infrastructure.entities.Category;
import br.edu.toycenter.infrastructure.entities.Product;
import br.edu.toycenter.infrastructure.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryConvert categoryConvert;
	
	public List<CategoryResponseDTO> findAll() {
		List<Category> listCategory = repository.findAll();
		
		List<CategoryResponseDTO> listCategoryDTO = new ArrayList<>();
		
		for (Category category : listCategory) {
			List<Product> listProduct = new ArrayList<>();
			for (String productId : category.getProductsId()) {
				Product product = productService.findAllById(productId);
				listProduct.add(product);
			}
			CategoryResponseDTO categoryDTO = categoryConvert.forCategoryResponseDTO(category, listProduct);
			listCategoryDTO.add(categoryDTO);
		}
		
		return listCategoryDTO;
	}
	
	public Category findById(String id) {
		Optional<Category> obj = repository.findById(id);
		return obj.orElseThrow();
	}
}
