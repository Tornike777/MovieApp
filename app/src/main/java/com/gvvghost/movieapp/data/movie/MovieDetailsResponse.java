package com.gvvghost.movieapp.data.movie;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieDetailsResponse {

    @SerializedName("id")
    private final int id;

    @SerializedName("results")
    private final List<MovieVideo> movieVideos;

    public MovieDetailsResponse(int id, List<MovieVideo> movieVideos) {
        this.id = id;
        this.movieVideos = movieVideos;
    }

    public int getId() {
        return id;
    }

    public List<MovieVideo> getMovieVideos() {
        return movieVideos;
    }

    @NonNull
    @Override
    public String toString() {
        return "MovieDetailsResponse{" +
                "id=" + id +
                ", movieVideos=" + movieVideos +
                '}';
    }
}
