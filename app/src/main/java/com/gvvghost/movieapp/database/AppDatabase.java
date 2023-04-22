package com.gvvghost.movieapp.database;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.gvvghost.movieapp.data.movie.Movie;
import com.gvvghost.movieapp.data.user.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Movie.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "movieapp.db";
    private static AppDatabase instance = null;
    private static final int NUMBER_OF_THREADS = 4;

    public static AppDatabase getInstance(Application application) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(application, AppDatabase.class, DB_NAME)
                    .build();
        }
        return instance;
    }

    public abstract AppDao appDao();
}