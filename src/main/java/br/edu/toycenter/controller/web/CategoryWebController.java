package br.edu.toycenter.controller.web;

import java.util.List;

import lombok.RequiredArgsConstructor;
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

import br.edu.toycenter.controller.mapper.CategoryMapper;
import br.edu.toycenter.controller.dto.request.CategoryRequestDTO;
import br.edu.toycenter.controller.dto.response.CategoryResponseDTO;
import br.edu.toycenter.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/category")
public class CategoryWebController {
	private final CategoryService service;
	private final CategoryMapper convert;
	private final  HttpServletRequest request;

	@GetMapping()
	public String AllCategorys(Model model) {
		List<CategoryResponseDTO> listCategoryDTO = service.findAll();
		model.addAttribute("listCategoryDTO", listCategoryDTO);
		return "/categories";
	}

	@GetMapping("/findAll")
	public String findAll(Model model) {
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/category/findAll"));
		List<CategoryResponseDTO> listCategoryDTO = service.findAll();
		model.addAttribute("listCategoryDTO", listCategoryDTO);
		return "/category/findAll";
	}

	@GetMapping("/insert")
	public String insert(Model model) {
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/category/insert"));
		model.addAttribute("categoryRequestDTO", new CategoryRequestDTO(null, null, null, null));
		return "/category/insert";
	}
	
	@GetMapping("/update/{id}")
	public String update(Model model, @PathVariable("id") String id) {
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/category/update/" + id));
		CategoryResponseDTO categoryResponseDTO = service.findById(id);
		model.addAttribute("categoryRequestDTO", convert.forCategoryRequestDTO(categoryResponseDTO));
		return "/category/update";
	}

	@GetMapping("/delete/{id}")
	public String delete(Model model, @PathVariable("id") String id) {
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/category/delete/" + id));
		model.addAttribute("categoryId", id);
		return "/category/delete";
	}

	@PostMapping("/insert")
	public String insert(@ModelAttribute("categoryRequestDTO") CategoryRequestDTO categoryDTO, @RequestParam("imageFile") MultipartFile file) {
		CategoryRequestDTO categoryDTOWithImage = service.categoryDTOWithImage(categoryDTO, file);
		service.insert(categoryDTOWithImage);
		return "redirect:/category/findAll";
	}

	@PutMapping("/update/{id}")
	public String update(@ModelAttribute("categoryRequestDTO") CategoryRequestDTO categoryDTO, @RequestParam("imageFile") MultipartFile file, @PathVariable String id) {
    	if (file.isEmpty()) {
    		service.update(id, categoryDTO);
    	} else {
    		CategoryRequestDTO categoryDTOWithImage = service.categoryDTOWithImage(categoryDTO, file);
    		service.update(id, categoryDTOWithImage);
    	}

		return "redirect:/category/findAll";
	}

	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable String id) {
		service.delete(id);
		return "redirect:/category/findAll";
	}
}
