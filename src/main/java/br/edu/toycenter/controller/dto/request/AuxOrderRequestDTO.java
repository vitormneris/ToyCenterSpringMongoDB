package br.edu.toycenter.controller.dto.request;

public record AuxOrderRequestDTO(
		String id,
    	String clientId,
    	Integer quantity,
   	    String productId
) {
}
