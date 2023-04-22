package com.gvvghost.movieapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gvvghost.movieapp.adapters.ReviewAdapter;
import com.gvvghost.movieapp.adapters.VideoAdapter;
import com.gvvghost.movieapp.data.movie.Movie;
import com.gvvghost.movieapp.viewmodels.MovieDetailViewModel;

public class MovieDetailActivity extends AppCompatActivity {

    // available poster sizes: w92, w154, w185, w342, w500, w780, original
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w342";
    public static final String BASE_URL_YOUTUBE = "https://www.youtube.com/watch?v=";
    private static final String EXTRA_MOVIE = "movie";
    private static final String TAG = "MovieDetailActivity";
    private ImageView imageViewPoster;
    private ImageView imageViewFavorite;
    private TextView textViewMovieTitle;
    private TextView textViewReleaseDate;
    private TextView textViewContent;
    private RecyclerView recycleViewVideos;
    private RecyclerView recycleViewReviews;
    private MovieDetailViewModel viewModel;
    private VideoAdapter videoAdapter;
    private ReviewAdapter reviewAdapter;
    private Movie movie;

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);
        initViewsAndFields();
        setupViews();
        setupObservers();
        setupListeners();
        initContentLoading(movie.getId());
    }

    private void initViewsAndFields() {
        imageViewPoster = findViewById(R.id.imageViewPoster);
        imageViewFavorite = findViewById(R.id.imageViewFavorite);
        textViewMovieTitle = findViewById(R.id.textViewMovieTitle);
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
        textViewContent = findViewById(R.id.textViewContent);
        recycleViewVideos = findViewById(R.id.recycleViewVideos);
        recycleViewReviews = findViewById(R.id.recycleViewReviews);
        viewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
        videoAdapter = new VideoAdapter();
        reviewAdapter = new ReviewAdapter();
        recycleViewVideos.setAdapter(videoAdapter);
        recycleViewReviews.setAdapter(reviewAdapter);
        movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
    }

    private void setupViews() {
        Glide.with(this)
                .load(POSTER_BASE_URL + movie.getPosterPath())
                .into(imageViewPoster);
        textViewMovieTitle.setText(movie.getTitle());
        textViewReleaseDate.setText(movie.getReleaseDate());
        textViewContent.setText(movie.getOverview());
    }

    private void setupObservers() {
        viewModel.getMovieVideo().observe(this,
                movieVideos -> videoAdapter.setVideos(movieVideos));
        viewModel.getReviews().observe(this,
                authorReviews -> reviewAdapter.setReviews(authorReviews));
        Drawable marked = ContextCompat.getDrawable(MovieDetailActivity.this,
                R.drawable.ic_baseline_bookmark_24);
        Drawable unmarked = ContextCompat.getDrawable(MovieDetailActivity.this,
                R.drawable.ic_baseline_bookmark_border_24);
        viewModel.getFavoriteMovies(movie.getId())
                .observe(MovieDetailActivity.this,
                        movieFromDb -> {
                            Log.d(TAG, "setupObservers: movie is " +
                                    (movieFromDb == null ? "null" : "not null"));
                            if (movieFromDb == null) {
                                imageViewFavorite.setImageDrawable(unmarked);
                                imageViewFavorite.setOnClickListener(view ->
                                        viewModel.insertMovie(movie));
                            } else {
                                imageViewFavorite.setImageDrawable(marked);
                                imageViewFavorite.setOnClickListener(view ->
                                        viewModel.removeMovie(movie.getId()));
                            }
                        }
                );
        viewModel.getError().observe(this,
                error -> Toast.makeText(MovieDetailActivity.this,
                        error, Toast.LENGTH_SHORT).show());
    }

    private void setupListeners() {
        videoAdapter.setOnVideoClickListener(movieVideo -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(BASE_URL_YOUTUBE + movieVideo.getKey()));
            startActivity(intent);
        });
    }

    private void initContentLoading(int movieId) {
        viewModel.loadVideos(movieId);
        viewModel.loadReviews(movieId);
    }
}