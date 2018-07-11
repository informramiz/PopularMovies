package com.github.informramiz.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.Log;

import com.github.informramiz.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Ramiz Raja on 11/07/2018.
 */
public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_API_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String IMAGE_API_BASE_URL = "https://image.tmdb.org/t/p/w185";

    //sort order of movies
    public static final String SORT_ORDER_POPULAR = "popular";
    public static final String SORT_ORDER_TOP_RATED = "top_rated";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef(value = {SORT_ORDER_POPULAR, SORT_ORDER_TOP_RATED})
    public @interface SortOrder {}

    //API param names
    private static final String PARAM_API_KEY = "api_key";

    /**
     * Given the sort order of the movies, this method returns the full HTTP URL of the API
     * @param sortOrder The sorder order of movies, popular, top_rated etc.
     * @return The full HTTP URL of the API
     */
    @Nullable
    public static URL buildMovieApiUrl(@SortOrder String sortOrder) {
        Uri builtUri = Uri.parse(MOVIE_API_BASE_URL).buildUpon()
                .appendPath(sortOrder)
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.API_KEY)
                .build();

        URL url = getURL(builtUri);
        Log.v(LOG_TAG, "Built URI " + url);
        return url;
    }

    /**
     * Given the poster path this method returns the full HTTP URL of that image
     * @param posterPath The relative path of the poster
     * @return The full HTTP URL of the poster
     */
    @Nullable
    public static URL buildImageUrl(String posterPath) {
        Uri builtUri = Uri.parse(IMAGE_API_BASE_URL).buildUpon()
                .appendEncodedPath(posterPath)
                .build();

        URL url = getURL(builtUri);
        Log.v(LOG_TAG, "Built image url " + url);
        return url;
    }

    /**
     * Converts given Uri into URL
     * @param builtUri Uri to convert to URL to
     * @return The converted URL
     */
    @Nullable
    private static URL getURL(Uri builtUri) {
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the result of Http request
     * @param url URL to fetch HTTP response from
     * @return The content of HTTP response
     * @throws IOException related to network or stream reading
     */
    @Nullable
    public static String getResponseFromHttpUrl(@NonNull URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        try {
            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }

    /**
     * Returns true of device is connecting or connected to network
     * @param context The context of app
     * @return true if device is connected to internet
     */
    public static boolean isInternetPresent(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
