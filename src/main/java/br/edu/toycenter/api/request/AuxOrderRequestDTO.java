package br.edu.toycenter.api.request;

public record AuxOrderRequestDTO(
	String id,
    String clientId,
    Integer quantity,
    String productId) {
}
