package br.edu.toycenter.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.toycenter.api.convert.UserConvert;
import br.edu.toycenter.api.request.UserRequestDTO;
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
			listUserDTO.add(userToUserResponseDTO(user));
		}
		
		return listUserDTO;
	}
	
	public UserResponseDTO findByCpf(String cpf) {
		Optional<User> obj = repository.findByCpf(cpf);
		UserResponseDTO userDTO = userToUserResponseDTO(obj.get());
		return userDTO;
	}
	
	public UserResponseDTO findByEmail(String email) {
		Optional<User> obj = repository.findByEmail(email);
		UserResponseDTO userDTO = userToUserResponseDTO(obj.get());
		return userDTO;
	}
	
	public UserResponseDTO insert(UserRequestDTO userRequestDTO) {
		User user = userConvert.forUser(userRequestDTO);
		User userInserted = repository.save(user);
		return userToUserResponseDTO(userInserted);
	}
	
	public UserResponseDTO update(String id, UserRequestDTO userRequestDTO) {
		User user = userConvert.forUser(userRequestDTO);
		Optional<User> obj = repository.findById(id);
		updateData(obj.get(), user);
		User userUpdated = repository.save(obj.get());
		return userToUserResponseDTO(userUpdated);
	}
	
	private void updateData(User obj, User user) {
		obj.setCpf(user.getCpf());
		obj.setName(user.getName());
		obj.setEmail(user.getEmail());
		obj.setPhone(user.getPhone());
		obj.setPassword(user.getPassword());
	}

	public UserResponseDTO userToUserResponseDTO(User user) {
		List<Order> listOrder = new ArrayList<>();
		
		for (String orderId : user.getOrdersId()) {
			Optional<Order> obj = orderRepository.findById(orderId);
			listOrder.add(obj.get());
		}
		
		UserResponseDTO userDTO = userConvert.forUserResponseDTO(user, listOrder);
		return userDTO;
	}
}
