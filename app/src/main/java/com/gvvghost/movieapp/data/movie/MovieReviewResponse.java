package com.gvvghost.movieapp.data.movie;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.gvvghost.movieapp.data.author.AuthorReview;

import java.util.List;

public class MovieReviewResponse {

    @SerializedName("id")
    private final int id;

    @SerializedName("results")
    private final List<AuthorReview> movieReviews;

    public MovieReviewResponse(int id, List<AuthorReview> movieReviews) {
        this.id = id;
        this.movieReviews = movieReviews;
    }

    public int getId() {
        return id;
    }

    public List<AuthorReview> getMovieReviews() {
        return movieReviews;
    }

    @NonNull
    @Override
    public String toString() {
        return "MovieReviewResponse{" +
                "id=" + id +
                ", movieReviews=" + movieReviews +
                '}';
    }
}
