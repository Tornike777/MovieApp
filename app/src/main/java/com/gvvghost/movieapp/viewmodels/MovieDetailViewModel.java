package com.gvvghost.movieapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.gvvghost.movieapp.data.author.AuthorReview;
import com.gvvghost.movieapp.data.movie.Movie;
import com.gvvghost.movieapp.data.movie.MovieDetailsResponse;
import com.gvvghost.movieapp.data.movie.MovieReviewResponse;
import com.gvvghost.movieapp.data.movie.MovieVideo;
import com.gvvghost.movieapp.database.AppDatabase;
import com.gvvghost.movieapp.database.AppDao;
import com.gvvghost.movieapp.retrofit.ApiFactory;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailViewModel extends AndroidViewModel {

    private final AppDao appDao;
    private static final String TAG = "MovieDetailViewModel";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<List<MovieVideo>> movieVideos = new MutableLiveData<>();
    private final MutableLiveData<List<AuthorReview>> reviews = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<String> getError() {
        return error;
    }

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        this.appDao = AppDatabase.getInstance(application).appDao();
    }

    public LiveData<List<MovieVideo>> getMovieVideo() {
        return movieVideos;
    }

    public LiveData<List<AuthorReview>> getReviews() {
        return reviews;
    }

    public LiveData<Movie> getFavoriteMovies(int movieId) {
        return appDao.getFavoriteMovie(movieId);
    }

    String apiKey = "8c24c04a3d6d549bad94ce9d7aa4c9d6";

    public void loadVideos(int id) {
        Disposable disposable = ApiFactory.apiService.loadMovieDetails(id, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(MovieDetailsResponse::getMovieVideos)
                .subscribe(movieVideos::setValue, throwable -> Log.d(TAG, throwable.toString()));
        compositeDisposable.add(disposable);
    }

    public void loadReviews(int id) {
        Disposable disposable = ApiFactory.apiService.loadMovieReviews(id, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(MovieReviewResponse::getMovieReviews)
                .subscribe(reviews::setValue, throwable -> Log.d(TAG, throwable.toString()));
        compositeDisposable.add(disposable);
    }

    public void insertMovie(Movie movie) {
        Disposable disposable = appDao.insertMovie(movie)
                .subscribeOn(Schedulers.io())
                .subscribe(); //TODO exception handler in subscribe method
        compositeDisposable.add(disposable);

    }

    public void removeMovie(int movieId) {
        Disposable disposable = appDao.removeMovie(movieId)
                .subscribeOn(Schedulers.io())
                .subscribe();
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
