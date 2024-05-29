package br.edu.toycenter.business;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.edu.toycenter.api.convert.ProductConvert;
import br.edu.toycenter.api.request.ProductRequestDTO;
import br.edu.toycenter.api.response.ProductResponseDTO;
import br.edu.toycenter.business.exceptions.InternalErrorException;
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
	
	public ProductRequestDTO productDTOWithImage(ProductRequestDTO productDTO, MultipartFile file) {
		ProductRequestDTO newProductDTO = new ProductRequestDTO(
				productDTO.id(),
				productDTO.name(), 
				uploadImage(file),
				productDTO.brand(), 
				productDTO.price(), 
				productDTO.description(), 
				productDTO.details(),
				productDTO.categoriesId());
		return newProductDTO;
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
	
    private String uploadImage(MultipartFile file) throws InvalidFormatException, InternalError {
    	String url = "/home/vitor/Documents/workspace-spring-tool-suite-4-4.22.1.RELEASE/ToyCenterSpringMongoDB/src/main/resources/static/images/product/";
    	
    	if (file.isEmpty()) 
    		throw new InvalidFormatException("The image can not be null.");
    	
        try {

            byte[] bytes = file.getBytes();
            Path path = Paths.get(url + file.getOriginalFilename());
            Files.write(path, bytes);

        } catch (IOException e) {
        	throw new InternalErrorException("Unable to save image");
        }
        
        return "/images/product" + file.getOriginalFilename();
    }
	
	private ProductResponseDTO productToProductResponseDTO(Product product) {
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
		isNullOrBlank(product.getCategoriesId());

	}
	
	private void isNullOrBlank(String string) throws InvalidFormatException {
		if (string == null || string.isBlank()) 
			throw new InvalidFormatException("The fields can not be null.");
	}
	
	private void isNullOrBlank(Double doub) throws InvalidFormatException {
		if (doub == null || doub <= 0f) 
			throw new InvalidFormatException("This price is not valid.");
	}

	private void isNullOrBlank(List<String> list) throws InvalidFormatException {
		if (list == null || list.size() <= 0)
			throw new InvalidFormatException("The list can not be null.");
	}
}
