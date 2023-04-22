package com.gvvghost.movieapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gvvghost.movieapp.BuildConfig;

import com.gvvghost.movieapp.data.movie.Movie;
import com.gvvghost.movieapp.database.AppDao;
import com.gvvghost.movieapp.database.AppDatabase;
import com.gvvghost.movieapp.retrofit.ApiFactory;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ContentViewModel extends AndroidViewModel {

    private static final String TAG = "ContentViewModel";
    private int page = 1;
    private final AppDao appDao;
    private final MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ContentViewModel(@NonNull Application application) {
        super(application);
        appDao = AppDatabase.getInstance(getApplication()).appDao();
        loadMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public boolean isMovieListEmpty() {
        return movies.getValue() == null || movies.getValue().isEmpty();
    }

    public void loadMovies(){
        loadMovies(true);
    }

    String apiKey = "8c24c04a3d6d549bad94ce9d7aa4c9d6";
    public void loadMovies(boolean addToExistList) {

        Boolean loading = isLoading.getValue();
        if (loading != null && loading) return;
        Disposable disposable = ApiFactory.apiService.loadMovies(apiKey, page)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
                .doAfterTerminate(() -> isLoading.setValue(false))
                .subscribe(
                        movieResponse -> {
                            List<Movie> loadedMovies = movies.getValue();
                            if (addToExistList && loadedMovies != null) {
                                loadedMovies.addAll(movieResponse.getMovies());
                                movies.setValue(loadedMovies);
                            } else movies.setValue(movieResponse.getMovies());
                            page++;
                            Log.d(TAG, "Loaded page: " + page);
                        },
                        throwable -> Log.d(TAG, throwable.toString())
                );
        compositeDisposable.add(disposable);
    }

    public void loadMarkedMovies() {
        page = 1;
        Boolean loading = isLoading.getValue();
        if (loading != null && loading) return;
        Disposable disposable = appDao.getAllFavoriteMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
                .doAfterTerminate(() -> isLoading.setValue(false))
                .subscribe(movies::setValue, throwable -> Log.d(TAG, throwable.toString()));
        compositeDisposable.add(disposable);
    }

    public void logOut(){

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}