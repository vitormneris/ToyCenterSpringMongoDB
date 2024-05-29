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

import br.edu.toycenter.api.convert.ClientConvert;
import br.edu.toycenter.api.request.ClientRequestDTO;
import br.edu.toycenter.api.response.ClientResponseDTO;
import br.edu.toycenter.business.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/client")
public class ClientWebController {

    @Autowired
    ClientService service;

    @Autowired
    ClientRequestDTO clientDTO;

    @Autowired
    ClientConvert clientConvert;
    
	@Autowired
	private HttpServletRequest request;
    
	@GetMapping("/findAll")
	public String findAll(Model model) {
		if (!administratorIsLogged()) return  "redirect:/administrator/login";
		List<ClientResponseDTO> listClientDTO = service.findAll();
		model.addAttribute("listClientDTO", listClientDTO);
		return "/client/findAll";
	}
	
	@GetMapping("/insert")
	public String insert(Model model) {
		if (!administratorIsLogged()) return  "redirect:/administrator/login";
		model.addAttribute("clientDTO", clientDTO);
		return "/client/insert";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("clientDTO", clientDTO);
		return "/signupClient";
	}

    @GetMapping("/update/{clientId}")
    public String update(Model model, @PathVariable String clientId) {
		if (!administratorIsLogged()) return  "redirect:/administrator/login";
        ClientResponseDTO clientResponseDTO = service.findById(clientId);
        clientDTO = clientConvert.forClientRequestDTO(clientResponseDTO);
        model.addAttribute("clientDTO", clientDTO);
        return "/client/update";
    }
    
	@GetMapping("/delete/{id}")
	public String delete(Model model, @PathVariable("id") String id) {
		if (!administratorIsLogged()) return  "redirect:/administrator/login";
		System.out.println(id);
		model.addAttribute("clientId", id);
		return "/client/delete";
	}

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("clientDTO", clientDTO);
        return "loginClient";
    }
    
	@PostMapping("/insert")
	public String insert(@ModelAttribute("clientDTO") ClientRequestDTO clientDTO) {		
		service.insert(clientDTO);
		return "redirect:/client/findAll";
	}
	
	@PostMapping("/insertClient")
	public String insertClient(@ModelAttribute("clientDTO") ClientRequestDTO clientDTO) {		
		service.insert(clientDTO);
		return "redirect:/client/login";
	}

    @PostMapping("/login")
    public String login(@ModelAttribute ClientRequestDTO clientRequestDTO, HttpSession session) {

        if (service.login(clientRequestDTO)) {
            ClientResponseDTO clientResponseDTO = service.findByEmail(clientRequestDTO.email());
            session.setAttribute("clientId", clientResponseDTO.id());
            return "redirect:/product";
        }
        return "redirect:/client/login";
    }
    
	public String update(@ModelAttribute("clientDTO") ClientRequestDTO clientDTO) {		
		service.insert(clientDTO);
		return "redirect:/client/findAll";
	}
	
	@PutMapping("/update/{id}")
	public String update(@ModelAttribute("clientDTO") ClientRequestDTO clientDTO, @PathVariable String id) {
    	service.update(id, clientDTO);
		return "redirect:/client/findAll";
	}
	
	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable String id) {
		service.delete(id);
		return "redirect:/client/findAll";
	}
	
	private boolean administratorIsLogged() {
		HttpSession session = request.getSession();
		String adminsitratorId = (String) session.getAttribute("administratorId");
		if (adminsitratorId == null) return false;
		return true;
	}
}
