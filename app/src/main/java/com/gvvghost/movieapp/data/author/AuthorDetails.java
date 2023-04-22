package com.gvvghost.movieapp.data.author;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class AuthorDetails {
    @SerializedName("rating")
    private final Integer rating;

    public AuthorDetails(Integer rating) {
        this.rating = rating;
    }

    public Integer getRating() {
        return rating;
    }

    @NonNull
    @Override
    public String toString() {
        return "AuthorDetails{" +
                "rating=" + rating +
                '}';
    }
}
