package com.example.movies;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    final String TOKEN = "VN2EXTJ-9P3M14S-MY23R5W-PY0BAKW";
    @GET("v1.3/movie")
    Single<MovieResponse> loadMovies(@Header("X-API-KEY") String header,
                                     @Query("sortField") String sortField,
                                     @Query("sortType") String sortType,
                                   //  @Query("year") String year,
                                     @Query("limit") String limit,
                                     @Query("page") int page);

    @GET("v1.3/movie/{id}")
    Single<TrailerResponse> loadTrailers(@Header("X-API-KEY") String header,
                                         @Path("id") long id);
    @GET("v1/review")
    Single<ReviewResponse> loadReviews(@Header("X-API-KEY") String header,
                                       @Query("limit") String limit,
                                       @Query("page") int page,
                                       @Query("movieId") int movieId);

    @GET("v1/image")
    Single<ImageResponse> loadImages(@Header("X-API-KEY") String header,
                                     @Query("limit") String limit,
                                     @Query("page") int page,
                                     @Query("movieId") int movieId);
}
