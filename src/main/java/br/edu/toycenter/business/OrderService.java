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
import br.edu.toycenter.infrastructure.entities.Order;
import br.edu.toycenter.infrastructure.entities.OrderItem;
import br.edu.toycenter.infrastructure.entities.Product;
import br.edu.toycenter.infrastructure.entities.User;
import br.edu.toycenter.infrastructure.repositories.OrderRepository;
import br.edu.toycenter.infrastructure.repositories.UserRepository;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository repository;
	
	@Autowired
	private UserRepository userRepository;
	
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
	
	public OrderResponseDTO insert(OrderRequestDTO orderRequestDTO) {
		try {
			Order order = orderConvert.forOrder(orderRequestDTO);
			order.setId(null);
			order.setMoment(Instant.now());
			for (OrderItem oi : order.getOrderItens()) {
				oi.setPrice(oi.getProduct().getPrice());
			}
			checkFields(order);
			Order orderInserted = repository.save(order);
			return orderToOrderResponseDTO(orderInserted);
		} catch (InvalidFormatException e){
			throw new InvalidFormatException(e.getMessage());	
		}
	}
	
	public OrderResponseDTO update(String id, OrderRequestDTO orderRequestDTO) {
		try {
			Order order = orderConvert.forOrder(orderRequestDTO);
			Optional<Order> obj = repository.findById(id);
			updateData(obj.get(), order);
			obj.get().setId(id);
			checkFields(obj.get());
			Order orderUpdated = repository.save(obj.get());
			return orderToOrderResponseDTO(orderUpdated);
		} catch (InvalidFormatException e){
			throw new InvalidFormatException(e.getMessage());	
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}
	
	@Transactional
	public void delete(String id) {
		try {
			Optional<Order> obj = repository.findById(id);
			Optional<User> objUser = userRepository.findById(obj.get().getUserId());
			
			objUser.get().setOrdersId(new ArrayList<>());
			userRepository.save(objUser.get());
			
			repository.delete(obj.get());
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}
	
	private void updateData(Order obj, Order order) {
		for (OrderItem orderItem : order.getOrderItens()) {
			orderItem.setPrice(orderItem.getProduct().getPrice());
		}
		
		for (int i = 0; i < obj.getOrderItens().size(); i++) {
			obj.getOrderItens().get(i).setQuantity(
			(order.getOrderItens().get(i).getQuantity() == null) ? 
			obj.getOrderItens().get(i).getQuantity() : order.getOrderItens().get(i).getQuantity());
			
			obj.getOrderItens().get(i).setPrice(
			(order.getOrderItens().get(i).getPrice() == null) ? 
			obj.getOrderItens().get(i).getPrice() : order.getOrderItens().get(i).getPrice());
			
			obj.getOrderItens().get(i).setProduct(
			(order.getOrderItens().get(i).getProduct() == null) ? 
			obj.getOrderItens().get(i).getProduct() : order.getOrderItens().get(i).getProduct());
		}
	}
	
	public OrderResponseDTO orderToOrderResponseDTO(Order order) {
		Optional<User> userObj = userRepository.findById(order.getUserId());
		List<OrderItemResponseDTO> listOrderItemDTO = new ArrayList<>();
		
		for (OrderItem orderItem : order.getOrderItens()) {
			OrderItemResponseDTO orderItemDTO = orderItemConvert.forOrderItemResponseDTO(orderItem);
			listOrderItemDTO.add(orderItemDTO);
		}	
		
		OrderResponseDTO orderDTO = orderConvert.forOrderResponseDTO(order, userObj.get(), listOrderItemDTO);
		return orderDTO;
	}
	
	private void checkFields(Order order) throws InvalidFormatException {
		if (order == null) throw new InvalidFormatException("The fields can not be null.");
		
		isNullOrBlank(order.getMoment());
		isNullOrBlank(order.getUserId());
		
		for (OrderItem oi : order.getOrderItens()) {
			isNullOrBlank(oi.getQuantity());
			isNullOrBlank(oi.getPrice());
			isNullOrBlank(oi.getProduct());
		}
	}
	
	private void isNullOrBlank(String string) throws InvalidFormatException {
		if (string == null || string.isBlank()) 
			throw new InvalidFormatException("This user is not valid.");
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
