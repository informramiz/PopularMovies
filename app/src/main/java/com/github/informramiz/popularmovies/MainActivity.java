package com.github.informramiz.popularmovies;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.informramiz.popularmovies.model.MovieApiResponse;
import com.github.informramiz.popularmovies.utils.JsonUtils;
import com.github.informramiz.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mMoviesDataTextView;
    private TextView mErrorMsgTextView;
    private ProgressBar mLoadingIndicator;
    private @NetworkUtils.SortOrder String mSortOrder;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesDataTextView = findViewById(R.id.tv_movies_data);
        mErrorMsgTextView = findViewById(R.id.tv_error_message);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mSortOrder = NetworkUtils.SORT_ORDER_POPULAR;
        loadMoviesData();
    }

    private void loadMoviesData() {
        if (NetworkUtils.isInternetPresent(this)) {
            mMoviesDataTextView.setText("");
            new FetchMoviesTask().execute(mSortOrder);
        } else {
            showErrorMessageView();
        }
    }

    private void showMoviesDataView() {
        mMoviesDataTextView.setVisibility(View.VISIBLE);
        mErrorMsgTextView.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessageView() {
        mMoviesDataTextView.setVisibility(View.INVISIBLE);
        mErrorMsgTextView.setVisibility(View.VISIBLE);
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

    public class FetchMoviesTask extends AsyncTask<String, Void, MovieApiResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
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
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (movieApiResponse != null) {
                Log.v(TAG, "Total movies fetched=" + movieApiResponse.getResults().size());
                mMoviesDataTextView.setText(String.valueOf(movieApiResponse.getResults().size()));
                showMoviesDataView();
            } else {
                showErrorMessageView();
            }
        }
    }
}
