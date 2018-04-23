package com.themovie.test.database.model;

import java.util.List;

public class SearchPersonResponse extends BasicPagesResponse {

	List<Person> results;

	public List<Person> getResults() {
		return results;
	}

	public void setResults(List<Person> results) {
		this.results = results;
	}

}
