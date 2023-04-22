package com.gvvghost.movieapp.viewmodels;

import android.app.Application;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gvvghost.movieapp.data.user.User;
import com.gvvghost.movieapp.database.AppDao;
import com.gvvghost.movieapp.database.AppDatabase;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginViewModel extends AndroidViewModel {

    private final String TAG = "LoginViewModel";
    private final int MIN_PASSWORD_LENGTH = 5;
    private final AppDao appDao;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<User> user = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        appDao = AppDatabase.getInstance(getApplication()).appDao();
        Log.d(TAG, "LoginViewModel: userDao " + appDao);
        allRegisteredUsers();
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<User> getUser() {
        return user;
    }


    public void login(String email, String password) {
        if (isUserNameNotValid(email)) error.setValue("Invalid username");
        else if (isPasswordNotValid(password)) error.setValue("Invalid password");
        else {
            validateUser(email, userFromDB -> {
                if (userFromDB == null) error.setValue("User not exist");
                else if (password.equals(userFromDB.getPassword())) user.setValue(userFromDB);
                else error.setValue("Incorrect password");
            }, throwable -> {
                error.setValue("login error");
                Log.d(TAG, "login error: " + throwable.toString());
            });
        }
    }

    public void register(String email, String password) {
        if (isUserNameNotValid(email)) error.setValue("Invalid username");
        else if (isPasswordNotValid(password)) error.setValue("Invalid password");
        else {
            validateUser(email, userFromDB -> {
                if (userFromDB != null) error.setValue("User already exist");
            }, throwable -> {
                User newUser = new User(email, password);
                Disposable disposable = appDao.insertUser(newUser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> this.user.setValue(newUser),
                                throwable1 -> Log.d(TAG, "There is a problem with " +
                                        "registration. Try again or reload/reinstall app"));
                compositeDisposable.add(disposable);
            });
        }
    }

    private void validateUser(String email,
                              Consumer<User> onSuccess,
                              Consumer<Throwable> onError) {
        Disposable disposable = appDao.getUser(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError);
        compositeDisposable.add(disposable);
    }

    private void allRegisteredUsers() {
        Disposable disposable = appDao.getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> Log.d(TAG, "AllRegisteredUsers: " + users));
        compositeDisposable.add(disposable);
    }



    private boolean isUserNameNotValid(String username) {
        if (username == null) return true;
        if (username.contains("@")) return !Patterns.EMAIL_ADDRESS.matcher(username).matches();
        else return username.trim().isEmpty();
    }

    private boolean isPasswordNotValid(String password) {
        return password == null || password.trim().length() <= MIN_PASSWORD_LENGTH;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
