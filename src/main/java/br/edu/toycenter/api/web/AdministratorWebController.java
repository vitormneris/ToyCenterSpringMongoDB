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

import br.edu.toycenter.api.convert.AdministratorConvert;
import br.edu.toycenter.api.request.AdministratorRequestDTO;
import br.edu.toycenter.api.response.AdministratorResponseDTO;
import br.edu.toycenter.business.AdministratorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/administrator")
public class AdministratorWebController {

    @Autowired
    AdministratorService service;

    @Autowired
    AdministratorRequestDTO administratorDTO;

    @Autowired
    AdministratorConvert administratorConvert;
    
	@Autowired
	private HttpServletRequest request;
    
	@GetMapping("/findAll")
	public String findAll(Model model) {
		if (!administratorIsLogged()) return  "redirect:/administrator/login";
		List<AdministratorResponseDTO> listAdministratorDTO = service.findAll();
		model.addAttribute("listAdministratorDTO", listAdministratorDTO);
		return "/administrator/findAll";
	}
	
	@GetMapping("/insert")
	public String insert(Model model) {
		if (!administratorIsLogged()) return  "redirect:/administrator/login";
		model.addAttribute("administratorDTO", administratorDTO);
		return "/administrator/insert";
	}

    @GetMapping("/update/{administratorId}")
    public String update(Model model, @PathVariable String administratorId) {
		if (!administratorIsLogged()) return  "redirect:/administrator/login";
        AdministratorResponseDTO administratorResponseDTO = service.findById(administratorId);
        administratorDTO = administratorConvert.forAdministratorRequestDTO(administratorResponseDTO);
        model.addAttribute("administratorDTO", administratorDTO);
        return "/administrator/update";
    }
    
	@GetMapping("/delete/{id}")
	public String delete(Model model, @PathVariable("id") String id) {
		if (!administratorIsLogged()) return  "redirect:/administrator/login";
		model.addAttribute("administratorId", id);
		return "/administrator/delete";
	}

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("administratorDTO", administratorDTO);
        return "/loginAdministrator";
    }
    
	@PostMapping("/insert")
	public String insert(@ModelAttribute("administratorDTO") AdministratorRequestDTO administratorDTO) {		
		service.insert(administratorDTO);
		return "redirect:/administrator/findAll";
	}

    @PostMapping("/login")
    public String login(@ModelAttribute AdministratorRequestDTO administratorRequestDTO, HttpSession session) {

        if (service.login(administratorRequestDTO)) {
            AdministratorResponseDTO administratorResponseDTO = service.findByEmail(administratorRequestDTO.email());
            session.setAttribute("administratorId", administratorResponseDTO.id());
            return "redirect:/product/findAll";
        }
        return "redirect:/administrator/login";
    }
    
	public String update(@ModelAttribute("administratorDTO") AdministratorRequestDTO administratorDTO) {		
		service.insert(administratorDTO);
		return "redirect:/administrator/findAll";
	}
	
	@PutMapping("/update/{id}")
	public String update(@ModelAttribute("administratorDTO") AdministratorRequestDTO administratorDTO, @PathVariable String id) {
    	service.update(id, administratorDTO);
		return "redirect:/administrator/findAll";
	}
	
	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable String id) {
		service.delete(id);
		return "redirect:/administrator/findAll";
	}
	
	private boolean administratorIsLogged() {
		HttpSession session = request.getSession();
		String adminsitratorId = (String) session.getAttribute("administratorId");
		if (adminsitratorId == null) return false;
		return true;
	}
}
