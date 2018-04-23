package com.themovie.test.database;

public class DatabaseServiceException extends Exception {

	private static final long serialVersionUID = 3405054550565894554L;

	public DatabaseServiceException() {
	}

	public DatabaseServiceException(String message) {
		super(message);
	}

	public DatabaseServiceException(Throwable cause) {
		super(cause);
	}

	public DatabaseServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}