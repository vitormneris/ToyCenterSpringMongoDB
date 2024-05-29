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

import br.edu.toycenter.api.convert.OrderConvert;
import br.edu.toycenter.api.request.OrderRequestDTO;
import br.edu.toycenter.api.response.OrderResponseDTO;
import br.edu.toycenter.business.OrderService;

@Controller
@RequestMapping(value = "/order")
public class OrderWebController {
	
	@Autowired
	OrderService service;
	
	@Autowired
	OrderConvert convert;
	
	@Autowired
	OrderRequestDTO orderDTO;
	
	@GetMapping()
	public String findAll(Model model) {
		List<OrderResponseDTO> listOrderDTO = service.findAll();
		model.addAttribute("listOrderDTO", listOrderDTO);
		return "order/findAll";
	}
	
	@GetMapping("/insert")
	public String insert(Model model) {
		model.addAttribute("orderDTO", orderDTO);
		return "order/insert";
	}
	
	@GetMapping("/update/client/{clientId}")
	public String update(Model model, @PathVariable("clientId") String clientId) {		
		OrderResponseDTO responseDTO = service.findByClientId(clientId);
		orderDTO = convert.forOrderRequestDTO(responseDTO);
		
		model.addAttribute("orderRequestDTO", orderDTO);
		return "order/update";
	}
	
	@GetMapping("/delete/client/{clientId}")
	public String delete(Model model, @PathVariable("clientId") String clientId) {
		model.addAttribute("clientId", clientId);
		return "order/delete";
	}
	
	@PostMapping("/insert")
	public String insert(@ModelAttribute("orderDTO") OrderRequestDTO orderDTO) {
		service.insert(orderDTO);
		return "redirect:/order";
	}
	
	@PutMapping("/update/client/{clientId}")
	public String updateByClientId(@ModelAttribute("orderDTO") OrderRequestDTO orderDTO, @PathVariable String clientId) {
		service.updateByClientId(clientId, orderDTO);
		return "redirect:/order";
	}
	
	@DeleteMapping("/delete/client/{clientId}")
	public String delete(@PathVariable String clientId) {
		service.deleteByClientId(clientId);
		return "redirect:/order";
	}
}
