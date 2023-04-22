package com.gvvghost.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.gvvghost.movieapp.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;
    private LoginViewModel viewModel;

    private SharedPreferences sharedPreferences;
    protected static final String MY_PREF = "mypref";
    protected static final String IS_LOGGED_IN = "isLoggedIn";
    protected static final String EMAIL = "email";
    protected static final String PASSWORD = "password";
    private static final String TAG = "LoginActivity";

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        initViews();
        setupListeners();
        observeViewModel();
        autologin();
    }

    private void autologin() {
        sharedPreferences = getApplicationContext()
                .getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(IS_LOGGED_IN)
                && sharedPreferences.getBoolean(IS_LOGGED_IN, false)) {
            if (sharedPreferences.contains(EMAIL) && sharedPreferences.contains(PASSWORD)) {
                String email = sharedPreferences.getString(EMAIL, "");
                String password = sharedPreferences.getString(PASSWORD, "");
                viewModel.login(email, password);
            }
        }
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
    }

    private void setupListeners() {
        buttonLogin.setOnClickListener(view -> viewModel.login(editTextEmail.getText().toString(),
                editTextPassword.getText().toString()));
        buttonRegister.setOnClickListener(view -> startActivity(RegistrationActivity.newIntent(
                LoginActivity.this, editTextEmail.getText().toString())));
    }

    private void observeViewModel() {
        viewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(LoginActivity.this,
                        errorMessage,
                        Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getUser().observe(this, user -> {
            if (user != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(IS_LOGGED_IN, true);
                editor.putString(EMAIL, user.getEmail());
                editor.putString(PASSWORD, user.getPassword());
                editor.apply();
                startActivity(ContentActivity.newIntent(LoginActivity.this,
                        user.getEmail()));
                finish();
            } else {
                Log.d(TAG, "observeViewModel: user is null");
            }
        });
    }
}