package br.edu.toycenter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.edu.toycenter.controller.mapper.ProductMapper;
import br.edu.toycenter.controller.dto.request.ProductRequestDTO;
import br.edu.toycenter.controller.dto.response.ProductResponseDTO;
import br.edu.toycenter.service.exceptions.InvalidFormatException;
import br.edu.toycenter.service.exceptions.ResourceNotFoundException;
import br.edu.toycenter.model.entities.Category;
import br.edu.toycenter.model.entities.Product;
import br.edu.toycenter.model.repositories.CategoryRepository;
import br.edu.toycenter.model.repositories.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository repository;
	private final CategoryRepository categoryRepository;
	private final ProductMapper productMapper;
	private final UploadService  uploadService;
	private final ValidationService validationService;
	
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
            return productToProductResponseDTO(obj.orElseThrow());
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}

	public ProductResponseDTO insert(ProductRequestDTO productRequestDTO) {
		try {
			Product product = productMapper.forProduct(productRequestDTO);
			product.setId(null);
			validationService.checkFields(product);
			Product productInserted = repository.save(product);
			return productToProductResponseDTO(productInserted);
		} catch (InvalidFormatException e) {
			throw new InvalidFormatException(e.getMessage());
		}
	}
	
	public ProductResponseDTO update(String id, ProductRequestDTO productRequestDTO) {
		try {
			Product product = productMapper.forProduct(productRequestDTO);
			Optional<Product> obj = repository.findById(id);
			updateData(obj.get(), product);
			obj.get().setId(id);
			validationService.checkFields(obj.get());
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
	
	public ProductRequestDTO productDTOWithImage(ProductRequestDTO productDTO, MultipartFile file) {
        return new ProductRequestDTO(
                productDTO.id(),
                productDTO.name(),
                uploadService.uploadImage(file, "product"),
                productDTO.brand(),
                productDTO.price(),
                productDTO.description(),
                productDTO.details(),
                productDTO.categoriesId());
	}
	
	private void updateData(Product obj, Product product) {
		obj.setName((product.getName() == null) ? obj.getName() : product.getName());
		obj.setImage((product.getImage() == null) ? obj.getImage() : product.getImage());
		obj.setBrand((product.getBrand() == null) ? obj.getBrand() : product.getBrand());
		obj.setPrice((product.getPrice() == null) ? obj.getPrice() : product.getPrice());
		obj.setDescription((product.getDescription() == null) ? obj.getDescription() : product.getDescription());
		obj.setDetails((product.getDetails() == null) ? obj.getDetails() : product.getDetails());
		obj.setCategoriesId((product.getCategoriesId() == null) ? obj.getCategoriesId() : product.getCategoriesId());
	}
	
	private ProductResponseDTO productToProductResponseDTO(Product product) {
		List<Category> listCategory = new ArrayList<>();
		
		for (String id : product.getCategoriesId()) {
			Optional<Category> obj = categoryRepository.findById(id);
			listCategory.add(obj.orElseThrow());
		}

        return productMapper.forProductResponseDTO(product, listCategory);
	}
}
