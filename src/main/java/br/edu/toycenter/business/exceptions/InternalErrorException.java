package br.edu.toycenter.business.exceptions;

public class InternalErrorException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public InternalErrorException(String data) {
		super(data);
	}
}
