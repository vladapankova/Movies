package com.example.movies;

import android.app.Application;
import android.util.Log;

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
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailViewModel extends AndroidViewModel {
    private final String TAG = "MovieDetailViewModel";
    private int page = 1;
    private int pageImage = 1;
    private Boolean isLoading = false;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<List<Trailer>> trailerListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Image>> imagesLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Review>> reviewListLiveData = new MutableLiveData<>();

    public LiveData<List<Image>> getImagesLiveData() {
        return imagesLiveData;
    }

    public LiveData<Movie> getFavouriteMovie(int movieId) {
        return movieDao.getFavouriteMovie(movieId);
    }

    public MutableLiveData<List<Review>> getReviewListLiveData() {
        return reviewListLiveData;
    }

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        movieDao = MovieDatabase.getInstance(getApplication()).movieDao();
    }

    private final MovieDao movieDao;

    public LiveData<List<Trailer>> getTrailerListLiveData() {
        return trailerListLiveData;
    }

    public void loadTrailers(int id) {
        Disposable disposable = ApiFactory.apiService.loadTrailers(ApiService.TOKEN, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<TrailerResponse, List<Trailer>>() {
                    @Override
                    public List<Trailer> apply(TrailerResponse trailerResponse) throws Throwable {
                        return trailerResponse.getTrailersList().getTrailerList();
                    }
                })
                .subscribe(new Consumer<List<Trailer>>() {
                    @Override
                    public void accept(List<Trailer> trailerList) throws Throwable {
                        trailerListLiveData.setValue(trailerList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void loadImages(int id) {
        Disposable disposable = ApiFactory.apiService.loadImages(
                        ApiService.TOKEN, "100", pageImage, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ImageResponse>() {
                    @Override
                    public void accept(ImageResponse imageResponse) throws Throwable {
                        List<Image> images = imageResponse.getImages();
                        List<Image> imageList = imagesLiveData.getValue();
                        if (imageList != null) {
                            imageList.addAll(images);
                            imagesLiveData.setValue(imageList);
                        } else {
                            imagesLiveData.setValue(images);
                        }
                        pageImage++;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, throwable.toString());
                    }
                });
    }

    public void loadReviews(int id) {
        if (isLoading != null && isLoading) {
            return;
        }
        Disposable disposable = ApiFactory.apiService.loadReviews(
                        ApiService.TOKEN, "30", page, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isLoading = true;
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isLoading = false;
                    }
                })
                .subscribe(new Consumer<ReviewResponse>() {
                    @Override
                    public void accept(ReviewResponse reviewResponse) throws Throwable {
                        List<Review> newList = reviewResponse.getReviewList();
                        List<Review> reviewList = reviewListLiveData.getValue();
                        if (reviewList != null) {
                            reviewList.addAll(newList);
                            reviewListLiveData.setValue(reviewList);
                        } else {
                            reviewListLiveData.setValue(newList);
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

    public void insertMovie(Movie movie) {
        Disposable disposable = movieDao.insertMovie(movie)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void removeMovie(int movieId) {
        Disposable disposable = movieDao.removeMovie(movieId)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
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
