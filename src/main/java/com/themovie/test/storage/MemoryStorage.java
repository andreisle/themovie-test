package com.themovie.test.storage;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class MemoryStorage implements LocalStorageApi {

	private Map<UUID, User> users = new ConcurrentHashMap<>();
	private Map<UUID, Set<Long>> actorsId = new ConcurrentHashMap<>();
	private Map<UUID, Set<Long>> watchedFilmsId = new ConcurrentHashMap<>();

	public User getUserById(UUID uuid) throws IllegalArgumentException {
		if (!users.containsKey(uuid))
			throw new IllegalArgumentException("uuid wrong");

		return users.get(uuid);
	}

	public Set<Long> getActors(UUID uuid) throws IllegalArgumentException {
		if (uuid == null)
			throw new IllegalArgumentException("uuid null");
		if (!actorsId.containsKey(uuid))
			throw new IllegalArgumentException("uuid wrong");

		return actorsId.get(uuid);
	}

	public Set<Long> getWatchedFilms(UUID uuid) throws IllegalArgumentException {
		if (uuid == null)
			throw new IllegalArgumentException("uuid null");
		if (!watchedFilmsId.containsKey(uuid))
			throw new IllegalArgumentException("uuid wrong");

		return watchedFilmsId.get(uuid);
	}

	public UUID addUser(String email) throws IllegalArgumentException {
		if ((email == null) || (email.isEmpty()))
			throw new IllegalArgumentException("email null/empty");

		for (User u : users.values())
			if (u.getEmail().equalsIgnoreCase(email))
				throw new IllegalArgumentException("user already exist");

		final UUID uuid = UUID.randomUUID();
		User u = new User(email);
		u.setUuid(uuid);

		users.put(uuid, u);
		actorsId.put(uuid, ConcurrentHashMap.newKeySet());
		watchedFilmsId.put(uuid, ConcurrentHashMap.newKeySet());

		return uuid;
	}

	public void addActor(UUID uuid, long actorId) throws IllegalArgumentException {
		if (uuid == null)
			throw new IllegalArgumentException("uuid null");
		if (actorId < 0)
			throw new IllegalArgumentException("actorId invalid");
		if (!actorsId.containsKey(uuid))
			throw new IllegalArgumentException("uuid wrong");
		if (actorsId.get(uuid).contains(actorId))
			throw new IllegalArgumentException("actorId already exist");

		actorsId.get(uuid).add(actorId);
	}

	public void removeActor(UUID uuid, long actorId) throws IllegalArgumentException {
		if (uuid == null)
			throw new IllegalArgumentException("uuid null");
		if (actorId < 0)
			throw new IllegalArgumentException("actorId invalid");
		if (!actorsId.containsKey(uuid))
			throw new IllegalArgumentException("uuid wrong");
		if (!actorsId.get(uuid).contains(actorId))
			throw new IllegalArgumentException("actorId not exist");

		actorsId.get(uuid).remove(actorId);
	}

	public void addMovieWatched(UUID uuid, long movieId) throws IllegalArgumentException {
		if (uuid == null)
			throw new IllegalArgumentException("uuid null");
		if (movieId < 0)
			throw new IllegalArgumentException("movieId invalid");
		if (!watchedFilmsId.containsKey(uuid))
			throw new IllegalArgumentException("uuid wrong");
		if (watchedFilmsId.get(uuid).contains(movieId))
			throw new IllegalArgumentException("movieId already exist");

		watchedFilmsId.get(uuid).add(movieId);
	}

	public void removeMovieWatched(UUID uuid, long movieId) throws IllegalArgumentException {
		if (uuid == null)
			throw new IllegalArgumentException("uuid null");
		if (movieId < 0)
			throw new IllegalArgumentException("movieId invalid");
		if (!watchedFilmsId.containsKey(uuid))
			throw new IllegalArgumentException("uuid wrong");
		if (!watchedFilmsId.get(uuid).contains(movieId))
			throw new IllegalArgumentException("movieId not exist");

		watchedFilmsId.get(uuid).remove(movieId);
	}

}
