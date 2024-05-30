package br.edu.toycenter.api.response;

public record AdministratorResponseDTO(
		String id,
		String name,
		String email,
		String password)  {
}
