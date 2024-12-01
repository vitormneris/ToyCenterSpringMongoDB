package br.edu.toycenter.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.toycenter.controller.mapper.OrderMapper;
import br.edu.toycenter.controller.mapper.OrderItemMapper;
import br.edu.toycenter.controller.dto.request.OrderRequestDTO;
import br.edu.toycenter.controller.dto.response.OrderItemResponseDTO;
import br.edu.toycenter.controller.dto.response.OrderResponseDTO;
import br.edu.toycenter.service.exceptions.InvalidFormatException;
import br.edu.toycenter.service.exceptions.ResourceNotFoundException;
import br.edu.toycenter.model.entities.Client;
import br.edu.toycenter.model.entities.Order;
import br.edu.toycenter.model.entities.OrderItem;
import br.edu.toycenter.model.repositories.ClientRepository;
import br.edu.toycenter.model.repositories.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository repository;
	private final ClientRepository clientRepository;
	private final OrderMapper orderMapper;
	private final OrderItemMapper orderItemMapper;
	private final ValidationService validationService;

	public List<OrderResponseDTO> findAll() {
		List<Order> listOrder = repository.findAll();	
		List<OrderResponseDTO> listOrderDTO = new ArrayList<>();
	
		for (Order order : listOrder) {
			listOrderDTO.add(orderToOrderResponseDTO(order));
		}
		
		return listOrderDTO;
	}
	
	public OrderResponseDTO findById(String id) {
		try {
			Optional<Order> obj = repository.findById(id);
            return orderToOrderResponseDTO(obj.orElseThrow());
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}

	public OrderResponseDTO findByClientIdAndId(String clientId, String id) {
		try {
			Optional<Order> obj = repository.findByClientIdAndId(clientId, id);
            return orderToOrderResponseDTO(obj.orElseThrow());
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}

	public List<OrderResponseDTO> findByClientId(String clientId) {
		try {
			List<Order> listOrder = repository.findByClientId(clientId);

			List<OrderResponseDTO> orderResponseDTOList = new ArrayList<>();
			for (Order order : listOrder) {
				OrderResponseDTO orderResponseDTO = orderToOrderResponseDTO(order);
				orderResponseDTOList.add(orderResponseDTO);
			}
			return orderResponseDTOList;
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", clientId);
		}
	}
	
	public OrderResponseDTO insert(OrderRequestDTO orderRequestDTO) {
		try {
			Order order = orderMapper.forOrder(orderRequestDTO);
			Client client = clientRepository.findById(order.getClientId()).orElseThrow();
			client.getOrdersId().add(order.getId());
			order.setId(null);
			order.setMoment(Instant.now());
			for (OrderItem oi : order.getOrderItens()) {
				oi.setPrice(oi.getProduct().getPrice());
			}
			validationService.checkFields(order);
			clientRepository.save(client);
			Order orderInserted = repository.save(order);
			return orderToOrderResponseDTO(orderInserted);
		} catch (InvalidFormatException e){
			throw new InvalidFormatException(e.getMessage());	
		}
	}

	public OrderResponseDTO updateByIndex(String clientId, String id, OrderRequestDTO orderRequestDTO, int index) {
		try {
			Order order = orderMapper.forOrder(orderRequestDTO);
			Optional<Order> obj = repository.findByClientIdAndId(clientId, id);
			obj.get().getOrderItens().get(index).setQuantity(order.getOrderItens().get(0).getQuantity());
			validationService.checkFields(obj.orElseThrow());
			Order orderUpdate = obj.orElseThrow();
			Order orderUpdated = repository.save(orderUpdate);
			return orderMapper.forOrderResponseDTO(orderUpdated);
		} catch (InvalidFormatException e) {
			throw new InvalidFormatException(e.getMessage());
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Client Id", clientId);
		}
	}

	public OrderResponseDTO updateByClientId(String clientId, String id, OrderRequestDTO orderRequestDTO) {
		try {
			Order order = orderMapper.forOrder(orderRequestDTO);
			Optional<Order> obj = repository.findByClientIdAndId(clientId, id);

			if ((obj.get().getOrderItens().isEmpty()) && (obj.get().getMoment() == null)) {
				Order newOrder = new Order(null, null, order.getClientId(), new ArrayList<>());
				newOrder = repository.save(newOrder);
				Client client = clientRepository.findById(newOrder.getClientId()).orElseThrow();
				client.getOrdersId().add(newOrder.getId());
				clientRepository.save(client);
				order.setMoment(Instant.now());
			} else {
				for (OrderItem oi : obj.get().getOrderItens()) {
					OrderItem newOi = new OrderItem(oi.getQuantity(), oi.getPrice(), oi.getProduct());
					order.getOrderItens().add(newOi);
				}
			}

			for (OrderItem oi : order.getOrderItens()) {
				oi.setPrice(oi.getProduct().getPrice());
			}

			updateData(obj.orElseThrow(), order);

			validationService.checkFields(obj.orElseThrow());
			Order orderUpdate = obj.orElseThrow();
			Order orderUpdated = repository.save(orderUpdate);
			return orderMapper.forOrderResponseDTO(orderUpdated);
		} catch (InvalidFormatException e){
			throw new InvalidFormatException(e.getMessage());
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Client Id", clientId);
		}
	}

	@Transactional
	public void deleteByClientIdAndId(String clientId, String orderId) {
		try {
			Order objOrder = repository.findByClientIdAndId(clientId, orderId).orElseThrow();
			Client objClient = clientRepository.findById(clientId).orElseThrow();

			List<String> newOrdersIdClient = new ArrayList<>();
			for (String orderIdClient : objClient.getOrdersId()) {
				if (!(orderIdClient.equals(orderId))) {
					newOrdersIdClient.add(orderIdClient);
				}
			}
			objClient.setOrdersId(newOrdersIdClient);
			clientRepository.save(objClient);

			repository.delete(objOrder);
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", clientId);
		}
	}

	@Transactional
	public void deleteOrderItemByClientIdAndId(String clientId, String orderId, int index) {
		try {
			Order objOrder = repository.findByClientIdAndId(clientId, orderId).orElseThrow();
			objOrder.getOrderItens().remove(index);
			repository.save(objOrder);
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", clientId);
		}
	}

	private void updateData(Order obj, Order order) {
		obj.setMoment((order.getMoment() == null) ? obj.getMoment() : order.getMoment());

		int sub = order.getOrderItens().size() - obj.getOrderItens().size();

		for (int i = 0; i < sub; i++) {
			obj.getOrderItens().add(new OrderItem());
		}

		for (int i = 0; i < order.getOrderItens().size(); i++) {
			obj.getOrderItens().get(i).setQuantity(order.getOrderItens().get(i).getQuantity());
			obj.getOrderItens().get(i).setPrice(order.getOrderItens().get(i).getPrice());
			obj.getOrderItens().get(i).setProduct(order.getOrderItens().get(i).getProduct());
		}
	}
	
	public OrderResponseDTO orderToOrderResponseDTO(Order order) {

		Optional<Client> clientObj = clientRepository.findById(order.getClientId());
		List<OrderItemResponseDTO> listOrderItemDTO = new ArrayList<>();

		for (OrderItem orderItem : order.getOrderItens()) {
			OrderItemResponseDTO orderItemDTO = orderItemMapper.forOrderItemResponseDTO(orderItem);
			listOrderItemDTO.add(orderItemDTO);
		}

        return orderMapper.forOrderResponseDTO(order, clientObj.orElseThrow(), listOrderItemDTO);
	}
}
