package br.edu.toycenter.controller.dto.request;

import java.util.List;

public record CategoryRequestDTO(
		String id,
		String name,
		String image,
		List<String> productsId
)  {
}
