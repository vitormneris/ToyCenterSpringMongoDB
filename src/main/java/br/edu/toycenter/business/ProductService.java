package br.edu.toycenter.business;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.toycenter.api.convert.ProductConvert;
import br.edu.toycenter.api.request.ProductRequestDTO;
import br.edu.toycenter.api.response.ProductResponseDTO;
import br.edu.toycenter.business.exceptions.InvalidFormatException;
import br.edu.toycenter.business.exceptions.ResourceNotFoundException;
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
		try {
			Optional<Product> obj = repository.findById(id);
			ProductResponseDTO responseDTO = productToProductResponseDTO(obj.orElseThrow());
			return responseDTO;
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}

	public ProductResponseDTO insert(ProductRequestDTO productRequestDTO) {
		try {
			Product product = productConvert.forProduct(productRequestDTO);
			product.setId(null);
			checkFields(product);
			Product productInserted = repository.save(product);
			return productToProductResponseDTO(productInserted);
		} catch (InvalidFormatException e) {
			throw new InvalidFormatException(e.getMessage());
		}
	}
	
	public ProductResponseDTO update(String id, ProductRequestDTO productRequestDTO) {
		try {
			Product product = productConvert.forProduct(productRequestDTO);
			Optional<Product> obj = repository.findById(id);
			updateData(obj.get(), product);
			obj.get().setId(id);
			checkFields(obj.get());
			Product productUpdated = repository.save(obj.get());
			return productToProductResponseDTO(productUpdated);
		}catch (InvalidFormatException e) {
			throw new InvalidFormatException(e.getMessage());
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		} 
	}
	
	@Transactional
	public void delete(String id) {
		try {
			Optional<Product> obj = repository.findById(id);
	
			for (String categoryId : obj.get().getCategoriesId()) {
				Optional<Category> objCategory = categoryRepository.findById(categoryId);
				
				List<String> listProductsId = objCategory.get().getProductsId();
				objCategory.get().setProductsId(new ArrayList<>());

				for (String objProductId : listProductsId) {
					if (!(obj.get().getId().equals(objProductId))) {
						objCategory.get().getProductsId().add(objProductId);
					}
				}
				
				categoryRepository.save(objCategory.get());
			}
			
			repository.delete(obj.get());
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}
	
	private void updateData(Product obj, Product product) {
		obj.setName((product.getName() == null) ? obj.getName() : product.getName());
		obj.setImage((product.getImage() == null) ? obj.getImage() : product.getImage());
		obj.setBrand((product.getBrand() == null) ? obj.getBrand() : product.getBrand());
		obj.setPrice((product.getPrice() == null) ? obj.getPrice() : product.getPrice());
		obj.setDescription((product.getDescription() == null) ? obj.getDescription() : product.getDescription());
		obj.setDetails((product.getDetails() == null) ? obj.getDetails() : product.getDetails());
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
	
	private void checkFields(Product product) throws InvalidFormatException {
		if (product == null) throw new InvalidFormatException("The fields can not be null.");
		
		isNullOrBlank(product.getName());
		isNullOrBlank(product.getImage());
		isNullOrBlank(product.getBrand());
		isNullOrBlank(product.getPrice());
		isNullOrBlank(product.getDescription());
		isNullOrBlank(product.getDetails());
	}
	
	private void isNullOrBlank(String string) throws InvalidFormatException {
		if (string == null || string.isBlank()) 
			throw new InvalidFormatException("The fields can not be null.");
	}
	
	private void isNullOrBlank(Double doub) throws InvalidFormatException {
		if (doub == null || doub <= 0f) 
			throw new InvalidFormatException("This price is not valid.");
	}
}
