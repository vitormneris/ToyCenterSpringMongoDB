package br.edu.toycenter.business;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.toycenter.api.convert.CategoryConvert;
import br.edu.toycenter.api.request.CategoryRequestDTO;
import br.edu.toycenter.api.response.CategoryResponseDTO;
import br.edu.toycenter.business.exceptions.DatabaseException;
import br.edu.toycenter.business.exceptions.InvalidFormatException;
import br.edu.toycenter.business.exceptions.ResourceNotFoundException;
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
		try {
			Optional<Category> obj = repository.findById(id);
			CategoryResponseDTO categoryResponseDTO = categoryToCategoryResponseDTO(obj.orElseThrow());
			return categoryResponseDTO;
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		} 
	}
	
	public CategoryResponseDTO insert(CategoryRequestDTO categoryRequestDTO) {
		try {
			Category category = categoryConvert.forCategory(categoryRequestDTO);
			category.setId(null);
			checkFields(category);
			Category categoryInserted = repository.save(category);
			return categoryToCategoryResponseDTO(categoryInserted);
		} catch (InvalidFormatException e) {
			throw new InvalidFormatException(e.getMessage());
		}
	}
	
	public CategoryResponseDTO update(String id, CategoryRequestDTO categoryRequestDTO) {
		try {
			Category category = categoryConvert.forCategory(categoryRequestDTO);
			Optional<Category> obj = repository.findById(id);
			updateData(obj.get(), category);
			category.setId(id);
			checkFields(obj.get());
			Category categoryUpdated = repository.save(obj.get());
			return categoryToCategoryResponseDTO(categoryUpdated);
		}catch (InvalidFormatException e) {
			throw new InvalidFormatException(e.getMessage());
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		} 
	}
	
	@Transactional
	public void delete(String id) {
		try {
			
			Optional<Category> obj = repository.findById(id);
		
			isLessThan2(obj.get().getProductsId());
			
			for (String productsId : obj.get().getProductsId()) {
				Optional<Product> objProduct = productRepository.findById(productsId);
				
				List<String> listCategoriesId = objProduct.get().getCategoriesId();
				
				objProduct.get().setCategoriesId(new ArrayList<>());

				for (String objCategoriesId : listCategoriesId) {
					if (!(obj.get().getId().equals(objCategoriesId))) {
						objProduct.get().getCategoriesId().add(objCategoriesId);
					}
				}
				
				productRepository.save(objProduct.get());
				
			}
			repository.delete(obj.get());
			
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		} catch (DatabaseException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	private void updateData(Category obj, Category category) {
		obj.setName((category.getName() == null) ? obj.getName() : category.getName());
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
	
	private void checkFields(Category category) throws InvalidFormatException {
		if (category == null) throw new InvalidFormatException("The fields can not be null.");
		
		isNullOrBlank(category.getName());
	}
	
	private void isNullOrBlank(String string) throws InvalidFormatException {
		if (string == null || string.isBlank()) 
			throw new InvalidFormatException("The name can not be null.");
	}
	
	private void isLessThan2(List<String> listString) throws DatabaseException {
		if (!(listString.size() > 1)) 
			throw new DatabaseException("Can't possible delete to category, because he is associate with only a product.");
	}
}
