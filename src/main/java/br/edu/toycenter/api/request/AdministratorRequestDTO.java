package br.edu.toycenter.api.request;

public record AdministratorRequestDTO(
		String id,
		String name,
		String email,
		String password)  {
}
