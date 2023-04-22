package com.gvvghost.movieapp.retrofit;

import com.gvvghost.movieapp.data.movie.MovieDetailsResponse;
import com.gvvghost.movieapp.data.movie.MovieResponse;
import com.gvvghost.movieapp.data.movie.MovieReviewResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("discover/movie?language=en-US&sort_by=popularity.desc&vote_average.gte=4")
    Single<MovieResponse> loadMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{movieId}/videos?language=en-US")
    Single<MovieDetailsResponse> loadMovieDetails(@Path("movieId") int id, @Query("api_key") String apiKey);

    @GET("movie/{movieId}/reviews?language=en-US&page=1")
    Single<MovieReviewResponse> loadMovieReviews(@Path("movieId") int id, @Query("api_key") String apiKey);
}
