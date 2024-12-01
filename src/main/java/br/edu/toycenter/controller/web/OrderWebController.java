package br.edu.toycenter.controller.web;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import br.edu.toycenter.controller.dto.request.EmailRequestDTO;
import br.edu.toycenter.controller.dto.response.ProductResponseDTO;
import br.edu.toycenter.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.toycenter.controller.mapper.AuxOrderMapper;
import br.edu.toycenter.controller.dto.request.AuxOrderRequestDTO;
import br.edu.toycenter.controller.dto.request.OrderRequestDTO;
import br.edu.toycenter.controller.dto.ClientDTO;
import br.edu.toycenter.controller.dto.response.OrderResponseDTO;
import br.edu.toycenter.service.ClientService;
import br.edu.toycenter.service.EmailService;
import br.edu.toycenter.service.OrderService;
import br.edu.toycenter.model.entities.Order;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/order")
public class OrderWebController {
	private final OrderService service;
	private final ClientService clientService;
	private final ProductService productService;
	private final EmailService emailService;
	private final AuxOrderMapper auxOrderMapper;
	private final  HttpServletRequest request;
	
	@GetMapping()
	public String findByClientId(Model model) {
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", "/order");
		if (!clientIsLogged()) return  "redirect:/client/login";

		String clientId = (String) session.getAttribute("clientId");
		List<OrderResponseDTO> listResponseDTO = service.findByClientId(clientId);
		List<OrderResponseDTO> listDTO = new ArrayList<>();

		if (!(listResponseDTO.size() == 1)) {
			listDTO.add(listResponseDTO.get(0));
		}
		model.addAttribute("orderDTOList", listDTO);
		return "order/findByClientId";
	}

	@GetMapping("/checkEmail")
	public String emailSend(Model model) {
		if (!clientIsLogged()) return  "redirect:/client/login";
		return "checkEmail";
	}

	@GetMapping("/confirmedEmail")
	public String confirmedEmail(Model model) {
		if (!clientIsLogged()) return  "redirect:/client/login";
		return "confirmedEmail";
	}
	
	@GetMapping("/insert/{productId}")
	public String insert(Model model, @PathVariable("productId") String productId) {
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/order/insert/" + productId));
		if (!clientIsLogged()) return  "redirect:/client/login";
		String clientId = (String) session.getAttribute("clientId");
		ClientDTO clientDTO = clientService.findById(clientId);
		List<String> listOrderId = new ArrayList<>();

		for (Order order : clientDTO.orders()) {
			listOrderId.add(order.getId());
		}
		String orderId = listOrderId.get(0);
		AuxOrderRequestDTO auxOrderRequestDTO = new AuxOrderRequestDTO(orderId, clientId, 0, productId);
		ProductResponseDTO productDTO = productService.findById(productId);
		model.addAttribute("productDTO", productDTO);
		model.addAttribute("orderRequestDTO", auxOrderRequestDTO);
		return "order/insert";
	}
	
	@GetMapping("/update/client/{clientId}/{orderId}/{index}")
	public String update(Model model, @PathVariable("clientId") String clientId, @PathVariable("orderId") String orderId, @PathVariable("index") int index) {
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/order/update/client/" + clientId + "/" + orderId + "/" + index));
		if (!clientIsLogged()) return  "redirect:/client/login";
		OrderResponseDTO responseDTO = service.findByClientIdAndId(clientId, orderId);
		AuxOrderRequestDTO auxOrderRequestDTO = auxOrderMapper.forAuxOrderRequestDTO(responseDTO, index);
		ProductResponseDTO productDTO = productService.findById(auxOrderRequestDTO.productId());
		model.addAttribute("productDTO", productDTO);
		model.addAttribute("auxOrderRequestDTO", auxOrderRequestDTO);
		model.addAttribute("clientId", clientId);
		model.addAttribute("orderId", orderId);
		model.addAttribute("index", index);
		return "order/update";
	}

	@GetMapping("/delete/client/{clientId}/{orderId}/{index}")
	public String delete(Model model, @PathVariable("clientId") String clientId, @PathVariable("orderId") String orderId,  @PathVariable("index") int index) {
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/order/delete/client/" + clientId + "/" + orderId + "/" + index));
		if (!clientIsLogged()) return  "redirect:/client/login";
		model.addAttribute("clientId", clientId);
		model.addAttribute("orderId", orderId);
		model.addAttribute("index", index);
		return "order/deleteOrderItem";
	}

	@GetMapping("/delete/client/{clientId}/{orderId}")
	public String delete(Model model, @PathVariable("clientId") String clientId, @PathVariable("orderId") String orderId) {
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/order/delete/client/" + clientId + "/" + orderId));
		if (!clientIsLogged()) return  "redirect:/client/login";
		model.addAttribute("clientId", clientId);
		model.addAttribute("orderId", orderId);
		return "order/deleteOrder";
	}

	@GetMapping(value = "/orderBuy/{clientId}/{orderId}")
	public String orderBuy(Model model, @PathVariable("clientId") String clientId, @PathVariable("orderId") String orderId) {
		HttpSession	session = request.getSession();
		session.setAttribute("previousURI", ("/order/orderBuy/" + clientId + "/" + orderId));
		if (!clientIsLogged()) return  "redirect:/client/login";
		ClientDTO clientRequestDTO = clientService.findById(clientId);
		OrderResponseDTO orderResponseDTO = service.findById(orderId);
		Instant instant = Instant.now();
		ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
		String formattedDateTime = formatter.format(instant.atZone(zoneId));

		EmailRequestDTO emailRequestDTO = new EmailRequestDTO(clientRequestDTO.email(),
				"Olá, " + clientRequestDTO.name() + ", efetue sua compra!",
				"Olá, " + clientRequestDTO.name() + ", tudo bem?" +
						"\n recebemos uma solicitação de compra da sua conta na TOYCENTER" +
						"\n ----------------------------------------------------------------------------------------" +
						"\n PEDIDO:" +
						"\n DATA E HORA: " + formattedDateTime +
						"\n TOTAL: " + orderResponseDTO.total() +
						"\n ITENS DE PEDIDO: " + orderResponseDTO.orderItens() +
						"\n ----------------------------------------------------------------------------------------" +
						"\n CLIQUE NO LINK PARA PROSSEGUIR " +
						"\n http://localhost:8080/order/confirmedEmail");
		emailService.sendEmail(emailRequestDTO);
		return "redirect:/order/checkEmail";
	}

	@PutMapping("/insert")
	public String insert(@ModelAttribute("orderDTO") AuxOrderRequestDTO auxOrderRequestDTO) {
		OrderRequestDTO orderDTO = auxOrderMapper.forOrderRequestDTO(auxOrderRequestDTO);
		service.updateByClientId(auxOrderRequestDTO.clientId(), auxOrderRequestDTO.id(), orderDTO);
		return "redirect:/order";
	}
	
	@PutMapping("/update/client/{clientId}/{orderId}/{index}")
	public String updateByIndex(@ModelAttribute("auxOrderRequestDTO") AuxOrderRequestDTO auxOrderRequestDTO, @PathVariable String clientId, @PathVariable String orderId, @PathVariable("index") int index) {
		OrderRequestDTO orderDTO = auxOrderMapper.forOrderRequestDTO(auxOrderRequestDTO);
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
        return clientId != null;
    }
}
