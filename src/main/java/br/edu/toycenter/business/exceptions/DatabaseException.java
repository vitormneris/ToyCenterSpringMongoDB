package br.edu.toycenter.business.exceptions;

public class DatabaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public DatabaseException(String data) {
		super(data);
	}
}
