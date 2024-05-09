package br.edu.toycenter.business.exceptions;

public class InvalidFormatException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public InvalidFormatException(String data, Object value) {
		super("Format not is valid. " + data + " " + value);
	}
	
	public InvalidFormatException(String data) {
		super(data);
	}
}
