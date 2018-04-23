package com.themovie.test.database;

public class NotFoundException extends Exception {

	private static final long serialVersionUID = -8563371146458054499L;

	public NotFoundException() {
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(Throwable cause) {
		super(cause);
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
