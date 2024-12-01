package br.edu.toycenter.controller.dto.request;

public record EmailRequestDTO(
        String to,
        String subject,
        String body
) {
}