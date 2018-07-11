package com.github.informramiz.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.informramiz.popularmovies.model.Movie;
import com.github.informramiz.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;
import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "movie";

    private ImageView mPosterImageView;
    private TextView mOverviewTextView;
    private TextView mAverageVoteTextView;
    private TextView mReleaseDateTextView;

    private Movie mMovie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intentThatStartThisActivity = getIntent();
        if (intentThatStartThisActivity.hasExtra(EXTRA_MOVIE)) {
            mMovie = intentThatStartThisActivity.getParcelableExtra(EXTRA_MOVIE);
        } else {
            Toast.makeText(this, R.string.error_msg_invalid_movie_data, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mPosterImageView = findViewById(R.id.iv_movie_poster);
        mOverviewTextView = findViewById(R.id.tv_overview);
        mAverageVoteTextView = findViewById(R.id.tv_vote_average);
        mReleaseDateTextView = findViewById(R.id.tv_release_date);

        loadMovieDataIntoViews();
    }

    private void loadMovieDataIntoViews() {
        //set title of movie as screen title
        setTitle(mMovie.getTitle());

        //load movie poster image using Picasso
        URL posterImageURL = NetworkUtils.buildImageUrl(mMovie.getPosterPath());
        try {
            Uri posterImageUri = Uri.parse(posterImageURL.toURI().toString());
            Picasso.get().load(posterImageUri).into(mPosterImageView);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //set synopsis (overview)
        mOverviewTextView.setText(mMovie.getOverview());
        //set user rating (voteAverage)
        mAverageVoteTextView.setText(String.valueOf(mMovie.getVoteAverage()));
        //set release date
        mReleaseDateTextView.setText(mMovie.getReleaseDate());
    }
}
