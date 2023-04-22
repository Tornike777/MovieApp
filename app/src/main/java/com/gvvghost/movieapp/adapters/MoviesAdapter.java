package com.gvvghost.movieapp.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gvvghost.movieapp.R;
import com.gvvghost.movieapp.data.movie.Movie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // available poster sizes: w92, w154, w185, w342, w500, w780, original
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w154";
    private List<Movie> movies = new ArrayList<>();
    private OnReachEndListener onReachEndListener;
    private OnMovieClickListener onMovieClickListener;
    private ViewType VIEW_TYPE = ViewType.GRID;


    public void setVIEW_TYPE(ViewType VIEW_TYPE) {
        this.VIEW_TYPE = VIEW_TYPE;
        notifyDataSetChanged();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }


    public void setOnReachEndListener(@Nullable OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    public void setOnMovieClickListener(OnMovieClickListener onMovieClickListener) {
        this.onMovieClickListener = onMovieClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (VIEW_TYPE == ViewType.GRID) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_card_item, parent, false);
            return new GridViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_item, parent, false);
            return new ListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        final Double rating = movie.getVoteAverage();
        int difForPreload = 6;
        if (VIEW_TYPE == ViewType.GRID && holder instanceof GridViewHolder) {
            GridViewHolder gridViewHolder = (GridViewHolder) holder;
            int backgroundId;
            Glide.with(gridViewHolder.itemView)
                    .load(POSTER_BASE_URL + movie.getPosterPath())
                    .into(gridViewHolder.imageViewPoster);
            if (rating > 8) backgroundId = R.drawable.rating_bg_green;
            else if (rating > 6) backgroundId = R.drawable.rating_bg_orange;
            else backgroundId = R.drawable.rating_bg_red;
            Drawable background = ContextCompat
                    .getDrawable(holder.itemView.getContext(), backgroundId);
            gridViewHolder.textViewMovieTitle.setText(movie.getTitle());
            gridViewHolder.textViewRating.setBackground(background);
            gridViewHolder.textViewRating.setText(String.valueOf(rating));
        } else if (VIEW_TYPE == ViewType.LIST && holder instanceof ListViewHolder) {
            difForPreload = 3;
            ListViewHolder listViewHolder = (ListViewHolder) holder;
            Glide.with(listViewHolder.itemView)
                    .load(POSTER_BASE_URL + movie.getPosterPath())
                    .into(listViewHolder.imageViewPoster);
            listViewHolder.textViewMovieTitle.setText(movie.getTitle());
            listViewHolder.textViewReleaseDate.setText(movie.getReleaseDate());
            listViewHolder.textViewContent.setText(movie.getOverview());
            listViewHolder.textViewRating.setText(String.valueOf(rating));
        }
        if (onReachEndListener != null && position >= (movies.size() - difForPreload))
            onReachEndListener.onReachEnd();
        holder.itemView.setOnClickListener(view -> {
            if (onMovieClickListener != null) onMovieClickListener.onMovieClick(movie);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class GridViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewPoster;
        private final TextView textViewMovieTitle;
        private final TextView textViewRating;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPoster = itemView.findViewById(R.id.imageViewPoster);
            textViewMovieTitle = itemView.findViewById(R.id.textViewMovieTitle);
            textViewRating = itemView.findViewById(R.id.textViewRating);
        }
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewPoster;
        private final TextView textViewMovieTitle;
        private final TextView textViewReleaseDate;
        private final TextView textViewContent;
        private final TextView textViewRating;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPoster = itemView.findViewById(R.id.imageViewPoster);
            textViewMovieTitle = itemView.findViewById(R.id.textViewMovieTitle);
            textViewReleaseDate = itemView.findViewById(R.id.textViewReleaseDate);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            textViewRating = itemView.findViewById(R.id.textViewRating);
        }
    }

    public enum ViewType implements Serializable {

        GRID("Grid"),
        LIST("List");

        private final String name;

        ViewType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public interface OnReachEndListener {
        void onReachEnd();
    }

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }
}