package com.themovie.test.storage;

import java.util.Set;
import java.util.UUID;

public interface LocalStorageApi {

	public User getUserById(UUID uuid) throws IllegalArgumentException;

	public Set<Long> getActors(UUID uuid) throws IllegalArgumentException;

	public Set<Long> getWatchedFilms(UUID uuid) throws IllegalArgumentException;

	public UUID addUser(String email) throws IllegalArgumentException;

	public void addActor(UUID uuid, long actorId) throws IllegalArgumentException;

	public void removeActor(UUID uuid, long actorId) throws IllegalArgumentException;

	public void addMovieWatched(UUID uuid, long movieId) throws IllegalArgumentException;

	public void removeMovieWatched(UUID uuid, long movieId) throws IllegalArgumentException;

}
