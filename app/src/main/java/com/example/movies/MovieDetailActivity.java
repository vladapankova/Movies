package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {
    private final String TAG = "MovieDetailActivity";
    private ImageView imageViewPoster;
    private TextView textViewTitle;
    private TextView textViewYear;
    private TextView textViewDescription;
    private RecyclerView recyclerViewTrailers;
    private RecyclerView recyclerViewReviews;
    private RecyclerView recyclerViewImages;
    private ImageView imageViewStar;
    private static final String EXTRA_MOVIE = "movie";
    private MovieDetailViewModel movieDetailViewModel;
    private TrailersAdapter trailersAdapter;
    private ReviewsAdapter reviewsAdapter;
    private ImagesAdapter imagesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        initViews();
        trailersAdapter = new TrailersAdapter();
        reviewsAdapter = new ReviewsAdapter();
        imagesAdapter = new ImagesAdapter();
        recyclerViewTrailers.setAdapter(trailersAdapter);
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviews.setAdapter(reviewsAdapter);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewImages.setAdapter(imagesAdapter);
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        movieDetailViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
        if (movie.getPoster() != null) {
            Glide.with(this)
                    .load(movie.getPoster().getUrl())
                    .into(imageViewPoster);
        }
        textViewTitle.setText(movie.getName());
        textViewYear.setText(String.valueOf(movie.getYear()));
        textViewDescription.setText(movie.getDescription());
        movieDetailViewModel.loadTrailers(movie.getId());
        movieDetailViewModel.loadReviews(movie.getId());
        movieDetailViewModel.loadImages(movie.getId());
        movieDetailViewModel.getImagesLiveData().observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                imagesAdapter.setImages(images);
            }
        });
        movieDetailViewModel.getTrailerListLiveData().observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(List<Trailer> trailerList) {
                trailersAdapter.setTrailerList(trailerList);
            }
        });
        movieDetailViewModel.getReviewListLiveData().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                reviewsAdapter.setReviewList(reviews);
            }
        });

        trailersAdapter.setOnTrailerClickListener(new TrailersAdapter.OnTrailerClickListener() {
            @Override
            public void onTrailerClick(Trailer trailer) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(trailer.getUrl()));
                startActivity(intent);
            }
        });
        Drawable starOn = ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.star_big_on);
        Drawable starOff = ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.star_big_off);
        movieDetailViewModel.getFavouriteMovie(movie.getId()).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movieFromDb) {
                if (movieFromDb == null) {
                    imageViewStar.setImageDrawable(starOff);
                    imageViewStar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            movieDetailViewModel.insertMovie(movie);
                        }
                    });
                } else {
                    imageViewStar.setImageDrawable(starOn);
                    imageViewStar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            movieDetailViewModel.removeMovie(movie.getId());
                        }
                    });
                }
            }
        });
        imagesAdapter.setOnImageClickListener(new ImagesAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(int position) {
                Intent intent = FullScreenImageActivity.newIntent(getApplicationContext(), position, movie.getId());
                startActivity(intent);
                Log.d(TAG, "интент отправлен");
            }
        });
    }

    private void initViews() {
        imageViewPoster = findViewById(R.id.imageViewPoster);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewYear = findViewById(R.id.textViewYear);
        textViewDescription = findViewById(R.id.textViewDescription);
        recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        recyclerViewImages = findViewById(R.id.recyclerViewImages);
        imageViewStar = findViewById(R.id.imageViewStar);
    }

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }
}