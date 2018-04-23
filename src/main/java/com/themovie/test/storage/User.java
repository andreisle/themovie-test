package com.themovie.test.storage;

import java.util.UUID;

public class User {

	UUID uuid;

	String email;

	public User() {

	}

	public User(String email) {
		this.email = email;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
