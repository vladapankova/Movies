package com.example.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class FavouriteMoviesActivity extends AppCompatActivity {
    private RecyclerView recyclerViewFavourites;
    private MoviesAdapter moviesAdapter;
    private FavouriteMoviesModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movies);
        initViews();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.favourite_movies);
        }
        moviesAdapter = new MoviesAdapter();
        recyclerViewFavourites.setAdapter(moviesAdapter);
        recyclerViewFavourites.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        viewModel = new ViewModelProvider(this).get(FavouriteMoviesModel.class);
        viewModel.getFavouriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                moviesAdapter.setMovies(movies);
            }
        });
        moviesAdapter.setOnMovieClickListener(new MoviesAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                Intent intent = MovieDetailActivity.newIntent(FavouriteMoviesActivity.this, movie);
                startActivity(intent);
            }
        });
    }


    private void initViews() {
        recyclerViewFavourites = findViewById(R.id.recyclerViewFavourites);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, FavouriteMoviesActivity.class);
    }
}