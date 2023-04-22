package com.gvvghost.movieapp;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static com.gvvghost.movieapp.LoginActivity.EMAIL;
import static com.gvvghost.movieapp.LoginActivity.IS_LOGGED_IN;
import static com.gvvghost.movieapp.LoginActivity.MY_PREF;
import static com.gvvghost.movieapp.LoginActivity.PASSWORD;
import static com.gvvghost.movieapp.adapters.MoviesAdapter.ViewType.GRID;
import static com.gvvghost.movieapp.adapters.MoviesAdapter.ViewType.LIST;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.gvvghost.movieapp.adapters.MoviesAdapter;
import com.gvvghost.movieapp.adapters.MoviesAdapter.ViewType;
import com.gvvghost.movieapp.network.NetworkStateReceiver;
import com.gvvghost.movieapp.viewmodels.ContentViewModel;

public class ContentActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener{

    private static final String TAG = "ContentActivity";
    private static final String EXTRA_USERNAME = "username";
    private static final String VIEW_TYPE_PARAM = "viewType";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewMovies;
    private MoviesAdapter moviesAdapter;
    private ContentViewModel viewModel;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private MutableLiveData<ViewType> viewType = new MutableLiveData<>();
    private boolean isSetOnMarked;
    private NetworkStateReceiver networkStateReceiver;

    public static Intent newIntent(Context context, String username) {
        Intent intent = new Intent(context, ContentActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        if (savedInstanceState != null)
            viewType.setValue((ViewType) savedInstanceState.get(VIEW_TYPE_PARAM));
        else viewType.setValue(GRID);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initFieldsAndViews();
        setupObservers();
        setupListeners();
        startNetworkBroadcastReceiver(this);
    }

    @Override
    protected void onPause() {
        unregisterNetworkBroadcastReceiver(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        registerNetworkBroadcastReceiver(this);
        super.onResume();
    }

    private void initFieldsAndViews() {
        viewModel = new ViewModelProvider(this).get(ContentViewModel.class);
        gridLayoutManager = new GridLayoutManager(this, isOrientationLandscape() ? 5 : 3);
        linearLayoutManager = new LinearLayoutManager(this);
        drawerLayout = findViewById(R.id.content_drawer_layout);
        navigationView = findViewById(R.id.navigation_menu);
        recyclerViewMovies = findViewById(R.id.recycleViewMovies);
        recyclerViewMovies.setHasFixedSize(true);
        progressBar = findViewById(R.id.progressBarLoading);
        moviesAdapter = new MoviesAdapter();
        moviesAdapter.setVIEW_TYPE(viewType.getValue());
        recyclerViewMovies.setAdapter(moviesAdapter);
        recyclerViewMovies.setLayoutManager(
                (viewType.getValue() == GRID) ? gridLayoutManager : linearLayoutManager);
    }

    private boolean isOrientationLandscape() {
        return getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE;
    }

    private void setupObservers() {
        viewModel.getMovies().observe(this, movies -> moviesAdapter.setMovies(movies));
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) progressBar.setVisibility(View.VISIBLE);
            else progressBar.setVisibility(View.GONE);
        });
        viewType.observe(this, setViewType -> {
            moviesAdapter.setVIEW_TYPE(setViewType);
            recyclerViewMovies.setLayoutManager(
                    setViewType == GRID ? gridLayoutManager : linearLayoutManager);
            recyclerViewMovies.setAdapter(moviesAdapter);
        });
    }

    private void setupListeners() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        moviesAdapter.setOnReachEndListener(() -> viewModel.loadMovies());
        moviesAdapter.setOnMovieClickListener(movie ->
                startActivity(MovieDetailActivity.newIntent(ContentActivity.this, movie)));
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case(R.id.movies_marked):
                    if (!isSetOnMarked) {
                        isSetOnMarked = true;
                        viewModel.loadMarkedMovies();
                        moviesAdapter.setOnReachEndListener(null);
                    }
                    return true;
                    case(R.id.movies_all):
                    if (isSetOnMarked) {
                        isSetOnMarked = false;
                        viewModel.loadMovies(false);
                        moviesAdapter.setOnReachEndListener(() -> viewModel.loadMovies());
                    }
                    return true;
                case(R.id.user_profile):
                    startActivity(UserProfileActivity.newIntent(ContentActivity.this,
                            getIntent().getStringExtra(EXTRA_USERNAME)));
                    return true;
                case (R.id.nav_logout):
                    logout();
                    viewModel.logOut();
                    startActivity(LoginActivity.newIntent(ContentActivity.this));
                    return true;
                default:
                    return false;
            }
        });
    }

    private void logout() {
        SharedPreferences.Editor editor = getApplicationContext()
                .getSharedPreferences(MY_PREF, Context.MODE_PRIVATE).edit();
        editor.putBoolean(IS_LOGGED_IN, false);
        editor.remove(EMAIL);
        editor.remove(PASSWORD);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.simple_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_list_view) viewType.setValue(LIST);
        if (item.getItemId() == R.id.menu_item_table_view) viewType.setValue(GRID);
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(VIEW_TYPE_PARAM, viewType.getValue());
    }

    public void startNetworkBroadcastReceiver(Context currentContext) {
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver
                .addListener((NetworkStateReceiver.NetworkStateReceiverListener) currentContext);

        registerNetworkBroadcastReceiver(currentContext);
    }

    public void registerNetworkBroadcastReceiver(Context currentContext) {
        currentContext.registerReceiver(networkStateReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void unregisterNetworkBroadcastReceiver(Context currentContext) {
        currentContext.unregisterReceiver(networkStateReceiver);
    }

    @Override
    public void networkAvailable() {
        Log.i(TAG, "networkAvailable()");
//        Toast.makeText(this, "network available", Toast.LENGTH_SHORT).show();
        if (viewModel.isMovieListEmpty()) viewModel.loadMovies();
    }

    @Override
    public void networkUnavailable() {
        Log.i(TAG, "networkUnavailable()");
        Toast.makeText(this, "network unavailable", Toast.LENGTH_SHORT).show();
    }
}