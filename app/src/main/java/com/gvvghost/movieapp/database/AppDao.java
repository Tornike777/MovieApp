package com.gvvghost.movieapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.gvvghost.movieapp.data.movie.Movie;
import com.gvvghost.movieapp.data.user.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface AppDao {

    @Query("SELECT * FROM favorite_movies")
    Single<List<Movie>> getAllFavoriteMovies();

    @Query("SELECT * FROM favorite_movies WHERE id = :movieId")
    LiveData<Movie> getFavoriteMovie(int movieId);

    @Insert
    Completable insertMovie(Movie movie);

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    Completable removeMovie(int movieId);

    @Query("SELECT * FROM registered_users")
    Single<List<User>> getAllUsers();

    @Query("SELECT * FROM registered_users WHERE email = :userName")
    Single<User> getUser(String userName);

    @Insert
    Completable insertUser(User user);

    @Query("DELETE FROM registered_users WHERE email = :userEmail")
    Completable removeUser(String userEmail);

    @Query("DELETE FROM registered_users")
    Completable removeAll();
}
