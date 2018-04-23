package com.themovie.test.database;

import java.util.List;

import com.themovie.test.database.model.Movie;
import com.themovie.test.database.model.Person;

public interface MovieDbAPI {

	public List<Person> searchActor(String fullName) throws DatabaseServiceException, IllegalArgumentException;

	public List<Movie> searchFilm(String name) throws DatabaseServiceException, IllegalArgumentException;

	public List<Movie> getActorFilms(long actorId) throws NotFoundException, DatabaseServiceException, IllegalArgumentException;

}
