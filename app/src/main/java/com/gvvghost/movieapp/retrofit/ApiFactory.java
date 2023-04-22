package com.gvvghost.movieapp.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/";
    public static final String POSTER_SIZE_W92 = "w92";
    public static final String POSTER_SIZE_W154 = "w154";
    public static final String POSTER_SIZE_W185 = "w185";
    public static final String POSTER_SIZE_W324 = "w342";
    public static final String POSTER_SIZE_W500 = "w500";
    public static final String POSTER_SIZE_W780 = "w780";
    public static final String POSTER_SIZE_ORIGINAL = "original";
    public static final String BASE_URL_YOUTUBE = "https://www.youtube.com/";
    public static final String YOUTUBE_WATCH = "watch?v=";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build();

    public static final ApiService apiService = retrofit.create(ApiService.class);
}
