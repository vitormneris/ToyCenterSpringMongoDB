package br.edu.toycenter.business;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.toycenter.api.convert.UserConvert;
import br.edu.toycenter.api.request.UserRequestDTO;
import br.edu.toycenter.api.response.UserResponseDTO;
import br.edu.toycenter.business.exceptions.InvalidFormatException;
import br.edu.toycenter.business.exceptions.ResourceNotFoundException;
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
	
	public UserResponseDTO findById(String id) {
		try {
			Optional<User> obj = repository.findById(id);
			UserResponseDTO userDTO = userToUserResponseDTO(obj.get());
			return userDTO;
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}
	
	public UserResponseDTO findByCpf(String cpf) {
		try {
			Optional<User> obj = repository.findByCpf(cpf);
			UserResponseDTO userDTO = userToUserResponseDTO(obj.get());
			return userDTO;
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("CPF", cpf);
		}
	}
	
	public UserResponseDTO findByEmail(String email) {
		try {
			Optional<User> obj = repository.findByEmail(email);
			UserResponseDTO userDTO = userToUserResponseDTO(obj.get());
			return userDTO;
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("E-mal", email);
		}
	}
	
	public UserResponseDTO insert(UserRequestDTO userRequestDTO) {
		try {
			User user = userConvert.forUser(userRequestDTO);
			user.setId(null);
			checkFields(user);
			User userInserted = repository.save(user);
			return userToUserResponseDTO(userInserted);
		} catch (InvalidFormatException e){
			throw new InvalidFormatException(e.getMessage());	
		}
	
	}
	
	public UserResponseDTO update(String id, UserRequestDTO userRequestDTO) {
		try {
			User user = userConvert.forUser(userRequestDTO);
			Optional<User> obj = repository.findById(id);
			updateData(obj.get(), user);
			obj.get().setId(id);
			checkFields(obj.get());
			User userUpdated = repository.save(obj.get());
			return userToUserResponseDTO(userUpdated);
		} catch (InvalidFormatException e){
			throw new InvalidFormatException(e.getMessage());	
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}
	
	@Transactional
	public void delete(String id) {
		try {
			Optional<User> objUser = repository.findById(id);
			Optional<Order> objOrder = orderRepository.findByUserId(id);
	
			repository.delete(objUser.get());
			orderRepository.delete(objOrder.get());
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}
	
	private void updateData(User obj, User user) {
		obj.setCpf((user.getCpf() == null) ? obj.getCpf() : user.getCpf());
		obj.setName((user.getName() == null) ? obj.getName() : user.getName());
		obj.setEmail((user.getEmail() == null) ? obj.getEmail() : user.getEmail());
		obj.setPhone((user.getPhone() == null) ? obj.getPhone() : user.getPhone());
		obj.setPassword((user.getPassword() == null) ? obj.getPassword() : user.getPassword());
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
	
	private void checkFields(User user) throws InvalidFormatException {
		if (user == null) throw new InvalidFormatException("Os campos não podem ser nulos");
		
		isNullOrBlank(user.getCpf());
		if (!user.getCpf().matches("^[0-9]{3}.[0-9]{3}.[0-9]{3}-[0-9]{2}$")) 
			throw new InvalidFormatException("CPF", user.getCpf());
		
		isNullOrBlank(user.getName());
		if (!user.getName().matches("^[a-zA-Z ]+$")) 
			throw new InvalidFormatException("Name", user.getName());
		
		isNullOrBlank(user.getEmail());
		if (!user.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) 
			throw new InvalidFormatException("E-mail", user.getEmail());
		
		isNullOrBlank(user.getPhone());
		if (!user.getPhone().matches("\\(?\\d{2}\\)? ?(?:\\d{4,5}-?\\d{4}|\\d{4}-?\\d{4})$")) 
			throw new InvalidFormatException("Phone", user.getPhone());
		
		isNullOrBlank(user.getPassword());
		if (!(user.getPassword().length() >= 8))
			throw new InvalidFormatException("Password");
	}
	
	private void isNullOrBlank(String string) {
		if (string == null || string.isBlank()) 
			throw new InvalidFormatException("The fields can not be null.");
	}
}
