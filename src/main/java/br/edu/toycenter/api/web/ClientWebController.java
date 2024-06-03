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

	@GetMapping("/findClient")
	public String findAClient(Model model) {
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/client/findClient"));
		if (!clientIsLogged()) return  "redirect:/client/login";
		String clientId = (String) session.getAttribute("clientId");
		ClientResponseDTO clientDTO = service.findById(clientId);
		model.addAttribute("clientDTO", clientDTO);
		return "/clientPerfil/findClient";
	}

	@GetMapping("/updateByClient/{clientId}")
	public String updateByClient(Model model, @PathVariable String clientId) {
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/client/updateByClient/" + clientId));
		if (!clientIsLogged()) return  "redirect:/client/login";
		ClientResponseDTO clientResponseDTO = service.findById(clientId);
		ClientRequestDTO clientDTO = clientConvert.forClientRequestDTO(clientResponseDTO);
		model.addAttribute("clientDTO", clientDTO);
		return "/clientPerfil/update";
	}

//	@GetMapping("/deleteByClient/{clientId}")
//	public String deleteByClient(Model model, @PathVariable("clientId") String clientId) {
//		HttpSession	session = request.getSession();
//		session.setAttribute("previousURI", ("/client/deleteByClient/" + clientId));
//		if (!clientIsLogged()) return  "redirect:/client/login";
//        return null;
//    }

	@GetMapping("/findAll")
	public String findAll(Model model) {
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/client/findAll"));
		if (!administratorIsLogged()) return  "redirect:/administrator/login";
		List<ClientResponseDTO> listClientDTO = service.findAll();
		model.addAttribute("listClientDTO", listClientDTO);
		return "/client/findAll";
	}
	
	@GetMapping("/insert")
	public String insert(Model model) {
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/client/insert"));
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
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/client/update/" + clientId));
		if (!administratorIsLogged()) return  "redirect:/administrator/login";
        ClientResponseDTO clientResponseDTO = service.findById(clientId);
		ClientRequestDTO clientRequestDTO = clientConvert.forClientRequestDTO(clientResponseDTO);
        model.addAttribute("clientDTO", clientRequestDTO);
        return "/client/update";
    }
    
	@GetMapping("/delete/{clientId}")
	public String delete(Model model, @PathVariable("clientId") String clientId) {
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/client/delete/" + clientId));
		if (!administratorIsLogged()) return  "redirect:/administrator/login";
		model.addAttribute("clientId", clientId);
		return "/client/delete";
	}

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("clientDTO", clientDTO);
        return "loginClient";
    }

	@GetMapping("/logout")
	public String logout(Model model) {
		HttpSession session = request.getSession();
		session.removeAttribute("clientId");
		return "redirect:/product";
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
    public String login(@ModelAttribute ClientRequestDTO clientRequestDTO, HttpSession session, HttpServletRequest request) {
        if (service.login(clientRequestDTO)) {
            ClientResponseDTO clientResponseDTO = service.findByEmail(clientRequestDTO.email());
            session.setAttribute("clientId", clientResponseDTO.id());
            return "redirect:" + ((session.getAttribute("previousURI") == null) ? ("/product") : (session.getAttribute("previousURI")));
        }
        return "redirect:/client/login";
    }

	@PutMapping("/updateByClient/{id}")
	public String updateByClient(@ModelAttribute("clientDTO") ClientRequestDTO clientDTO, @PathVariable String id) {
		service.update(id, clientDTO);
		return "redirect:/client/findClient";
	}

	@DeleteMapping("/deleteByClient/{id}")
	public String deleteByClient(Model model, @PathVariable String id) {
		service.delete(id);
		logout(model);
		return "redirect:/client/logout";
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

	private boolean clientIsLogged() {
		HttpSession session = request.getSession();
		String clientId = (String) session.getAttribute("clientId");
		if (clientId == null) return false;
		return true;
	}
	
	private boolean administratorIsLogged() {
		HttpSession session = request.getSession();
		String adminsitratorId = (String) session.getAttribute("administratorId");
		if (adminsitratorId == null) return false;
		return true;
	}
}
