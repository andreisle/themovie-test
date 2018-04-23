package com.themovie.test.rest;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.themovie.test.api.MovieApi;
import com.themovie.test.database.DatabaseServiceException;
import com.themovie.test.database.MoreThenOneRecordException;
import com.themovie.test.database.NotFoundException;
import com.themovie.test.database.model.Movie;
import com.themovie.test.database.model.Person;

@RestController
public class MoviesRestController {

	@Autowired
	MovieApi api;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@PostMapping(value = "/v1/register/user")
	public String registerUser(@RequestParam String email, HttpServletRequest request, HttpServletResponse response) {
		try {
			return api.registerUser(email).toString();
		} catch (IllegalArgumentException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		} catch (Exception e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	@GetMapping(value = "/v1/search/actor/{name}", produces = "application/json")
	public List<Person> searchActors(@RequestParam UUID uuid, @PathVariable String name, HttpServletRequest request, HttpServletResponse response) {
		try {
			return api.searchActors(uuid, name);
		} catch (IllegalArgumentException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		} catch (Exception e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	@GetMapping(value = "/v1/search/movie/{name}", produces = "application/json")
	public List<Movie> searchMovies(@RequestParam UUID uuid, @PathVariable String name, HttpServletRequest request, HttpServletResponse response) {
		try {
			return api.searchMovies(uuid, name);
		} catch (IllegalArgumentException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		} catch (Exception e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	// ========================================

	@PostMapping(value = "/v1/favorite/actor/id")
	public void addFavoriteActor(@RequestParam UUID uuid, @RequestParam long actorId, HttpServletRequest request, HttpServletResponse response) {
		try {
			api.addFavoriteActor(uuid, actorId);
		} catch (IllegalArgumentException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/v1/favorite/actor")
	public Long addFavoriteActor(@RequestParam UUID uuid, @RequestParam String actorName, HttpServletRequest request, HttpServletResponse response) {
		try {
			return api.addFavoriteActor(uuid, actorName);
		} catch (IllegalArgumentException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (NotFoundException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (MoreThenOneRecordException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		} catch (DatabaseServiceException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	// ========================================

	@PostMapping(value = "/v1/movie/watched/id")
	public void markMovieWatched(@RequestParam UUID uuid, @RequestParam long movieId, HttpServletRequest request, HttpServletResponse response) {
		try {
			api.addFavoriteActor(uuid, movieId);
		} catch (IllegalArgumentException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/v1/movie/watched")
	public Long markMovieWatched(@RequestParam UUID uuid, @RequestParam String movieName, HttpServletRequest request, HttpServletResponse response) {
		try {
			return api.markMovieWatched(uuid, movieName);
		} catch (IllegalArgumentException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (NotFoundException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (MoreThenOneRecordException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		} catch (DatabaseServiceException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	// ========================================

	@DeleteMapping(value = "/v1/favorite/actor/id")
	public void removeFavoriteActor(@RequestParam UUID uuid, @RequestParam long actorId, HttpServletRequest request, HttpServletResponse response) {
		try {
			api.removeFavoriteActor(uuid, actorId);
		} catch (IllegalArgumentException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(value = "/v1/favorite/actor")
	public void removeFavoriteActor(@RequestParam UUID uuid, @RequestParam String actorFullName, HttpServletRequest request, HttpServletResponse response) {
		try {
			api.removeFavoriteActor(uuid, actorFullName);
		} catch (IllegalArgumentException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (NotFoundException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (DatabaseServiceException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	// ========================================

	@PostMapping(value = "/v1/search/unviewed", produces = "application/json")
	public List<Movie> searchUnviewedFavoriteMovies(@RequestParam UUID uuid, @RequestParam int year, @RequestParam int month, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			return api.searchUnviewedFavoriteMovies(uuid, year, month);
		} catch (IllegalArgumentException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (NotFoundException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (DatabaseServiceException e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			logger.error("Exception occured!", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

}
