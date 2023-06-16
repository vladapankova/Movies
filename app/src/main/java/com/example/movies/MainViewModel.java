package com.example.movies;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {
    private final String TAG = "MainViewModel";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int page = 1;
    private final MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
        loadMoviesRx();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void loadMoviesRx() {
        Boolean loading = isLoading.getValue();
        if (loading != null && loading) {
            return;
        }
        Disposable disposable = ApiFactory.apiService.loadMovies(
                        ApiService.TOKEN,
                        "votes.kp", // чтобы были первые фильмы с постерами, не ноунэйм
                        "-1",
                    //    "2008",
                        "10",
                        page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isLoading.setValue(true);
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isLoading.setValue(false);
                    }
                })
                .subscribe(new Consumer<MovieResponse>() {
                    @Override
                    public void accept(MovieResponse movieResponse) throws Throwable {
                        List<Movie> loadedMovies = movies.getValue();
                        if (loadedMovies != null) {
                            loadedMovies.addAll(movieResponse.getMovies());
                            movies.setValue(loadedMovies);
                        } else {
                            movies.setValue(movieResponse.getMovies());
                        }
                        page++;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
