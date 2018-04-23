package com.themovie.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.themovie.test.storage.LocalStorageApi;
import com.themovie.test.storage.MemoryStorage;
import com.themovie.test.storage.User;

public class MemoryStorageTest {

	LocalStorageApi storage;

	@Before
	public void init() {
		storage = new MemoryStorage();
	}

	@Test
	public void addUser() {
		final String EMAIL = "email@email.com";
		final UUID uuid = storage.addUser(EMAIL);

		assertNotNull(storage.getUserById(uuid));
		assertEquals(EMAIL, storage.getUserById(uuid).getEmail());
		assertEquals(uuid, storage.getUserById(uuid).getUuid());

		assertNotNull(storage.getActors(uuid));
		assertNotNull(storage.getWatchedFilms(uuid));

		assertEquals(0, storage.getActors(uuid).size());
		assertEquals(0, storage.getWatchedFilms(uuid).size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void addUser_BAD1_null() {
		storage.addUser(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addUser_BAD2_empty() {
		storage.addUser("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void addUser_duplicate() {
		storage.addUser("aaaBBBCCC@test.com");
		storage.addUser("aaaBBBCCC@test.com");
	}

	@Test(expected = IllegalArgumentException.class)
	public void getUserById_wrongUUID() {
		User u = storage.getUserById(UUID.randomUUID());
		assertNotNull(u);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getActors_wrongUUID() {
		storage.getActors(UUID.randomUUID());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getWatchedFilms_wrongUUID() {
		storage.getWatchedFilms(UUID.randomUUID());
	}

	@Test
	public void addActor() {
		final UUID uuid = storage.addUser("email@email.com");
		storage.addActor(uuid, (long) 777);

		assertEquals(1, storage.getActors(uuid).size());
		assertTrue(storage.getActors(uuid).contains((long) 777));
	}

	@Test(expected = IllegalArgumentException.class)
	public void addActor_alreadyExist() {
		final UUID uuid = storage.addUser("email@email.com");
		storage.addActor(uuid, (long) 222);
		storage.addActor(uuid, (long) 222);

		assertEquals(1, storage.getActors(uuid).size());
		assertTrue(storage.getActors(uuid).contains((long) 777));
	}

	@Test(expected = IllegalArgumentException.class)
	public void addActor_wrongUUID() {
		storage.addUser("email@email.com");
		storage.addActor(UUID.randomUUID(), (long) 777);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addActor_wrongActorId() {
		storage.addUser("email@email.com");
		storage.addActor(UUID.randomUUID(), (long) -1277);
	}

	@Test
	public void removeActor() {
		final UUID uuid = storage.addUser("email1@email.com");
		storage.addActor(uuid, (long) 999);

		storage.removeActor(uuid, (long) 999);
		assertEquals(0, storage.getActors(uuid).size());
		assertFalse(storage.getActors(uuid).contains((long) 999));
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeActor_badActorId() {
		final UUID uuid = storage.addUser("email1@email.com");
		storage.addActor(uuid, (long) 333);
		storage.removeActor(uuid, (long) 999);
	}

	@Test
	public void addMovieWatched() {
		final UUID uuid = storage.addUser("email@email.com");
		storage.addMovieWatched(uuid, (long) 1000);

		assertEquals(1, storage.getWatchedFilms(uuid).size());
		assertTrue(storage.getWatchedFilms(uuid).contains((long) 1000));
	}

	@Test(expected = IllegalArgumentException.class)
	public void addMovieWatched_alreadyExist() {
		final UUID uuid = storage.addUser("email@email.com");
		storage.addMovieWatched(uuid, (long) 2000);
		storage.addMovieWatched(uuid, (long) 2000);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addMovieWatched_wrongUUID() {
		storage.addUser("email@email.com");
		storage.addMovieWatched(UUID.randomUUID(), (long) 555);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addMovieWatched_wrongMovieId() {
		storage.addUser("email@email.com");
		storage.addMovieWatched(UUID.randomUUID(), (long) -12737);
	}

	@Test
	public void removeMovieWatched() {
		final UUID uuid = storage.addUser("email1@email.com");
		storage.addMovieWatched(uuid, (long) 1999);

		storage.removeMovieWatched(uuid, (long) 1999);
		assertEquals(0, storage.getWatchedFilms(uuid).size());
		assertFalse(storage.getWatchedFilms(uuid).contains((long) 1999));
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeMovieWatched_badActorId() {
		final UUID uuid = storage.addUser("email1@email.com");
		storage.addMovieWatched(uuid, (long) 333);
		storage.removeMovieWatched(uuid, (long) 999);
	}

}
