package br.edu.toycenter.api.web;

import java.util.ArrayList;
import java.util.List;

import br.edu.toycenter.api.response.CategoryResponseDTO;
import br.edu.toycenter.business.CategoryService;
import br.edu.toycenter.infrastructure.entities.Category;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.edu.toycenter.api.convert.ProductConvert;
import br.edu.toycenter.api.request.ProductRequestDTO;
import br.edu.toycenter.api.response.ProductResponseDTO;
import br.edu.toycenter.business.ProductService;

@Controller
@RequestMapping(value = "/product")
public class ProductWebController {
	
	@Autowired
	ProductService service;

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductConvert convert;
	
	@Autowired
	ProductRequestDTO productRequestDTO;

	@Autowired
	private HttpServletRequest request;

	@GetMapping()
	public String AllProducts(Model model) {
		List<ProductResponseDTO> listProductDTO = service.findAll();
		model.addAttribute("listProductDTO", listProductDTO);
		return "/products";
	}

	@GetMapping("/oneProduct/{productId}")
	public String oneProduct(Model model, @PathVariable String productId) {
		ProductResponseDTO productDTO = service.findById(productId);
		model.addAttribute("productDTO", productDTO);
		return "/product";
	}

	@GetMapping("/productsByCategory/{categoryId}")
	public String productsByCategory(Model model, @PathVariable String categoryId) {
		List<ProductResponseDTO> listProductDTO = service.findAll();
		CategoryResponseDTO categoryResponseDTO = categoryService.findById(categoryId);

		List<ProductResponseDTO> responseDTOByCategoryList = new ArrayList<>();
		for (ProductResponseDTO requestDTO : listProductDTO) {
			for (Category cat : requestDTO.categories()) {
				if (cat.getId().equals(categoryId)) {
					responseDTOByCategoryList.add(requestDTO);
				}
			}
		}

		model.addAttribute("categoryName", categoryResponseDTO.name());

		model.addAttribute("listProductDTO", responseDTOByCategoryList);
		return "/productsByCategory";
	}

	@GetMapping("/findAll")
	public String findAll(Model model) {
		if (!administratorIsLogged()) return  "redirect:/administrator/login";
		
		List<ProductResponseDTO> productResponseDTOList = service.findAll();
		model.addAttribute("listProductDTO", productResponseDTOList);
		return "/product/findAll";
	}
	
	@GetMapping("/insert")
	public String insert(Model model) {
		if (!administratorIsLogged()) return  "redirect:/administrator/login";

		
		List<CategoryResponseDTO> categoryResponseDTOList = categoryService.findAll();

		model.addAttribute("productDTO", productRequestDTO);
		model.addAttribute("listCategoryDTO", categoryResponseDTOList);

		return "/product/insert";
	}
	
	@GetMapping("/update/{id}")
	public String update(Model model, @PathVariable("id") String id) {
		if (!administratorIsLogged()) return  "redirect:/administrator/login";

		
		ProductResponseDTO productResponseDTO = service.findById(id);
		List<CategoryResponseDTO> categoryResponseDTOList = categoryService.findAll();

		model.addAttribute("productRequestDTO", convert.forProductRequestDTO(productResponseDTO));
		model.addAttribute("listCategoryDTO", categoryResponseDTOList);

		return "/product/update";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(Model model, @PathVariable("id") String id) {
		if (!administratorIsLogged()) return  "redirect:/administrator/login";
		
		model.addAttribute("productId", id);
		return "/product/delete";
	}
	
	@PostMapping("/insert")
	public String insert(@ModelAttribute("productDTO") ProductRequestDTO productDTO, @RequestParam("imageFile") MultipartFile file) {		
		ProductRequestDTO productDTOWithImage = service.productDTOWithImage(productDTO, file);
		service.insert(productDTOWithImage);
		return "redirect:/product/findAll";
	}
	
	@PutMapping("/update/{id}")
	public String update(@ModelAttribute("productDTO") ProductRequestDTO productDTO, @RequestParam("imageFile") MultipartFile file, @PathVariable String id) {		
    	if (file.isEmpty()) {
    		service.update(id, productDTO);
    	} else {
    		ProductRequestDTO productDTOWithImage = service.productDTOWithImage(productDTO, file);
    		service.update(id, productDTOWithImage);
    	}
    	
		return "redirect:/product/findAll";
	}
	
	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable String id) {
		
		service.delete(id);
		return "redirect:/product/findAll";
	}
	
	private boolean administratorIsLogged() {
		HttpSession session = request.getSession();
		String adminsitratorId = (String) session.getAttribute("administratorId");
		if (adminsitratorId == null) return false;
		return true;
	}
}
