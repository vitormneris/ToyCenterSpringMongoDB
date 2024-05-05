package br.edu.toycenter.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.toycenter.api.convert.ProductConvert;
import br.edu.toycenter.api.request.ProductRequestDTO;
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
	
	public ProductResponseDTO insert(ProductRequestDTO productRequestDTO) {
		Product product = productConvert.forProduct(productRequestDTO);
		Product productInserted = repository.save(product);
		return productToProductResponseDTO(productInserted);
	}
	
	public ProductResponseDTO update(String id, ProductRequestDTO productRequestDTO) {
		Product product = productConvert.forProduct(productRequestDTO);
		Optional<Product> obj = repository.findById(id);
		updateData(obj.get(), product);
		Product productUpdated = repository.save(obj.get());
		return productToProductResponseDTO(productUpdated);
	}
	
	@Transactional
	public void delete(String id) {
		Optional<Product> obj = repository.findById(id);

		repository.delete(obj.get());
	}
	
	private void updateData(Product obj, Product product) {
		obj.setName(product.getName());
		obj.setBrand(product.getBrand());
		obj.setPrice(product.getPrice());
		obj.setDescription(product.getDescription());
		obj.setDetails(product.getDetails());
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
