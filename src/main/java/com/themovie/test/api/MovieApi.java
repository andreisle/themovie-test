package com.themovie.test.api;

import java.util.List;
import java.util.UUID;

import com.themovie.test.database.DatabaseServiceException;
import com.themovie.test.database.MoreThenOneRecordException;
import com.themovie.test.database.NotFoundException;
import com.themovie.test.database.model.Movie;
import com.themovie.test.database.model.Person;

public interface MovieApi {

	public UUID registerUser(String email) throws IllegalArgumentException;

	public List<Person> searchActors(UUID uuid, String name) throws IllegalArgumentException, DatabaseServiceException;

	public long addFavoriteActor(UUID uuid, String actorFullName)
			throws IllegalArgumentException, NotFoundException, DatabaseServiceException, MoreThenOneRecordException;

	public void addFavoriteActor(UUID uuid, Long actorId) throws IllegalArgumentException;

	public void removeFavoriteActor(UUID uuid, String actorFullName) throws IllegalArgumentException, NotFoundException, DatabaseServiceException;

	public void removeFavoriteActor(UUID uuid, Long actorId) throws IllegalArgumentException;

	public List<Movie> searchMovies(UUID uuid, String name) throws IllegalArgumentException, DatabaseServiceException;

	public long markMovieWatched(UUID uuid, String movieFullName)
			throws IllegalArgumentException, NotFoundException, DatabaseServiceException, MoreThenOneRecordException;

	public void markMovieWatched(UUID uuid, Long movieId) throws IllegalArgumentException;

	public List<Movie> searchUnviewedFavoriteMovies(UUID uuid, int year, int month) throws IllegalArgumentException, NotFoundException, DatabaseServiceException;

}
