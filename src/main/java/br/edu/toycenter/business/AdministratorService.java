package br.edu.toycenter.business;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.toycenter.api.convert.AdministratorConvert;
import br.edu.toycenter.api.request.AdministratorRequestDTO;
import br.edu.toycenter.api.response.AdministratorResponseDTO;
import br.edu.toycenter.business.exceptions.InvalidFormatException;
import br.edu.toycenter.business.exceptions.LoginInvalidException;
import br.edu.toycenter.business.exceptions.ResourceNotFoundException;
import br.edu.toycenter.infrastructure.entities.Administrator;
import br.edu.toycenter.infrastructure.repositories.AdministratorRepository;

@Service
public class AdministratorService {
	
	@Autowired
	private AdministratorRepository repository;
	
	@Autowired
	private AdministratorConvert administratorConvert;
	
	public List<AdministratorResponseDTO> findAll() {
		List<Administrator> listAdministrator = repository.findAll();
		List<AdministratorResponseDTO> listAdministratorDTO = new ArrayList<>();
		
		for (Administrator administrator : listAdministrator) {
			listAdministratorDTO.add(administratorConvert.forAdministratorResponseDTO(administrator));
		}
		
		return listAdministratorDTO;
	}
	
	public AdministratorResponseDTO findById(String id) {
		try {
			Optional<Administrator> obj = repository.findById(id);
			AdministratorResponseDTO administratorDTO = administratorConvert.forAdministratorResponseDTO(obj.get());
			return administratorDTO;
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}
	
	public AdministratorResponseDTO findByEmail(String email) {
		try {
			Optional<Administrator> obj = repository.findByEmail(email);
			AdministratorResponseDTO administratorDTO = administratorConvert.forAdministratorResponseDTO(obj.get());
			return administratorDTO;
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("E-mal", email);
		}
	}
	
	public AdministratorResponseDTO insert(AdministratorRequestDTO administratorRequestDTO) {
		try {
			Administrator administrator = administratorConvert.forAdministrator(administratorRequestDTO);
			administrator.setId(null);
			checkFields(administrator);
			Administrator administratorInserted = repository.save(administrator);
			return administratorConvert.forAdministratorResponseDTO(administratorInserted);
		} catch (InvalidFormatException e){
			throw new InvalidFormatException(e.getMessage());	
		}
	}
	
	public AdministratorResponseDTO update(String id, AdministratorRequestDTO administratorRequestDTO) {
		try {
			Administrator administrator = administratorConvert.forAdministrator(administratorRequestDTO);
			Optional<Administrator> obj = repository.findById(id);
			updateData(obj.get(), administrator);
			obj.get().setId(id);
			checkFields(obj.get());
			Administrator administratorUpdated = repository.save(obj.get());
			return administratorConvert.forAdministratorResponseDTO(administratorUpdated);
		} catch (InvalidFormatException e){
			throw new InvalidFormatException(e.getMessage());	
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}
	
	@Transactional
	public void delete(String id) {
		try {
			Optional<Administrator> objAdministrator = repository.findById(id);
			
			repository.delete(objAdministrator.get());
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}

	public boolean login(AdministratorRequestDTO administratorRequestDTO) {
		Administrator admRequest = administratorConvert.forAdministrator(administratorRequestDTO);
		Administrator admDatabase = repository.findByEmail(admRequest.getEmail()).orElseThrow(LoginInvalidException::new);
		
		if (admDatabase.getPassword().equals(admRequest.getPassword())) 
			return true;
		return false;
	}
	
	private void updateData(Administrator obj, Administrator administrator) {
		obj.setName((administrator.getName() == null) ? obj.getName() : administrator.getName());
		obj.setEmail((administrator.getEmail() == null) ? obj.getEmail() : administrator.getEmail());
		obj.setPassword((administrator.getPassword() == null) ? obj.getPassword() : administrator.getPassword());
	}
	
	private void checkFields(Administrator administrator) throws InvalidFormatException {
		if (administrator == null) throw new InvalidFormatException("Os campos não podem ser nulos");
		
		isNullOrBlank(administrator.getName(), "name");
		if (!administrator.getName().matches("^[a-zA-Z ]+$")) 
			throw new InvalidFormatException("Name", administrator.getName());
		
		isNullOrBlank(administrator.getEmail(), "e-mail");
		if (!administrator.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) 
			throw new InvalidFormatException("E-mail", administrator.getEmail());
		
		isNullOrBlank(administrator.getPassword(), "password");
		if (!(administrator.getPassword().length() >= 8))
			throw new InvalidFormatException("Password");
	}
	
	private void isNullOrBlank(String string, String field) throws InvalidFormatException {
		if (string == null || string.isBlank()) 
			throw new InvalidFormatException("The " + field + " can not be null.");
	}
}
