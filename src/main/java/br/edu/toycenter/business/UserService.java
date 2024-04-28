package br.edu.toycenter.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.toycenter.api.convert.UserConvert;
import br.edu.toycenter.api.response.UserResponseDTO;
import br.edu.toycenter.infrastructure.entities.Order;
import br.edu.toycenter.infrastructure.entities.User;
import br.edu.toycenter.infrastructure.repositories.OrderRepository;
import br.edu.toycenter.infrastructure.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserConvert userConvert;
	
	public List<UserResponseDTO> findAll() {
		List<User> listUser = repository.findAll();
		
		List<UserResponseDTO> listUserDTO = new ArrayList<>();
		
		for (User user : listUser) {
			List<Order> listOrder = new ArrayList<>();
			
			for (String orderId : user.getOrdersId()) {
				Optional<Order> obj = orderRepository.findById(orderId);
				listOrder.add(obj.get());
			}
			listUserDTO.add(userConvert.forUserResponseDTO(user, listOrder));
		}
		
		return listUserDTO;
	}
	
	public User findByCpf(String cpf) {
		Optional<User> obj = repository.findByCpf(cpf);
		return obj.orElseThrow();
	}
	
	public User findByEmail(String email) {
		Optional<User> obj = repository.findByEmail(email);
		return obj.orElseThrow();
	}
}
