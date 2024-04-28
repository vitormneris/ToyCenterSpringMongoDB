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
import br.edu.toycenter.infrastructure.repositories.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryService serviceCategory;
	
	@Autowired
	private ProductConvert productConvert;
	
	public List<ProductResponseDTO> findAll() {
		List<Product> listProduct = repository.findAll();
		
		List<ProductResponseDTO> listProductDTO = new ArrayList<>();
		
		for (Product product : listProduct) {
			List<Category> listCategory = new ArrayList<>();
			for (String id : product.getCategoriesId()) {
				Category category = serviceCategory.findById(id);
				listCategory.add(category);
			}
			ProductResponseDTO productDTO = productConvert.forProductResponseDTO(product, listCategory);
			listProductDTO.add(productDTO);
		}
		
		return listProductDTO;
	}
	
	public Product findAllById(String id) {
		Optional<Product> obj = repository.findById(id);
		return obj.orElseThrow();
	}
}
