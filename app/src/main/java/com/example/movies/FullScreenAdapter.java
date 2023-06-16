package com.example.movies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FullScreenAdapter extends RecyclerView.Adapter<FullScreenAdapter.FullScreenViewHolder> {
    private List<Image> images = new ArrayList<>();
    private OnImageClickListener onImageClickListener;

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public void setImages(List<Image> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FullScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item_fullscreen, parent, false);
        return new FullScreenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FullScreenViewHolder holder, int position) {
        Image image = images.get(position);
        String imageUrl = image.getUrl();
        Glide.with(holder.imageView.getContext())
                .load(imageUrl)
                .into(holder.imageView);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(onImageClickListener != null){
//                    onImageClickListener.onImageClick(image);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class FullScreenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        public FullScreenViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.galleryImageViewFullscreen);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onImageClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onImageClickListener.onImageClick(position);
                }
            }
        }
    }

    interface OnImageClickListener {
        void onImageClick(int position);
    }
}
