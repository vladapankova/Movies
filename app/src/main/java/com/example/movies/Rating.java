package com.example.movies;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rating implements Serializable {
    @SerializedName("kp")
    private double rating;

    public double getRating() {
        return rating;
    }

    public Rating(double rating) {
        this.rating = rating;
    }



    @Override
    public String toString() {
        return "Rating{" +
                "rating='" + rating + '\'' +
                '}';
    }
}
