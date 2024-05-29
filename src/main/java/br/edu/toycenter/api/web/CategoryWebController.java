package br.edu.toycenter.api.web;

import br.edu.toycenter.api.convert.ProductConvert;
import br.edu.toycenter.api.request.ProductRequestDTO;
import br.edu.toycenter.api.response.ProductResponseDTO;
import br.edu.toycenter.business.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping(value = "/product")
public class CategoryWebController {
	
	@Autowired
	ProductService service;
	
	@Autowired
	ProductConvert convert;
	
	@Autowired
	ProductRequestDTO productRequestDTO;

	@GetMapping()
	public String AllProducts(Model model) {
		List<ProductResponseDTO> listProductDTO = service.findAll();
		model.addAttribute("listProductDTO", listProductDTO);
		return "/products";
	}

	@GetMapping("/findAll")
	public String findAll(Model model) {
		List<ProductResponseDTO> listProductDTO = service.findAll();
		model.addAttribute("listProductDTO", listProductDTO);
		return "product/findAll";
	}
	
	@GetMapping("/insert")
	public String insert(Model model) {
		model.addAttribute("productDTO", productRequestDTO);
		return "product/insert";
	}
	
	@GetMapping("/update/{id}")
	public String update(Model model, @PathVariable("id") String id) {
		ProductResponseDTO productResponseDTO = service.findById(id);
		model.addAttribute("productRequestDTO", convert.forProductRequestDTO(productResponseDTO));
		return "product/update";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(Model model, @PathVariable("id") String id) {
		model.addAttribute("productId", id);
		return "product/delete";
	}
	
	@PostMapping("/insert")
	public String insert(@ModelAttribute("productDTO") ProductRequestDTO productDTO, @RequestParam("imageFile") MultipartFile file) {		
		ProductRequestDTO productDTOWithImage = service.productDTOWithImage(productDTO, file);
		service.insert(productDTOWithImage);
		return "redirect:/product";
	}
	
	@PutMapping("/update/{id}")
	public String update(@ModelAttribute("productDTO") ProductRequestDTO productDTO, @RequestParam("imageFile") MultipartFile file, @PathVariable String id) {		
    	if (file.isEmpty()) {
    		service.update(id, productDTO);
    	} else {
    		ProductRequestDTO productDTOWithImage = service.productDTOWithImage(productDTO, file);
    		service.update(id, productDTOWithImage);
    	}
    	
		return "redirect:/product";
	}
	
	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable String id) {
		service.delete(id);
		return "redirect:/product";
	}
}
