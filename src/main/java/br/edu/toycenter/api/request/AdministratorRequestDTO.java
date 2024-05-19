package br.edu.toycenter.api.request;

public record AdministratorRequestDTO(
		String name,
		String email,
		String password)  {
}
