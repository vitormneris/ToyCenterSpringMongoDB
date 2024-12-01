package br.edu.toycenter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.edu.toycenter.controller.mapper.CategoryMapper;
import br.edu.toycenter.controller.dto.request.CategoryRequestDTO;
import br.edu.toycenter.controller.dto.response.CategoryResponseDTO;
import br.edu.toycenter.service.exceptions.DatabaseException;
import br.edu.toycenter.service.exceptions.InvalidFormatException;
import br.edu.toycenter.service.exceptions.ResourceNotFoundException;
import br.edu.toycenter.model.entities.Category;
import br.edu.toycenter.model.entities.Product;
import br.edu.toycenter.model.repositories.CategoryRepository;
import br.edu.toycenter.model.repositories.ProductRepository;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryRepository repository;
	private final ProductRepository productRepository;
	private final CategoryMapper categoryMapper;
	private final UploadService  uploadService;
	private final ValidationService validationService;

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
            return categoryToCategoryResponseDTO(obj.orElseThrow());
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		} 
	}
	
	public CategoryResponseDTO insert(CategoryRequestDTO categoryRequestDTO) {
		try {
			Category category = categoryMapper.forCategory(categoryRequestDTO);
			category.setId(null);
			validationService.checkFields(category);
			Category categoryInserted = repository.save(category);
			return categoryToCategoryResponseDTO(categoryInserted);
		} catch (InvalidFormatException e) {
			throw new InvalidFormatException(e.getMessage());
		}
	}
	
	public CategoryResponseDTO update(String id, CategoryRequestDTO categoryRequestDTO) {
		try {
			Category category = categoryMapper.forCategory(categoryRequestDTO);
			Optional<Category> obj = repository.findById(id);
			updateData(obj.get(), category);
			category.setId(id);
			validationService.checkFields(obj.get());
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

			isEqualsOne(obj.get().getProductsId());
			
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
		obj.setImage((category.getImage() == null) ? obj.getImage() : category.getImage());
		obj.setProductsId((category.getProductsId() == null) ? obj.getProductsId() : category.getProductsId());

	}

	public CategoryRequestDTO categoryDTOWithImage(CategoryRequestDTO categoryRequestDTO, MultipartFile file) {
        return new CategoryRequestDTO(
                categoryRequestDTO.id(),
                categoryRequestDTO.name(),
                uploadService.uploadImage(file, "category"),
                categoryRequestDTO.productsId());
	}
	
	public CategoryResponseDTO categoryToCategoryResponseDTO(Category category) {
		List<Product> listProduct = new ArrayList<>();

		if (category.getProductsId() != null) {
			for (String productId : category.getProductsId()) {
				Optional<Product> obj = productRepository.findById(productId);

				listProduct.add(obj.orElseThrow());
			}
		}

        return categoryMapper.forCategoryResponseDTO(category, listProduct);
	}

	private void isEqualsOne(List<String> listString) throws DatabaseException {
		if (listString.size() == 1)
			throw new DatabaseException("Can't possible delete to category, because he is associate with only a product.");
	}
}
