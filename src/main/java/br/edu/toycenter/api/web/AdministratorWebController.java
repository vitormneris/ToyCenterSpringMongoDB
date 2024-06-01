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
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/administrator/findAll"));
		if (!administratorIsLogged()) return  "redirect:/administrator/login";
		List<AdministratorResponseDTO> listAdministratorDTO = service.findAll();
		model.addAttribute("listAdministratorDTO", listAdministratorDTO);
		return "/administrator/findAll";
	}
	
	@GetMapping("/insert")
	public String insert(Model model) {
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/administrator/insert"));
		if (!administratorIsLogged()) return  "redirect:/administrator/login";
		model.addAttribute("administratorDTO", administratorDTO);
		return "/administrator/insert";
	}

    @GetMapping("/update/{administratorId}")
    public String update(Model model, @PathVariable String administratorId) {
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/administrator/update" + administratorId));
		if (!administratorIsLogged()) return  "redirect:/administrator/login";
        AdministratorResponseDTO administratorResponseDTO = service.findById(administratorId);
		AdministratorRequestDTO administratorRequestDTO = administratorConvert.forAdministratorRequestDTO(administratorResponseDTO);
        model.addAttribute("administratorDTO", administratorRequestDTO);
        return "/administrator/update";
    }
    
	@GetMapping("/delete/{id}")
	public String delete(Model model, @PathVariable("id") String id) {
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/administrator/delete" + id));
		if (!administratorIsLogged()) return  "redirect:/administrator/login";
		model.addAttribute("administratorId", id);
		return "/administrator/delete";
	}

	@GetMapping("/team")
	public String equipe(Model model) {
		model.addAttribute("administratorDTO", administratorDTO);
		return "/team";
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

	@GetMapping("/logout")
	public String logout(Model model) {
		HttpSession session = request.getSession();
		session.removeAttribute("administratorId");
		return "redirect:/product";
	}

    @PostMapping("/login")
    public String login(@ModelAttribute AdministratorRequestDTO administratorRequestDTO, HttpSession session, HttpServletRequest request) {

        if (service.login(administratorRequestDTO)) {
            AdministratorResponseDTO administratorResponseDTO = service.findByEmail(administratorRequestDTO.email());
            session.setAttribute("administratorId", administratorResponseDTO.id());
			return "redirect:" + ((session.getAttribute("previousURI") == null) ? ("/administrator/findAll") : (session.getAttribute("previousURI")));
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
