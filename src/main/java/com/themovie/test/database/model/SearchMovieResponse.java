package com.themovie.test.database.model;

import java.util.List;

public class SearchMovieResponse extends BasicPagesResponse {

	List<Movie> results;

	public List<Movie> getResults() {
		return results;
	}

	public void setResults(List<Movie> results) {
		this.results = results;
	}

}
