package br.edu.toycenter.api.convert;

import java.util.List;

import org.springframework.stereotype.Component;

import br.edu.toycenter.api.request.UserRequestDTO;
import br.edu.toycenter.api.response.UserResponseDTO;
import br.edu.toycenter.infrastructure.entities.Order;
import br.edu.toycenter.infrastructure.entities.User;

@Component
public class UserConvert {
	
	public User forUser(UserRequestDTO userDTO) {
		User user = new User.Builder()
				.cpf(userDTO.cpf())
				.name(userDTO.name())
				.email(userDTO.email())
				.phone(userDTO.phone())
				.password(userDTO.password())
				.build();

		return user;
	}
	
	public UserResponseDTO forUserResponseDTO(User user, List<Order> listOrder) {
		UserResponseDTO userDTO = new UserResponseDTO(				
				user.getId(),
				user.getCpf(),
				user.getName(),
				user.getEmail(),
				user.getPhone(),
				user.getPassword(),
				listOrder);
				
		return userDTO;
	}
}
