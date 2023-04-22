package com.gvvghost.movieapp.data.movie;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MovieVideo implements Serializable {

    @SerializedName("name")
    private final String name;
    @SerializedName("key")
    private final String key;

    public MovieVideo(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    @NonNull
    @Override
    public String toString() {
        return "MovieVideo{" +
                "name='" + name + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
