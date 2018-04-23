package com.themovie.test.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.validator.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.themovie.test.database.DatabaseServiceException;
import com.themovie.test.database.MoreThenOneRecordException;
import com.themovie.test.database.MovieDbAPI;
import com.themovie.test.database.NotFoundException;
import com.themovie.test.database.model.Movie;
import com.themovie.test.database.model.Person;
import com.themovie.test.storage.LocalStorageApi;

@Service
public class MovieApiImpl implements MovieApi {

	@Autowired
	MovieDbAPI movieDb;

	@Autowired
	LocalStorageApi storage;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public MovieApiImpl() {

	}

	public MovieApiImpl(MovieDbAPI movieDb, LocalStorageApi storage) {
		this.movieDb = movieDb;
		this.storage = storage;
	}

	@Override
	public UUID registerUser(String email) throws IllegalArgumentException {
		if ((email == null) || (email.isEmpty()))
			throw new IllegalArgumentException("email is null/empty");
		logger.debug("email:" + email);
		if (!EmailValidator.getInstance().isValid(email))
			throw new IllegalArgumentException("email invalid " + email);
		final UUID uuid = storage.addUser(email);
		return uuid;
	}

	@Override
	public List<Person> searchActors(UUID uuid, String name) throws IllegalArgumentException, DatabaseServiceException {
		if (uuid == null)
			throw new IllegalArgumentException("uuid is null");
		if ((name == null) || (name.isEmpty()))
			throw new IllegalArgumentException("name is null/empty");
		if (storage.getUserById(uuid) == null)
			throw new IllegalArgumentException("uuid not found");
		return movieDb.searchActor(name);
	}

	@Override
	public long addFavoriteActor(UUID uuid, String actorFullName)
			throws IllegalArgumentException, NotFoundException, DatabaseServiceException, MoreThenOneRecordException {
		if (uuid == null)
			throw new IllegalArgumentException("uuid is null");
		if ((actorFullName == null) || (actorFullName.isEmpty()))
			throw new IllegalArgumentException("name is null/empty");
		if (storage.getUserById(uuid) == null)
			throw new IllegalArgumentException("uuid not found");

		List<Person> actors = movieDb.searchActor(actorFullName);

		if ((actors == null) || (actors.isEmpty()))
			throw new NotFoundException("actor not found");

		if (actors.size() > 1)
			throw new MoreThenOneRecordException("more then 1 record returned by actor's full name");

		final long actorId = actors.get(0).getId();

		storage.addActor(uuid, actorId);
		return actorId;
	}

	@Override
	public void addFavoriteActor(UUID uuid, Long actorId) throws IllegalArgumentException {
		if (uuid == null)
			throw new IllegalArgumentException("uuid is null");
		if ((actorId == null) || (actorId < 0))
			throw new IllegalArgumentException("actorId is null/negative");
		if (storage.getUserById(uuid) == null)
			throw new IllegalArgumentException("uuid not found");

		// TODO: validate given actorId via movieDB
		// TODO: validate actorId already present into storage

		storage.addActor(uuid, actorId);
	}

	@Override
	public void removeFavoriteActor(UUID uuid, String actorFullName) throws IllegalArgumentException, NotFoundException, DatabaseServiceException {
		if (uuid == null)
			throw new IllegalArgumentException("uuid is null");
		if ((actorFullName == null) || (actorFullName.isEmpty()))
			throw new IllegalArgumentException("name is null/empty");
		if (storage.getUserById(uuid) == null)
			throw new IllegalArgumentException("uuid not found");

		List<Person> actors = movieDb.searchActor(actorFullName);

		if ((actors == null) || (actors.isEmpty()))
			throw new NotFoundException("actor not found");

		if (actors.size() > 1)
			throw new IllegalArgumentException("more then 1 record returned by actor's full name");

		// check if actor with given Id present in database

		storage.removeActor(uuid, actors.get(0).getId());
	}

	@Override
	public void removeFavoriteActor(UUID uuid, Long actorId) throws IllegalArgumentException {
		if (uuid == null)
			throw new IllegalArgumentException("uuid is null");
		if ((actorId == null) || (actorId < 0))
			throw new IllegalArgumentException("actorId is null/negative");
		if (storage.getUserById(uuid) == null)
			throw new IllegalArgumentException("uuid not found");

		// TODO: validate given actorId via movieDB
		storage.removeActor(uuid, actorId);
	}

	@Override
	public List<Movie> searchMovies(UUID uuid, String name) throws IllegalArgumentException, DatabaseServiceException {
		if (uuid == null)
			throw new IllegalArgumentException("uuid is null");
		if ((name == null) || (name.isEmpty()))
			throw new IllegalArgumentException("name is null/empty");
		if (storage.getUserById(uuid) == null)
			throw new IllegalArgumentException("uuid not found");

		return movieDb.searchFilm(name);
	}

	@Override
	public long markMovieWatched(UUID uuid, String movieFullName)
			throws IllegalArgumentException, NotFoundException, DatabaseServiceException, MoreThenOneRecordException {
		if (uuid == null)
			throw new IllegalArgumentException("uuid is null");
		if ((movieFullName == null) || (movieFullName.isEmpty()))
			throw new IllegalArgumentException("movieFullName is null/empty");
		if (storage.getUserById(uuid) == null)
			throw new IllegalArgumentException("uuid not found");

		List<Movie> movies = movieDb.searchFilm(movieFullName);
		if ((movies == null) || (movies.isEmpty()))
			throw new NotFoundException("movie not found");
		if (movies.size() > 1)
			throw new MoreThenOneRecordException("more then 1 record returned by movie's full name");

		final long movieId = movies.get(0).getId();
		storage.addMovieWatched(uuid, movieId);
		return movieId;
	}

	@Override
	public void markMovieWatched(UUID uuid, Long movieId) {
		if (uuid == null)
			throw new IllegalArgumentException("uuid is null");
		if ((movieId == null) || (movieId < 0))
			throw new IllegalArgumentException("movieId is null/negative");
		if (storage.getUserById(uuid) == null)
			throw new IllegalArgumentException("uuid not found");

		// TODO: validate given movieId via movieDB
		storage.addMovieWatched(uuid, movieId);
	}

	@Override
	public List<Movie> searchUnviewedFavoriteMovies(UUID uuid, int year, int month) throws IllegalArgumentException, NotFoundException, DatabaseServiceException {
		if (uuid == null)
			throw new IllegalArgumentException("uuid is null");
		if (storage.getUserById(uuid) == null)
			throw new IllegalArgumentException("uuid not found");
		if ((year < 1900) || (year > 2030))
			throw new IllegalArgumentException("year incorrect");
		if ((month < 1) || (month > 12))
			throw new IllegalArgumentException("month incorrect");

		List<Movie> resultMovies = new ArrayList<Movie>();
		// get all movies per favorite actor
		for (Long actorId : storage.getActors(uuid)) {
			resultMovies.addAll(movieDb.getActorFilms(actorId));
		}
		// remove duplicates
		List<Movie> resMovies = resultMovies.stream().distinct().collect(Collectors.toList());

		// remove watched
		resMovies.removeIf(mov -> storage.getWatchedFilms(uuid).contains(mov.getId()));

		// skip movies without any release date
		resMovies.removeIf(mov -> (mov.getRelease_date() == null) || (mov.getRelease_date().isEmpty()));

		// search by year and month
		List<Movie> movs = new ArrayList<>();
		for (Movie mov : resMovies) {
			if ((mov.getYear() == year) && (mov.getMonth() == month))
				movs.add(mov);
		}

		return movs;

	}

}
