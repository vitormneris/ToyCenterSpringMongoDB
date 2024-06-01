package br.edu.toycenter.api.web;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import br.edu.toycenter.api.request.ClientRequestDTO;
import br.edu.toycenter.api.request.EmailRequestDTO;
import br.edu.toycenter.api.response.OrderItemResponseDTO;
import br.edu.toycenter.infrastructure.entities.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.toycenter.api.convert.AuxOrderConvert;
import br.edu.toycenter.api.convert.OrderConvert;
import br.edu.toycenter.api.request.AuxOrderRequestDTO;
import br.edu.toycenter.api.request.OrderRequestDTO;
import br.edu.toycenter.api.response.ClientResponseDTO;
import br.edu.toycenter.api.response.OrderResponseDTO;
import br.edu.toycenter.business.ClientService;
import br.edu.toycenter.business.EmailService;
import br.edu.toycenter.business.OrderService;
import br.edu.toycenter.infrastructure.entities.Order;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/order")
public class OrderWebController {
	
	@Autowired
	OrderService service;
	
	@Autowired
	ClientService clientService;

	@Autowired
	EmailService emailService;
	
	@Autowired
	OrderConvert convert;

	@Autowired
	AuxOrderConvert auxOrderConvert;

	@Autowired
	OrderRequestDTO orderDTO;

	@Autowired
	private HttpServletRequest request;
	
	@GetMapping()
	public String findByClientId(Model model) {
		if (!clientIsLogged()) return  "redirect:/client/login";

		HttpSession session = request.getSession();
		String clientId = (String) session.getAttribute("clientId");
		List<OrderResponseDTO> listResponseDTO = service.findByClientId(clientId);
		int lastIndex = listResponseDTO.size() - 1;
		listResponseDTO.remove(lastIndex);
		model.addAttribute("orderDTOList", listResponseDTO);
		return "order/findByClientId";
	}
	
	@GetMapping("/insert/{productId}")
	public String insert(Model model, @PathVariable("productId") String productId) {
		if (!clientIsLogged()) return  "redirect:/client/login";

		HttpSession session = request.getSession();
		String clientId = (String) session.getAttribute("clientId");

		ClientResponseDTO clientDTO = clientService.findById(clientId);
		List<String> listOrderId = new ArrayList<>();
		for (Order order : clientDTO.orders()) {
			listOrderId.add(order.getId());
		}
		
		AuxOrderRequestDTO auxOrderRequestDTO = new AuxOrderRequestDTO(null, clientId, 0, productId);
		
		model.addAttribute("productId", productId);
		model.addAttribute("listId", listOrderId);
		model.addAttribute("orderRequestDTO", auxOrderRequestDTO);
		return "order/insert";
	}
	
	@GetMapping("/update/client/{clientId}/{orderId}/{index}")
	public String update(Model model, @PathVariable("clientId") String clientId, @PathVariable("orderId") String orderId, @PathVariable("index") int index) {
		if (!clientIsLogged()) return  "redirect:/client/login";

		OrderResponseDTO responseDTO = service.findByClientIdAndId(clientId, orderId);
		AuxOrderRequestDTO auxOrderRequestDTO = auxOrderConvert.forAuxOrderRequestDTO(responseDTO, index);
		model.addAttribute("auxOrderRequestDTO", auxOrderRequestDTO);
		model.addAttribute("clientId", clientId);
		model.addAttribute("orderId", orderId);
		model.addAttribute("index", index);

		return "order/update";
	}

	@GetMapping("/delete/client/{clientId}/{orderId}/{index}")
	public String delete(Model model, @PathVariable("clientId") String clientId, @PathVariable("orderId") String orderId,  @PathVariable("index") int index) {
		if (!clientIsLogged()) return  "redirect:/client/login";

		model.addAttribute("clientId", clientId);
		model.addAttribute("orderId", orderId);
		model.addAttribute("index", index);
		return "order/deleteOrderItem";
	}

	@GetMapping("/delete/client/{clientId}/{orderId}")
	public String delete(Model model, @PathVariable("clientId") String clientId, @PathVariable("orderId") String orderId) {
		if (!clientIsLogged()) return  "redirect:/client/login";

		model.addAttribute("clientId", clientId);
		model.addAttribute("orderId", orderId);
		return "order/deleteOrder";
	}

	@GetMapping(value = "/orderBuy/{clientId}/{orderId}")
	public String orderBuy(Model model, @PathVariable("clientId") String clientId, @PathVariable("orderId") String orderId) {
		if (!clientIsLogged()) return  "redirect:/client/login";
		ClientResponseDTO clientRequestDTO = clientService.findById(clientId);
		OrderResponseDTO orderResponseDTO = service.findById(orderId);

		Instant instant = Instant.now();
		ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
		String formattedDateTime = formatter.format(instant.atZone(zoneId));

		EmailRequestDTO emailRequestDTO = new EmailRequestDTO(clientRequestDTO.email(),
				"Olá, " + clientRequestDTO.name() + ", efetue sua compra!",
				"Olá, " + clientRequestDTO.name() + ", tudo bem?" +
						"\n recebemos uma solicitação de compra da sua conta na TOYCENTER" +
						"\n -------------------------------------------------------------" +
						"\n PEDIDO:" +
						"\n DATA E HORA: " + formattedDateTime +
						"\n TOTAL: " + orderResponseDTO.total() +
						"\n ITENS DE PEDIDO: " + orderResponseDTO.orderItens() +
						"\n -------------------------------------------------------------" +
						"\n CLIQUE NO LINK PARA PROSSEGUIR " +
						"\n http://localhost:8080/category");
		emailService.sendEmail(emailRequestDTO);
		return "redirect:/product";
	}

	@PutMapping("/insert")
	public String insert(@ModelAttribute("orderDTO") AuxOrderRequestDTO auxOrderRequestDTO) {
		orderDTO = auxOrderConvert.forOrderRequestDTO(auxOrderRequestDTO);
		service.updateByClientId(auxOrderRequestDTO.clientId(), auxOrderRequestDTO.id(), orderDTO);
		return "redirect:/order";
	}
	
	@PutMapping("/update/client/{clientId}/{orderId}/{index}")
	public String updateByIndex(@ModelAttribute("auxOrderRequestDTO") AuxOrderRequestDTO auxOrderRequestDTO, @PathVariable String clientId, @PathVariable String orderId, @PathVariable("index") int index) {
		orderDTO = auxOrderConvert.forOrderRequestDTO(auxOrderRequestDTO);
		service.updateByIndex(clientId, orderId, orderDTO, index);
		return "redirect:/order";
	}

	@DeleteMapping("/delete/client/{clientId}/{orderId}/{index}")
	public String deleteOrderItemByClientIdAndId(@PathVariable String clientId, @PathVariable String orderId, @PathVariable int index) {
		service.deleteOrderItemByClientIdAndId(clientId, orderId, index);
		return "redirect:/order";
	}

	@DeleteMapping("/delete/client/{clientId}/{orderId}")
	public String deleteByClientIdAndId(@PathVariable String clientId, @PathVariable String orderId) {
		service.deleteByClientIdAndId(clientId, orderId);
		return "redirect:/order";
	}

	private boolean clientIsLogged() {
		HttpSession session = request.getSession();
		String clientId = (String) session.getAttribute("clientId");
		if (clientId == null) return false;
		return true;
	}
}
