package com.themovie.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.themovie.test.api.MovieApi;
import com.themovie.test.api.MovieApiImpl;
import com.themovie.test.database.DatabaseServiceException;
import com.themovie.test.database.MoreThenOneRecordException;
import com.themovie.test.database.MovieDbAPI;
import com.themovie.test.database.MovieDbRestImpl;
import com.themovie.test.database.NotFoundException;
import com.themovie.test.database.model.Movie;
import com.themovie.test.database.model.Person;
import com.themovie.test.storage.LocalStorageApi;
import com.themovie.test.storage.MemoryStorage;

public class MovieApiIntegrationTest {

	MovieApi mApi;

	MovieDbAPI mDb;

	LocalStorageApi storageApi;

	UUID testUuid;

	@Before
	public void init() {
		storageApi = new MemoryStorage();
		mDb = new MovieDbRestImpl();
		mApi = new MovieApiImpl(mDb, storageApi);

		testUuid = mApi.registerUser("testuser1@testcompany.com");
	}

	@Test
	public void registerUser() {
		final UUID uuid = mApi.registerUser("user@dotcode.com");
		assertNotNull(storageApi.getUserById(uuid));
		assertEquals("user@dotcode.com", storageApi.getUserById(uuid).getEmail());
		assertEquals(uuid, storageApi.getUserById(uuid).getUuid());
	}

	@Test(expected = IllegalArgumentException.class)
	public void registerUser_badEmail() {
		mApi.registerUser("@BAD.EMAIL");
	}

	@Test
	public void searchActors() throws IllegalArgumentException, DatabaseServiceException {
		List<Person> actors = mApi.searchActors(testUuid, "Tom Hanks");
		assertEquals(1, actors.size());
		assertEquals("Tom Hanks", actors.get(0).getName());
		assertEquals(31, actors.get(0).getId());
	}

	@Test
	public void searchMovies() throws IllegalArgumentException, DatabaseServiceException {
		List<Movie> movies = mApi.searchMovies(testUuid, "StarCraft");
		assertEquals(1, movies.size());
		assertEquals(Long.valueOf(498425), movies.get(0).getId());
		assertEquals("StarCraft II - Year One", movies.get(0).getTitle());
		assertEquals("2011-08-30", movies.get(0).getRelease_date());

	}

	@Test
	public void addFavoriteActor_byFullName() throws IllegalArgumentException, NotFoundException, DatabaseServiceException, MoreThenOneRecordException {
		final long actorId = mApi.addFavoriteActor(testUuid, "Tom Hanks");
		assertTrue(storageApi.getActors(testUuid).contains(actorId));
		assertTrue(storageApi.getActors(testUuid).contains((long) 31));
	}

	@Test
	public void addFavoriteActor_byID() throws IllegalArgumentException, NotFoundException, DatabaseServiceException, MoreThenOneRecordException {
		mApi.addFavoriteActor(testUuid, (long) 2254);
		assertTrue(storageApi.getActors(testUuid).contains((long) 2254));
	}

	@Test(expected = NotFoundException.class)
	public void addFavoriteActor_badActorNotFound() throws IllegalArgumentException, NotFoundException, DatabaseServiceException, MoreThenOneRecordException {
		mApi.addFavoriteActor(testUuid, "Tom Mattews Smith");
	}

	@Test(expected = MoreThenOneRecordException.class)
	public void addFavoriteActor_moreThenOneRecordFromDB()
			throws IllegalArgumentException, NotFoundException, DatabaseServiceException, MoreThenOneRecordException {
		mApi.addFavoriteActor(testUuid, "Mike Smith");
	}

	@Test
	public void markMovieWatched_ByFullName() throws IllegalArgumentException, NotFoundException, DatabaseServiceException, MoreThenOneRecordException {
		mApi.markMovieWatched(testUuid, "Capturing Avatar");
		assertTrue(storageApi.getWatchedFilms(testUuid).contains((long) 183392));
	}

	@Test
	public void markMovieWatched_byID() throws IllegalArgumentException, NotFoundException, DatabaseServiceException, MoreThenOneRecordException {
		mApi.markMovieWatched(testUuid, (long) 183392);
		assertTrue(storageApi.getWatchedFilms(testUuid).contains((long) 183392));
	}

	@Test(expected = NotFoundException.class)
	public void markMovieWatched_NotFound() throws IllegalArgumentException, NotFoundException, DatabaseServiceException, MoreThenOneRecordException {
		mApi.markMovieWatched(testUuid, "BadFilm");
	}

