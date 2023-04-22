package com.gvvghost.movieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "UserProfileActivity";
    private static final String EXTRA_USERNAME = "username";


    public static Intent newIntent(Context context, String username) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        String username = getIntent().getStringExtra(EXTRA_USERNAME);
        TextView textViewUsername = findViewById(R.id.username);
        textViewUsername.setText(username);
    }


}