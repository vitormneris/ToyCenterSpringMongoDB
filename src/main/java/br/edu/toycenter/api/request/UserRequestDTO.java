package br.edu.toycenter.api.request;

public record UserRequestDTO(
		String id,
		String cpf,
		String name,
		String email,
		String phone,
		String password)  {
}
