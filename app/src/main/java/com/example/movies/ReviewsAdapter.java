package com.example.movies;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    private List<Review> reviewList = new ArrayList<>();
    private final String POSITIVE = "Позитивный";
    private final String NEUTRAL = "Нейтральный";
    private final String NEGATIVE = "Негативный";
    private OnReachEndListener onReachEndListener;

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.textViewAuthor.setText(review.getAuthor());
        holder.textViewReview.setText(review.getReview());

        String type = review.getType();
        int colorResId = android.R.color.holo_orange_light;
        if (type != null) {
            switch (type) {
                case POSITIVE:
                    colorResId = android.R.color.holo_green_light;
                    break;
                case NEGATIVE:
                    colorResId = android.R.color.holo_red_light;
                    break;
            }
        }
        int color = ContextCompat.getColor(holder.itemView.getContext(), colorResId);
        holder.linearLayoutContainer.setBackgroundColor(color);

//        if (position >= reviewList.size() - 10 && onReachEndListener != null) {
//            onReachEndListener.onReachEnd();
//        }
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    interface OnReachEndListener {
        void onReachEnd();
    }



    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewAuthor;
        private final TextView textViewReview;
        private final LinearLayout linearLayoutContainer;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewReview = itemView.findViewById(R.id.textViewReview);
            linearLayoutContainer = itemView.findViewById(R.id.linearLayoutContainer);
        }
    }
}
