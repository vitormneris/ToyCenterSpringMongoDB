package br.edu.toycenter.api.request;

import java.util.List;

public record CategoryRequestDTO(
		String id,
		String name,
		String image,
		List<String> productsId)  {
}
