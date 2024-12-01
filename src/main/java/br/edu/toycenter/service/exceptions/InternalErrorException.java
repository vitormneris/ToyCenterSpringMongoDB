package br.edu.toycenter.service.exceptions;

import java.io.Serial;

public class InternalErrorException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 1L;
	
	public InternalErrorException(String data) {
		super(data);
	}
}
