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
import br.edu.toycenter.infrastructure.repositories.ProductRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryConvert categoryConvert;
	
	public List<CategoryResponseDTO> findAll() {
		List<Category> listCategory = repository.findAll();
		List<CategoryResponseDTO> listCategoryDTO = new ArrayList<>();
		
		for (Category category : listCategory) {
			listCategoryDTO.add(categoryToCategoryResponseDTO(category));
		}
		
		return listCategoryDTO;
	}
	
	public CategoryResponseDTO findById(String id) {
		Optional<Category> obj = repository.findById(id);
		CategoryResponseDTO categoryResponseDTO = categoryToCategoryResponseDTO(obj.orElseThrow());
		return categoryResponseDTO;
	}
	
	public CategoryResponseDTO categoryToCategoryResponseDTO(Category category) {
		List<Product> listProduct = new ArrayList<>();
		
		for (String productId : category.getProductsId()) {
			Optional<Product> obj = productRepository.findById(productId);
			listProduct.add(obj.orElseThrow());
		}
		
		CategoryResponseDTO categoryDTO = categoryConvert.forCategoryResponseDTO(category, listProduct);
		return categoryDTO;
	}
}
