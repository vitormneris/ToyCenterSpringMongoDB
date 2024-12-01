package br.edu.toycenter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import br.edu.toycenter.service.exceptions.DatabaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.toycenter.controller.mapper.AdministratorMapper;
import br.edu.toycenter.controller.dto.AdministratorDTO;
import br.edu.toycenter.service.exceptions.InvalidFormatException;
import br.edu.toycenter.service.exceptions.LoginInvalidException;
import br.edu.toycenter.service.exceptions.ResourceNotFoundException;
import br.edu.toycenter.model.entities.Administrator;
import br.edu.toycenter.model.repositories.AdministratorRepository;

@Service
@RequiredArgsConstructor
public class AdministratorService {
	private final AdministratorRepository repository;
	private final AdministratorMapper administratorMapper;
	private final ValidationService validationService;

	public List<AdministratorDTO> findAll() {
		List<Administrator> listAdministrator = repository.findAll();
		List<AdministratorDTO> listAdministratorDTO = new ArrayList<>();
		
		for (Administrator administrator : listAdministrator) {
			listAdministratorDTO.add(administratorMapper.forAdministratorResponseDTO(administrator));
		}
		
		return listAdministratorDTO;
	}
	
	public AdministratorDTO findById(String id) {
		try {
			Optional<Administrator> obj = repository.findById(id);
            return administratorMapper.forAdministratorResponseDTO(obj.get());
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		}
	}
	
	public AdministratorDTO findByEmail(String email) {
		try {
			Optional<Administrator> obj = repository.findByEmail(email);
            return administratorMapper.forAdministratorResponseDTO(obj.get());
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("E-mal", email);
		}
	}
	
	public AdministratorDTO insert(AdministratorDTO administratorRequestDTO) {
		try {
			Administrator administrator = administratorMapper.forAdministrator(administratorRequestDTO);
			administrator.setId(null);
			validationService.checkFields(administrator);
			Administrator administratorInserted = repository.save(administrator);
			return administratorMapper.forAdministratorResponseDTO(administratorInserted);
		} catch (InvalidFormatException e){
			throw new InvalidFormatException(e.getMessage());	
		}
	}
	
	public AdministratorDTO update(String id, AdministratorDTO administratorRequestDTO) {
		try {
			Administrator administrator = administratorMapper.forAdministrator(administratorRequestDTO);
			Optional<Administrator> obj = repository.findById(id);
			updateData(obj.get(), administrator);
			obj.get().setId(id);
			validationService.checkFields(obj.get());
			Administrator administratorUpdated = repository.save(obj.get());
			return administratorMapper.forAdministratorResponseDTO(administratorUpdated);
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
			List<Administrator> administratorList = repository.findAll();
			if (administratorList.size() == 1)
				throw new DatabaseException("Can't possible delete this administrator, because he is the only administrator.");
			repository.delete(objAdministrator.get());
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Id", id);
		} catch (DatabaseException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public boolean login(AdministratorDTO administratorRequestDTO) {
		Administrator admRequest = administratorMapper.forAdministrator(administratorRequestDTO);
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
}
