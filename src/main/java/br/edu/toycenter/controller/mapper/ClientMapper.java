package br.edu.toycenter.controller.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import br.edu.toycenter.controller.dto.ClientDTO;
import br.edu.toycenter.model.entities.Client;
import br.edu.toycenter.model.entities.Order;

@Component
public class ClientMapper {
	
	public Client forClient(ClientDTO clientDTO) {
        return new Client.Builder()
                .cpf(clientDTO.cpf())
                .name(clientDTO.name())
                .email(clientDTO.email())
                .phone(clientDTO.phone())
                .password(clientDTO.password())
                .build();
	}
	
	public ClientDTO forClientResponseDTO(Client client, List<Order> listOrder) {
        return new ClientDTO(
                client.getId(),
                client.getCpf(),
                client.getName(),
                client.getEmail(),
                client.getPhone(),
                client.getPassword(),
                listOrder);
	}
}
