package br.edu.toycenter.api.convert;

import java.util.List;

import org.springframework.stereotype.Component;

import br.edu.toycenter.api.request.ClientRequestDTO;
import br.edu.toycenter.api.response.ClientResponseDTO;
import br.edu.toycenter.infrastructure.entities.Client;
import br.edu.toycenter.infrastructure.entities.Order;

@Component
public class ClientConvert {
	
	public Client forClient(ClientRequestDTO clientDTO) {
		Client client = new Client.Builder()
				.cpf(clientDTO.cpf())
				.name(clientDTO.name())
				.email(clientDTO.email())
				.phone(clientDTO.phone())
				.password(clientDTO.password())
				.build();

		return client;
	}
	
	public ClientResponseDTO forClientResponseDTO(Client client, List<Order> listOrder) {
		ClientResponseDTO clientDTO = new ClientResponseDTO(				
				client.getId(),
				client.getCpf(),
				client.getName(),
				client.getEmail(),
				client.getPhone(),
				client.getPassword(),
				listOrder);
				
		return clientDTO;
	}

	public ClientRequestDTO forClientRequestDTO(ClientResponseDTO clientResponseDTO) {
		ClientRequestDTO clientRequestDTO = new ClientRequestDTO(
				clientResponseDTO.id(),
				clientResponseDTO.cpf(),
				clientResponseDTO.name(),
				clientResponseDTO.email(),
				clientResponseDTO.phone(),
				clientResponseDTO.password());
		return clientRequestDTO;
	}
}
