package com.example.movies;

import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("movieId")
    private int movieId;
    @SerializedName("title")
    private String title;
    @SerializedName("type")
    private String type;
    @SerializedName("review")
    private String review;
    @SerializedName("author")
    private String author;

    public Review(int movieId, String title, String type, String review, String author) {
        this.movieId = movieId;
        this.title = title;
        this.type = type;
        this.review = review;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getReview() {
        return review;
    }

    public String getAuthor() {
        return author;
    }

    public int getMovieId() {
        return movieId;
    }

    @Override
    public String toString() {
        return "Review{" +
                "movieId=" + movieId +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", review='" + review + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
