package com.github.informramiz.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.informramiz.popularmovies.model.Movie;
import com.github.informramiz.popularmovies.model.MovieApiResponse;
import com.github.informramiz.popularmovies.utils.JsonUtils;
import com.github.informramiz.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MoviesRecycleViewAdapter.ListItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mMoviesListRecyclerView;
    private MoviesRecycleViewAdapter mMoviesRecycleViewAdapter;
    private TextView mErrorMsgTextView;
    private ProgressBar mLoadingIndicator;
    private @NetworkUtils.SortOrder String mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesListRecyclerView = findViewById(R.id.rv_movies_list);
        mErrorMsgTextView = findViewById(R.id.tv_error_message);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mMoviesRecycleViewAdapter = new MoviesRecycleViewAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mMoviesListRecyclerView.setLayoutManager(gridLayoutManager);
        mMoviesListRecyclerView.setHasFixedSize(true);
        mMoviesListRecyclerView.setAdapter(mMoviesRecycleViewAdapter);

        mSortOrder = NetworkUtils.SORT_ORDER_POPULAR;
        loadMoviesData();
    }

    private void loadMoviesData() {
        if (NetworkUtils.isInternetPresent(this)) {
            new FetchMoviesTask().execute(mSortOrder);
        } else {
            showErrorMessageView();
        }
    }

    private void showMoviesDataView() {
        mMoviesListRecyclerView.setVisibility(View.VISIBLE);
        mErrorMsgTextView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessageView() {
        mMoviesListRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMsgTextView.setVisibility(View.VISIBLE);
    }

    private void showLoadingIndicatorView() {
        mMoviesListRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMsgTextView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicatorView() {
        mMoviesListRecyclerView.setVisibility(View.VISIBLE);
        mErrorMsgTextView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mSortOrder.equals(NetworkUtils.SORT_ORDER_POPULAR)) {
            menu.findItem(R.id.action_sort_by_popular).setChecked(true);
        } else if (mSortOrder.equals(NetworkUtils.SORT_ORDER_TOP_RATED)){
            menu.findItem(R.id.action_sort_by_top_rated).setChecked(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_sort_by_popular:
                item.setChecked(true);
                mSortOrder = NetworkUtils.SORT_ORDER_POPULAR;
                loadMoviesData();
                return true;

            case R.id.action_sort_by_top_rated:
                item.setChecked(true);
                mSortOrder = NetworkUtils.SORT_ORDER_TOP_RATED;
                loadMoviesData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(Movie clickedMovie) {
        Intent movieDetailActivityIntent = new Intent(this, MovieDetailActivity.class);
        movieDetailActivityIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE, clickedMovie);
        startActivity(movieDetailActivityIntent);
    }

    class FetchMoviesTask extends AsyncTask<String, Void, MovieApiResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingIndicatorView();
        }

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
            hideLoadingIndicatorView();

            if (movieApiResponse != null) {
                Log.v(TAG, "Total movies fetched=" + movieApiResponse.getResults().size());
                mMoviesRecycleViewAdapter.setMovies(movieApiResponse.getResults());
                showMoviesDataView();
            } else {
                showErrorMessageView();
            }
        }
    }
}
