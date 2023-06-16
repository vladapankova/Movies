package com.example.movies;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Image implements Serializable {
    @SerializedName("movieId")
    private int movieId;
    @SerializedName("url")
    private String url;

    public Image(int movieId, String url) {
        this.movieId = movieId;
        this.url = url;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Image{" +
                "movieId=" + movieId +
                ", url='" + url + '\'' +
                '}';
    }
}