	@Test(expected = MoreThenOneRecordException.class)
	public void markMovieWatched_MoreThenOneRecordException()
			throws IllegalArgumentException, NotFoundException, DatabaseServiceException, MoreThenOneRecordException {
		mApi.markMovieWatched(testUuid, "Equilibrium");
	}

	@Test
	public void searchUnviewedFavoriteMovies_1() throws IllegalArgumentException, NotFoundException, DatabaseServiceException, MoreThenOneRecordException {
		final UUID uuid = mApi.registerUser("testuser2@codecode.com");

		mApi.addFavoriteActor(uuid, "Johnny Depp"); // 85
		mApi.addFavoriteActor(uuid, "Tom Hanks"); // 31
		mApi.addFavoriteActor(uuid, "Bruce Willis"); // 62
		mApi.addFavoriteActor(uuid, "Gary Oldman"); // 64
		mApi.addFavoriteActor(uuid, "Milla Jovovich"); // 63
		mApi.addFavoriteActor(uuid, "Cate Blanchett"); // 112
		mApi.addFavoriteActor(uuid, "Mark Hamill"); // 2

		// mApi.markMovieWatched(uuid, "The Fifth Element");

		List<Movie> movs = mApi.searchUnviewedFavoriteMovies(uuid, 1997, 5);

		assertEquals(1, movs.size());
		assertEquals("The Fifth Element", movs.get(0).getTitle());
	}

	@Test
	public void searchUnviewedFavoriteMovies_2() throws IllegalArgumentException, NotFoundException, DatabaseServiceException, MoreThenOneRecordException {
		final UUID uuid = mApi.registerUser("testuser2@codecode.com");

		mApi.addFavoriteActor(uuid, "Johnny Depp"); // 85
		mApi.addFavoriteActor(uuid, "Tom Hanks"); // 31
		mApi.addFavoriteActor(uuid, "Bruce Willis"); // 62
		mApi.addFavoriteActor(uuid, "Gary Oldman"); // 64
		mApi.addFavoriteActor(uuid, "Milla Jovovich"); // 63
		mApi.addFavoriteActor(uuid, "Cate Blanchett"); // 112
		mApi.addFavoriteActor(uuid, "Mark Hamill"); // 2

		mApi.markMovieWatched(uuid, "The Fifth Element");

		List<Movie> movs = mApi.searchUnviewedFavoriteMovies(uuid, 1997, 5);

		assertEquals(0, movs.size());
	}

	@Test
	public void searchUnviewedFavoriteMovies_3() throws IllegalArgumentException, NotFoundException, DatabaseServiceException, MoreThenOneRecordException {
		final UUID uuid = mApi.registerUser("testuser2@codecode.com");

		mApi.addFavoriteActor(uuid, "Johnny Depp"); // 85
		mApi.addFavoriteActor(uuid, "Tom Hanks"); // 31
		mApi.addFavoriteActor(uuid, "Bruce Willis"); // 62
		mApi.addFavoriteActor(uuid, "Gary Oldman"); // 64
		mApi.addFavoriteActor(uuid, "Milla Jovovich"); // 63
		mApi.addFavoriteActor(uuid, "Cate Blanchett"); // 112
		mApi.addFavoriteActor(uuid, "Mark Hamill"); // 2

		mApi.markMovieWatched(uuid, "The Lord of the Rings: The Fellowship of the Ring");
		mApi.markMovieWatched(uuid, "The Lord of the Rings: The Two Towers");
		//// ---mApi.markMovieWatched(uuid, "The Lord of the Rings: The Return of the King");
		mApi.markMovieWatched(uuid, "The Hobbit: An Unexpected Journey");
		mApi.markMovieWatched(uuid, "The Hobbit: The Battle of the Five Armies");
		mApi.markMovieWatched(uuid, "Lennon or McCartney");
		// ---mApi.markMovieWatched(uuid, "Into the Woods");

		List<Movie> movs = mApi.searchUnviewedFavoriteMovies(uuid, 2014, 12);

		assertEquals(3, movs.size());
		for (Movie m : movs) {
			assertTrue(m.getYear() == 2014);
			assertTrue(m.getMonth() == 12);
			assertTrue(m.getTitle().equals("Elf: Buddy's Musical Christmas") || m.getTitle().equals("Into the Woods")
					|| m.getTitle().equals("Toy Story That Time Forgot"));
		}

	}

	// TODO: remove actor tests

}
