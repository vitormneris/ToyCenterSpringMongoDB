package br.edu.toycenter.api.request;

public record ClientRequestDTO(
		String cpf,
		String name,
		String email,
		String phone,
		String password)  {
}
