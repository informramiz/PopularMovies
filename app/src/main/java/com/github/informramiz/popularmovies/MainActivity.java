package com.github.informramiz.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.informramiz.popularmovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "API key is=" + BuildConfig.API_KEY);
        NetworkUtils.buildMovieApiUrl(NetworkUtils.SORT_ORDER_TOP_RATED);
        NetworkUtils.buildImageUrl("/rv1AWImgx386ULjcf62VYaW8zSt.jpg");
    }
}
