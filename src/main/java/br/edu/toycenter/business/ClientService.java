package br.edu.toycenter.business;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.toycenter.api.convert.ClientConvert;
import br.edu.toycenter.api.request.ClientRequestDTO;
import br.edu.toycenter.api.response.ClientResponseDTO;
import br.edu.toycenter.business.exceptions.InvalidFormatException;
import br.edu.toycenter.business.exceptions.LoginInvalidException;
import br.edu.toycenter.business.exceptions.ResourceNotFoundException;
import br.edu.toycenter.infrastructure.entities.Client;
import br.edu.toycenter.infrastructure.entities.Order;
import br.edu.toycenter.infrastructure.repositories.ClientRepository;
import br.edu.toycenter.infrastructure.repositories.OrderRepository;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository repository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ClientConvert clientConvert;
	
	public List<ClientResponseDTO> findAll() {
		List<Client> listClient = repository.findAll();
		List<ClientResponseDTO> listClientDTO = new ArrayList<>();
		
		for (Client client : listClient) {
			listClientDTO.add(clientToClientResponseDTO(client));
		}
		
		return listClientDTO;
	}
	
	public ClientResponseDTO findById(String id) {
		try {
			Optional<Client> obj = repository.findById(id);
			ClientResponseDTO clientDTO = clientToClientResponseDTO(obj.get());
			return clientDTO;
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}
	
	public ClientResponseDTO findByCpf(String cpf) {
		try {
			Optional<Client> obj = repository.findByCpf(cpf);
			ClientResponseDTO clientDTO = clientToClientResponseDTO(obj.get());
			return clientDTO;
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("CPF", cpf);
		}
	}
	
	public ClientResponseDTO findByEmail(String email) {
		try {
			Optional<Client> obj = repository.findByEmail(email);
			ClientResponseDTO clientDTO = clientToClientResponseDTO(obj.get());
			return clientDTO;
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("E-mal", email);
		}
	}
	
	public ClientResponseDTO insert(ClientRequestDTO clientRequestDTO) {
		try {
			Client client = clientConvert.forClient(clientRequestDTO);
			client.setId(null);
			checkFields(client);
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
	
	public ClientResponseDTO update(String id, ClientRequestDTO clientRequestDTO) {
		try {
			Client client = clientConvert.forClient(clientRequestDTO);
			Optional<Client> obj = repository.findById(id);
			updateData(obj.get(), client);
			obj.get().setId(id);
			checkFields(obj.get());
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

	public boolean login(ClientRequestDTO ClientRequestDTO) {

		Client clientRequest = clientConvert.forClient(ClientRequestDTO);
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

	public ClientResponseDTO clientToClientResponseDTO(Client client) {
		List<Order> listOrder = new ArrayList<>();
		
		for (String orderId : client.getOrdersId()) {
			Optional<Order> obj = orderRepository.findById(orderId);
			listOrder.add(obj.get());
		}
		
		ClientResponseDTO clientDTO = clientConvert.forClientResponseDTO(client, listOrder);
		return clientDTO;
	}
	
	private void checkFields(Client client) throws InvalidFormatException {
		if (client == null) throw new InvalidFormatException("Os campos não podem ser nulos");
		
		isNullOrBlank(client.getCpf());
		if (!client.getCpf().matches("^[0-9]{3}.[0-9]{3}.[0-9]{3}-[0-9]{2}$")) 
			throw new InvalidFormatException("CPF", client.getCpf());
		
		isNullOrBlank(client.getName());
		if (!client.getName().matches("^[a-zA-Z ]+$")) 
			throw new InvalidFormatException("Name", client.getName());
		
		isNullOrBlank(client.getEmail());
		if (!client.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) 
			throw new InvalidFormatException("E-mail", client.getEmail());
		
		isNullOrBlank(client.getPhone());
		if (!client.getPhone().matches("\\(?\\d{2}\\)? ?(?:\\d{4,5}-?\\d{4}|\\d{4}-?\\d{4})$")) 
			throw new InvalidFormatException("Phone", client.getPhone());
		
		isNullOrBlank(client.getPassword());
		if (!(client.getPassword().length() >= 8))
			throw new InvalidFormatException("Password");
	}
	
	private void isNullOrBlank(String string) throws InvalidFormatException {
		if (string == null || string.isBlank()) 
			throw new InvalidFormatException("The fields can not be null.");
	}
}
