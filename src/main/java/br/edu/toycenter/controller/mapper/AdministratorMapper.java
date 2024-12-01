package br.edu.toycenter.controller.mapper;

import org.springframework.stereotype.Component;

import br.edu.toycenter.controller.dto.AdministratorDTO;
import br.edu.toycenter.model.entities.Administrator;

@Component
public class AdministratorMapper {
	
	public Administrator forAdministrator(AdministratorDTO administratorDTO) {
        return new Administrator.Builder()
                .name(administratorDTO.name())
                .email(administratorDTO.email())
                .password(administratorDTO.password())
                .build();
	}
	
	public AdministratorDTO forAdministratorResponseDTO(Administrator administrator) {

        return new AdministratorDTO(
                administrator.getId(),
                administrator.getName(),
                administrator.getEmail(),
                administrator.getPassword());
	}
}
