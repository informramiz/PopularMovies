package com.github.informramiz.popularmovies;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.informramiz.popularmovies.model.Movie;
import com.github.informramiz.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Created by Ramiz Raja on 11/07/2018.
 */
public class MoviesRecycleViewAdapter extends RecyclerView.Adapter<MoviesRecycleViewAdapter.MovieItemViewHolder> {
    @Nullable
    private List<Movie> mMovies;
    @Nullable
    private ListItemClickListener mListItemClickListener;

    public MoviesRecycleViewAdapter(@Nullable List<Movie> movies,
                                    @Nullable ListItemClickListener listItemClickListener) {
        this.mMovies = movies;
        this.mListItemClickListener = listItemClickListener;
    }

    public MoviesRecycleViewAdapter(@Nullable ListItemClickListener listItemClickListener) {
        this.mListItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public MovieItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        return new MovieItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieItemViewHolder holder, int position) {
        holder.bind(mMovies.get(position));
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) {
            return 0;
        }

        return mMovies.size();
    }

    public void setListItemClickListener(@Nullable ListItemClickListener listItemClickListener) {
        this.mListItemClickListener = listItemClickListener;
    }

    public void setMovies(@Nullable List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public class MovieItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView posterImageView;

        public MovieItemViewHolder(View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            URL url = NetworkUtils.buildImageUrl(movie.getPosterPath());
            try {
                Picasso.get().load(Uri.parse(url.toURI().toString())).into(posterImageView);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        public void onClick(View view) {
            int clickedMovieIndex = getAdapterPosition();
            if (mListItemClickListener != null
                    && clickedMovieIndex != RecyclerView.NO_POSITION) {
                Movie clickedMovie = mMovies.get(clickedMovieIndex);
                mListItemClickListener.onListItemClick(clickedMovie);
            }
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(Movie clickedMovie);
    }
}
