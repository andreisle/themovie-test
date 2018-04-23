package com.themovie.test.database;

public class MoreThenOneRecordException extends Exception {

	public MoreThenOneRecordException() {
	}

	public MoreThenOneRecordException(String message) {
		super(message);
	}

	public MoreThenOneRecordException(Throwable cause) {
		super(cause);
	}

	public MoreThenOneRecordException(String message, Throwable cause) {
		super(message, cause);
	}

}
