package br.edu.toycenter.api.request;

public record EmailRequestDTO(String to, String subject, String body) {

}