package br.edu.toycenter.api.web;

import java.util.List;

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

import br.edu.toycenter.api.request.ProductRequestDTO;
import br.edu.toycenter.api.response.ProductResponseDTO;
import br.edu.toycenter.business.ProductService;

@Controller
@RequestMapping(value = "/product")
public class ProductWebController {
	
	@Autowired
	ProductService service;
	
	@GetMapping()
	public String findAll(Model model) {
		List<ProductResponseDTO> listProductDTO = service.findAll();
		model.addAttribute("listProductDTO", listProductDTO);
		return "product/findAll";
	}
	
	@GetMapping("/insert")
	public String insert(Model model) {
		ProductRequestDTO productDTO = new ProductRequestDTO(null, null, null, null, null, null);
		model.addAttribute("productDTO", productDTO);
		return "product/insert";
	}
	
	@GetMapping("/update/{id}")
	public String update(Model model, @PathVariable("id") String id) {
		ProductRequestDTO productDTO = new ProductRequestDTO(null, null, null, null, null, null);
		model.addAttribute("productDTO", productDTO);
		model.addAttribute("productId", id);
		return "product/update";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(Model model, @PathVariable("id") String id) {
		model.addAttribute("productId", id);
		return "product/delete";
	}
	
	@PostMapping("/insert")
	public String insert(@ModelAttribute("productDTO") ProductRequestDTO productDTO) {
		service.insert(productDTO);
		return "redirect:/product";
	}
	
	@PutMapping("/update/{id}")
	public String update(@ModelAttribute("productDTO") ProductRequestDTO productDTO, @PathVariable String id) {
		service.update(id, productDTO);
		return "redirect:/product";
	}
	
	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable String id) {
		service.delete(id);
		return "redirect:/product";
	}
}
