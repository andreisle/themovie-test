package com.themovie.test.database.model;

import java.util.List;

public class PersonMovieCreditsResponse {

	// List<PersonCast> cast;
	List<Movie> cast;

	public List<Movie> getCast() {
		return cast;
	}

	public void setCast(List<Movie> cast) {
		this.cast = cast;
	}

}
