package com.themovie.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.themovie.test.database.DatabaseServiceException;
import com.themovie.test.database.MovieDbAPI;
import com.themovie.test.database.MovieDbRestImpl;
import com.themovie.test.database.NotFoundException;
import com.themovie.test.database.model.Movie;
import com.themovie.test.database.model.Person;

public class MovieDBTest {

	MovieDbAPI api;

	@Before
	public void init() {
		api = new MovieDbRestImpl();
	}

	@Test
	public void searchActor_one() throws IllegalArgumentException, DatabaseServiceException {
		List<Person> persons = api.searchActor("Tom Hanks");

		assertEquals(1, persons.size());
		assertEquals("Tom Hanks", persons.get(0).getName());
		assertEquals(31, persons.get(0).getId());
	}

	@Test
	public void searchActor_empty1() throws IllegalArgumentException, DatabaseServiceException {
		List<Person> persons = api.searchActor("Alex Jonson");
		assertEquals(0, persons.size());
	}

	@Test
	public void searchActor_empty2() throws IllegalArgumentException, DatabaseServiceException {
		List<Person> persons = api.searchActor("Mattew Peterson");
		assertEquals(0, persons.size());
	}

	@Test
	public void searchActor_many() throws IllegalArgumentException, DatabaseServiceException {
		List<Person> persons = api.searchActor("Emilson");

		assertEquals(2, persons.size());
		assertEquals("Fredrik Emilson", persons.get(0).getName());
		assertEquals(1164734, persons.get(0).getId());

		assertEquals("Emilson st Felix", persons.get(1).getName());
		assertEquals(1905778, persons.get(1).getId());
	}

	@Test
	public void searchFilm_one() throws IllegalArgumentException, DatabaseServiceException {
		List<Movie> movies = api.searchFilm("the Matrix Reloaded");

		assertEquals(1, movies.size());
		assertEquals(Long.valueOf(604), movies.get(0).getId());
		assertEquals("The Matrix Reloaded", movies.get(0).getTitle());
		assertEquals("2003-05-15", movies.get(0).getRelease_date());
	}

	@Test
	public void searchFilm_empty() throws IllegalArgumentException, DatabaseServiceException {
		List<Movie> movies = api.searchFilm("bravehood");
		assertEquals(0, movies.size());
	}

	@Test
	public void searchFilm_many() throws IllegalArgumentException, DatabaseServiceException {
		List<Movie> movies = api.searchFilm("warcraft");
		assertEquals(3, movies.size());
		assertEquals(Long.valueOf(68735), movies.get(0).getId());
		assertEquals("Warcraft", movies.get(0).getTitle());
		assertEquals("2016-05-25", movies.get(0).getRelease_date());
		assertEquals(Long.valueOf(301865), movies.get(1).getId());
		assertEquals("World of Warcraft: Looking For Group", movies.get(1).getTitle());
		assertEquals("2014-11-08", movies.get(1).getRelease_date());
		assertEquals(Long.valueOf(391584), movies.get(2).getId());
		assertEquals("World of Warcraft - Geschichte eines Kult-Spiels", movies.get(2).getTitle());
		assertEquals("2015-01-01", movies.get(2).getRelease_date());
	}

	@Test
	public void getActorFilms_one() throws NotFoundException, DatabaseServiceException {
		List<Movie> movies = api.getActorFilms(2016805);
		assertEquals(1, movies.size());
		assertEquals(Long.valueOf(472945), movies.get(0).getId());
		assertEquals("Space Boobs In Space", movies.get(0).getTitle());
		assertEquals("2017-08-10", movies.get(0).getRelease_date());
	}

	@Test(expected = NotFoundException.class)
	public void getActorFilms_wrongActorId() throws NotFoundException, DatabaseServiceException {
		api.getActorFilms(3000000);
	}

}
