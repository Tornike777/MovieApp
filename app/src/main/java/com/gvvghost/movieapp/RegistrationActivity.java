package com.gvvghost.movieapp;

import static com.gvvghost.movieapp.LoginActivity.EMAIL;
import static com.gvvghost.movieapp.LoginActivity.IS_LOGGED_IN;
import static com.gvvghost.movieapp.LoginActivity.MY_PREF;
import static com.gvvghost.movieapp.LoginActivity.PASSWORD;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.gvvghost.movieapp.viewmodels.LoginViewModel;

public class RegistrationActivity extends AppCompatActivity {

    private static final String EXTRA_USERNAME = "username";

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;

    private LoginViewModel viewModel;

    public static Intent newIntent(Context context, String username) {
        Intent intent = new Intent(context, RegistrationActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        initViews();
        setupListeners();
        observeViewModel();
        editTextEmail.setText(getIntent().getStringExtra(EXTRA_USERNAME));
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
    }

    private void setupListeners() {
        buttonRegister.setOnClickListener(view ->
                viewModel.register(editTextEmail.getText().toString(),
                        editTextPassword.getText().toString())
        );
    }

    private void observeViewModel() {
        viewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(RegistrationActivity.this,
                        errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getUser().observe(this, user -> {
            if (user != null) {
                SharedPreferences.Editor editor = getApplicationContext()
                        .getSharedPreferences(MY_PREF, Context.MODE_PRIVATE).edit();
                editor.putBoolean(IS_LOGGED_IN, true);
                editor.putString(EMAIL, user.getEmail());
                editor.putString(PASSWORD, user.getPassword());
                editor.apply();
                startActivity(ContentActivity.newIntent(RegistrationActivity.this,
                        user.getEmail()));
                finish();
            }
        });
    }
}