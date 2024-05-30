package br.edu.toycenter.business;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.toycenter.api.convert.OrderConvert;
import br.edu.toycenter.api.convert.OrderItemConvert;
import br.edu.toycenter.api.request.OrderRequestDTO;
import br.edu.toycenter.api.response.OrderItemResponseDTO;
import br.edu.toycenter.api.response.OrderResponseDTO;
import br.edu.toycenter.business.exceptions.InvalidFormatException;
import br.edu.toycenter.business.exceptions.ResourceNotFoundException;
import br.edu.toycenter.infrastructure.entities.Client;
import br.edu.toycenter.infrastructure.entities.Order;
import br.edu.toycenter.infrastructure.entities.OrderItem;
import br.edu.toycenter.infrastructure.entities.Product;
import br.edu.toycenter.infrastructure.repositories.ClientRepository;
import br.edu.toycenter.infrastructure.repositories.OrderRepository;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository repository;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private OrderConvert orderConvert; 

	@Autowired
	private OrderItemConvert orderItemConvert;
	
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
			OrderResponseDTO orderResponseDTO = orderToOrderResponseDTO(obj.orElseThrow());
			return orderResponseDTO;
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}

	public OrderResponseDTO findByClientIdAndId(String clientId, String id) {
		try {
			Optional<Order> obj = repository.findByClientIdAndId(clientId, id);
			OrderResponseDTO orderResponseDTO = orderToOrderResponseDTO(obj.orElseThrow());
			return orderResponseDTO;
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
			Order order = orderConvert.forOrder(orderRequestDTO);
			Client client = clientRepository.findById(order.getClientId()).orElseThrow();
			client.getOrdersId().add(order.getId());
			order.setId(null);
			order.setMoment(Instant.now());
			for (OrderItem oi : order.getOrderItens()) {
				oi.setPrice(oi.getProduct().getPrice());
			}
			checkFields(order);
			clientRepository.save(client);
			Order orderInserted = repository.save(order);
			return orderToOrderResponseDTO(orderInserted);
		} catch (InvalidFormatException e){
			throw new InvalidFormatException(e.getMessage());	
		}
	}
	
	public OrderResponseDTO insertNull(OrderRequestDTO orderRequestDTO) {
		try {
			Order order = orderConvert.forOrder(orderRequestDTO);
			Client client = clientRepository.findById(order.getClientId()).orElseThrow();
			Order orderInserted = repository.save(order);
			client.getOrdersId().add(orderInserted.getId());
			clientRepository.save(client);
			return orderToOrderResponseDTO(orderInserted);
		} catch (InvalidFormatException e){
			throw new InvalidFormatException(e.getMessage());	
		}
	}

	public OrderResponseDTO updateByClientId(String clientId, String id, OrderRequestDTO orderRequestDTO) {
		try {
			Order order = orderConvert.forOrder(orderRequestDTO);
			Optional<Order> obj = repository.findByClientIdAndId(clientId, id);

			if ((obj.get().getOrderItens().size() == 0) && (obj.get().getMoment() == null)) {
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

			checkFields(obj.orElseThrow());
			Order orderUpdate = obj.orElseThrow();
			Order orderUpdated = repository.save(orderUpdate);
			return orderConvert.forOrderResponseDTO(orderUpdated);
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
			OrderItemResponseDTO orderItemDTO = orderItemConvert.forOrderItemResponseDTO(orderItem);
			listOrderItemDTO.add(orderItemDTO);
		}

        return orderConvert.forOrderResponseDTO(order, clientObj.orElseThrow(), listOrderItemDTO);
	}
	
	private void checkFields(Order order) throws InvalidFormatException {
		if (order == null) throw new InvalidFormatException("The fields can not be null.");
		
		isNullOrBlank(order.getMoment());
		isNullOrBlank(order.getClientId());
		
		for (OrderItem oi : order.getOrderItens()) {
			isNullOrBlank(oi.getQuantity());
			isNullOrBlank(oi.getPrice());
			isNullOrBlank(oi.getProduct());
		}
	}
	
	private void isNullOrBlank(String string) throws InvalidFormatException {
		if (string == null || string.isBlank()) 
			throw new InvalidFormatException("This client is not valid.");
	}
	
	private void isNullOrBlank(Double doub) throws InvalidFormatException {
		if (doub == null || doub <= 0f)
			throw new InvalidFormatException("This price is not valid.");
	}
	
	private void isNullOrBlank(Integer interger) throws InvalidFormatException {
		if (interger == null || interger <= 0) 
			throw new InvalidFormatException("This quantity is not valid.");
	}
	
	private void isNullOrBlank(Instant moment) throws InvalidFormatException {
		if (moment == null) 
			throw new InvalidFormatException("This time is not valid.");
	}
	
	private void isNullOrBlank(Product product) throws InvalidFormatException {
		if (product == null) 
			throw new InvalidFormatException("This product is not valid.");
	}
}
