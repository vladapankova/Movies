package com.example.movies;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import java.util.List;

public class FullScreenImageActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private int currentPosition;
    private static final String MOVIE_ID = "movie_id";
    private static final String EXTRA_POSITION = "extra_position";
    private MovieDetailViewModel movieDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        int movieId = getIntent().getIntExtra(MOVIE_ID, 0);
        viewPager2 = findViewById(R.id.viewPager2);
        currentPosition = getIntent().getIntExtra(EXTRA_POSITION, 0);
        FullScreenAdapter fullScreenAdapter = new FullScreenAdapter();
        viewPager2.setAdapter(fullScreenAdapter);

        movieDetailViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
        movieDetailViewModel.loadImages(movieId);
        movieDetailViewModel.getImagesLiveData().observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                fullScreenAdapter.setImages(images);
                viewPager2.setCurrentItem(currentPosition);
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }
        });

    }

    public static Intent newIntent(Context context, int position, int movieId) {
        Intent intent = new Intent(context, FullScreenImageActivity.class);
        intent.putExtra(EXTRA_POSITION, position);
        intent.putExtra(MOVIE_ID, movieId);
        return intent;
    }
}