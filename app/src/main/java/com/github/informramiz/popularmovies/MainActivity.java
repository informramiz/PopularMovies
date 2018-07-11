package com.github.informramiz.popularmovies;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.informramiz.popularmovies.model.MovieApiResponse;
import com.github.informramiz.popularmovies.utils.JsonUtils;
import com.github.informramiz.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "API key is=" + BuildConfig.API_KEY);
        NetworkUtils.buildMovieApiUrl(NetworkUtils.SORT_ORDER_TOP_RATED);
        NetworkUtils.buildImageUrl("/rv1AWImgx386ULjcf62VYaW8zSt.jpg");
        Log.d(TAG, "Is internet present=" + NetworkUtils.isInternetPresent(this));
        new FetchMoviesTask().execute(NetworkUtils.SORT_ORDER_POPULAR);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, MovieApiResponse> {

        @Nullable
        @Override
        protected MovieApiResponse doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String sortOrder = params[0];
            URL url = NetworkUtils.buildMovieApiUrl(sortOrder);
            if (url == null) {
                return null;
            }

            MovieApiResponse movieApiResponse = null;
            try {
                String movieApiJsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                if (movieApiJsonResponse != null) {
                    movieApiResponse = JsonUtils.parseMovieApiJsonResponse(movieApiJsonResponse);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return movieApiResponse;
        }

        @Override
        protected void onPostExecute(@Nullable MovieApiResponse movieApiResponse) {
            super.onPostExecute(movieApiResponse);
            if (movieApiResponse != null) {
                Log.v(TAG, "Total movies fetched=" + movieApiResponse.getResults().size());
            }
        }
    }
}
