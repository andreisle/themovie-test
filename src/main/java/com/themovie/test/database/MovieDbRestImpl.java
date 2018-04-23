package com.themovie.test.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.themovie.test.database.model.ErrorResponse;
import com.themovie.test.database.model.Movie;
import com.themovie.test.database.model.Person;
import com.themovie.test.database.model.PersonMovieCreditsResponse;
import com.themovie.test.database.model.SearchMovieResponse;
import com.themovie.test.database.model.SearchPersonResponse;

/**
 * https://www.themoviedb.org/documentation/api/status-codes
 * 
 *
 */

// TODO: multi page implementation
// TODO: validate input
// TODO: response codes handing 404, bad auth, etc
// if total_results":> 20
// or total_pages > 1
// send request to obtain more pages (if configured)

@Service
public class MovieDbRestImpl implements MovieDbAPI {

	// FIXME: add to configuration
	private static final String API_KEY = "50c33f232135f8738583301da20d31d0";

	private static final String API_V3 = "http://api.themoviedb.org/3";

	private static final int PAGE_MAX_ITEMS = 20;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private Gson gson = new Gson();

	@Override
	public List<Person> searchActor(String fullName) throws DatabaseServiceException, IllegalArgumentException {
		if ((fullName == null) || (fullName.isEmpty()))
			throw new IllegalArgumentException("searchActor incorrect");

		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(API_V3 + "/search/person" + "?api_key=" + API_KEY + "&query=" + URLEncoder.encode(fullName));
			HttpResponse response = httpClient.execute(request);

			final int statusCode = response.getStatusLine().getStatusCode();
			logger.debug("statuscode=" + statusCode);

			final String content = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().parallel()
					.collect(Collectors.joining("\n"));
			logger.debug(content);

			if (statusCode != 200) {
				final ErrorResponse resp = gson.fromJson(content, ErrorResponse.class);
				throw new DatabaseServiceException(resp.getStatus_code() + " " + resp.getStatus_message());
			}

			final SearchPersonResponse resp = gson.fromJson(content, SearchPersonResponse.class);
			return resp.getResults();

		} catch (ClientProtocolException e) {
			throw new DatabaseServiceException(e);
		} catch (IOException e) {
			throw new DatabaseServiceException(e);
		} catch (UnsupportedOperationException e) {
			throw new DatabaseServiceException(e);
		}

	}

	@Override
	public List<Movie> searchFilm(String name) throws DatabaseServiceException, IllegalArgumentException {
		if ((name == null) || (name.isEmpty()))
			throw new IllegalArgumentException("name incorrect");

		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(API_V3 + "/search/movie" + "?api_key=" + API_KEY + "&query=" + URLEncoder.encode(name));
			HttpResponse response = httpClient.execute(request);

			final int statusCode = response.getStatusLine().getStatusCode();
			logger.debug("statuscode=" + statusCode);

			final String content = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().parallel()
					.collect(Collectors.joining("\n"));
			logger.debug(content);

			if (statusCode != 200) {
				final ErrorResponse resp = gson.fromJson(content, ErrorResponse.class);
				throw new DatabaseServiceException(resp.getStatus_code() + " " + resp.getStatus_message());
			}
			final SearchMovieResponse resp = gson.fromJson(content, SearchMovieResponse.class);
			return resp.getResults();
		} catch (ClientProtocolException e) {
			throw new DatabaseServiceException(e);
		} catch (IOException e) {
			throw new DatabaseServiceException(e);
		} catch (UnsupportedOperationException e) {
			throw new DatabaseServiceException(e);
		}
	}

	@Override
	public List<Movie> getActorFilms(long actorId) throws NotFoundException, DatabaseServiceException, IllegalArgumentException {
		if (actorId < 0)
			throw new IllegalArgumentException("actorId incorrect");

		try {
			HttpGet request = new HttpGet(API_V3 + "/person/" + actorId + "/movie_credits" + "?api_key=" + API_KEY);
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse response = httpClient.execute(request);

			final int statusCode = response.getStatusLine().getStatusCode();
			logger.debug("statuscode=" + statusCode);

			final String content = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().parallel()
					.collect(Collectors.joining("\n"));
			logger.debug(content);

			if (statusCode == 404)
				throw new NotFoundException();

			if (statusCode != 200) {
				final ErrorResponse resp = gson.fromJson(content, ErrorResponse.class);
				throw new DatabaseServiceException(resp.getStatus_code() + " " + resp.getStatus_message());
			}

			final PersonMovieCreditsResponse resp = gson.fromJson(content, PersonMovieCreditsResponse.class);
			return resp.getCast();
		} catch (ClientProtocolException e) {
			throw new DatabaseServiceException(e);
		} catch (IOException e) {
			throw new DatabaseServiceException(e);
		} catch (UnsupportedOperationException e) {
			throw new DatabaseServiceException(e);
		}
	}

}
