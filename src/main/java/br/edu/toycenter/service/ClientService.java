package br.edu.toycenter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.toycenter.controller.mapper.ClientMapper;
import br.edu.toycenter.controller.dto.ClientDTO;
import br.edu.toycenter.service.exceptions.InvalidFormatException;
import br.edu.toycenter.service.exceptions.LoginInvalidException;
import br.edu.toycenter.service.exceptions.ResourceNotFoundException;
import br.edu.toycenter.model.entities.Client;
import br.edu.toycenter.model.entities.Order;
import br.edu.toycenter.model.repositories.ClientRepository;
import br.edu.toycenter.model.repositories.OrderRepository;

@Service
@RequiredArgsConstructor
public class ClientService {
	private final ClientRepository repository;
	private final OrderRepository orderRepository;
	private final ClientMapper clientMapper;
	private final ValidationService validationService;

	public List<ClientDTO> findAll() {
		List<Client> listClient = repository.findAll();
		List<ClientDTO> listClientDTO = new ArrayList<>();
		
		for (Client client : listClient) {
			listClientDTO.add(clientToClientResponseDTO(client));
		}
		
		return listClientDTO;
	}
	
	public ClientDTO findById(String id) {
		try {
			Optional<Client> obj = repository.findById(id);
            return clientToClientResponseDTO(obj.get());
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}
	
	public ClientDTO findByCpf(String cpf) {
		try {
			Optional<Client> obj = repository.findByCpf(cpf);
            return clientToClientResponseDTO(obj.get());
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("CPF", cpf);
		}
	}
	
	public ClientDTO findByEmail(String email) {
		try {
			Optional<Client> obj = repository.findByEmail(email);
            return clientToClientResponseDTO(obj.get());
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("E-mal", email);
		}
	}
	
	public ClientDTO insert(ClientDTO clientRequestDTO) {
		try {
			Client client = clientMapper.forClient(clientRequestDTO);
			client.setId(null);
			validationService.checkFields(client);
			Client clientInserted = repository.save(client);
			Order order = new Order(null, null, clientInserted.getId(), new ArrayList<>());
			Order orderInserted = orderRepository.save(order);
			clientInserted.getOrdersId().add(orderInserted.getId());
			repository.save(clientInserted);
			return clientToClientResponseDTO(clientInserted);
		} catch (InvalidFormatException e){
			throw new InvalidFormatException(e.getMessage());	
		}
	}
	
	public ClientDTO update(String id, ClientDTO clientRequestDTO) {
		try {
			Client client = clientMapper.forClient(clientRequestDTO);
			Optional<Client> obj = repository.findById(id);
			updateData(obj.get(), client);
			obj.get().setId(id);
			validationService.checkFields(obj.get());
			Client clientUpdated = repository.save(obj.get());
			return clientToClientResponseDTO(clientUpdated);
		} catch (InvalidFormatException e){
			throw new InvalidFormatException(e.getMessage());	
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}
	
	@Transactional
	public void delete(String id) {
		try {
			Optional<Client> objClient = repository.findById(id);
			List<Order> objOrder = orderRepository.findByClientId(id);
	
			repository.delete(objClient.get());
			orderRepository.deleteAll(objOrder);
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}

	public boolean login(ClientDTO ClientRequestDTO) {

		Client clientRequest = clientMapper.forClient(ClientRequestDTO);
		Client clientDatabase = repository.findByEmail(clientRequest.getEmail()).orElseThrow(LoginInvalidException::new);

		if (clientDatabase.getPassword().equals(clientRequest.getPassword()))
			return true;
		return false;
	}
	
	private void updateData(Client obj, Client client) {
		obj.setCpf((client.getCpf() == null) ? obj.getCpf() : client.getCpf());
		obj.setName((client.getName() == null) ? obj.getName() : client.getName());
		obj.setEmail((client.getEmail() == null) ? obj.getEmail() : client.getEmail());
		obj.setPhone((client.getPhone() == null) ? obj.getPhone() : client.getPhone());
		obj.setPassword((client.getPassword() == null) ? obj.getPassword() : client.getPassword());
	}

	public ClientDTO clientToClientResponseDTO(Client client) {
		List<Order> listOrder = new ArrayList<>();
		
		for (String orderId : client.getOrdersId()) {
			Optional<Order> obj = orderRepository.findById(orderId);
			listOrder.add(obj.get());
		}

        return clientMapper.forClientResponseDTO(client, listOrder);
	}
}
