package br.edu.toycenter.service.exceptions;

import java.io.Serial;

public class InvalidFormatException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 1L;
	
	public InvalidFormatException(String data, Object value) {
		super("Format not is valid. " + data + " " + value);
	}
	
	public InvalidFormatException(String data) {
		super(data);
	}
}
