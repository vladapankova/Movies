package com.example.movies;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavouriteMoviesModel extends AndroidViewModel {
    private MovieDao movieDao;

    public FavouriteMoviesModel(@NonNull Application application) {
        super(application);
        movieDao = MovieDatabase.getInstance(getApplication()).movieDao();
    }

    public LiveData<List<Movie>> getFavouriteMovies() {
     return    movieDao.getAllFavouriteMovies();
    }
}
