package com.gvvghost.movieapp.data.movie;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "favorite_movies")
public class Movie implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    private final int id;
    @SerializedName("title")
    private final String title;
    @SerializedName("overview")
    private final String overview;
    @SerializedName("release_date")
    private final String releaseDate;
    @SerializedName("poster_path")
    private final String posterPath;
    @SerializedName("vote_average")
    private final double voteAverage;

    public Movie(int id,
                 String title,
                 String overview,
                 String releaseDate,
                 String posterPath,
                 double voteAverage) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", voteAverage=" + voteAverage +
                '}';
    }
}