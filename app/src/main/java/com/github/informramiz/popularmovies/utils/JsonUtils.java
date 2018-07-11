package com.github.informramiz.popularmovies.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.informramiz.popularmovies.model.Movie;
import com.github.informramiz.popularmovies.model.MovieApiResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramiz Raja on 11/07/2018.
 */
public class JsonUtils {
    @Nullable
    public static MovieApiResponse parseMovieApiJsonResponse(@NonNull String response) {m
        try {
            MovieApiResponse movieApiResponse = new MovieApiResponse();

            JSONObject jsonObject = new JSONObject(response);
            movieApiResponse.setPage(jsonObject.optInt("page"));
            movieApiResponse.setTotalResults(jsonObject.optInt("total_results"));
            movieApiResponse.setTotalPages(jsonObject.optInt("total_pages"));
            JSONArray moviesJsonArray = jsonObject.optJSONArray("results");
            movieApiResponse.setResults(parseMoviesJsonArray(moviesJsonArray));

            return movieApiResponse;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<Movie> parseMoviesJsonArray(JSONArray moviesJsonArray) {
        List<Movie> movies = new ArrayList<>(moviesJsonArray.length());
        for (int i = 0; i < moviesJsonArray.length(); i++) {
            JSONObject movieJsonObject = moviesJsonArray.optJSONObject(i);
            if (movieJsonObject != null) {
                movies.add(parseMovieJsonObject(movieJsonObject));
            }
        }

        return movies;
    }

    private static Movie parseMovieJsonObject(@NonNull JSONObject jsonObject) {
        Movie movie = new Movie();

        movie.setId(jsonObject.optInt("id"));
        movie.setTitle(jsonObject.optString("title"));
        movie.setPosterPath(jsonObject.optString("poster_path"));
        movie.setVoteAverage(jsonObject.optDouble("vote_average"));
        movie.setOverview(jsonObject.optString("overview"));
        movie.setReleaseDate(jsonObject.optString("release_date"));

        return movie;
    }
}
