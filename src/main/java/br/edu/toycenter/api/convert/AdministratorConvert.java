package br.edu.toycenter.api.convert;

import org.springframework.stereotype.Component;

import br.edu.toycenter.api.request.AdministratorRequestDTO;
import br.edu.toycenter.api.response.AdministratorResponseDTO;
import br.edu.toycenter.infrastructure.entities.Administrator;

@Component
public class AdministratorConvert {
	
	public Administrator forAdministrator(AdministratorRequestDTO administratorDTO) {
		Administrator administrator = new Administrator.Builder()
				.name(administratorDTO.name())
				.email(administratorDTO.email())
				.password(administratorDTO.password())
				.build();

		return administrator;
	}
	
	public AdministratorResponseDTO forAdministratorResponseDTO(Administrator administrator) {
		AdministratorResponseDTO administratorDTO = new AdministratorResponseDTO(				
				administrator.getId(),
				administrator.getName(),
				administrator.getEmail(),
				administrator.getPassword());
				
		return administratorDTO;
	}
}
