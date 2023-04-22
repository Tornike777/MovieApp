package com.gvvghost.movieapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gvvghost.movieapp.R;
import com.gvvghost.movieapp.data.author.AuthorReview;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{

    private List<AuthorReview> reviews = new ArrayList<>();

    public void setReviews(List<AuthorReview> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        AuthorReview review = reviews.get(position);
        Integer rating = review.getAuthorDetail().getRating();

        int bgId;
        if (rating != null) {
            if (rating > 7) bgId = android.R.color.holo_green_light;
            else if (rating > 5) bgId = android.R.color.holo_orange_light;
            else bgId = android.R.color.holo_red_light;
        } else bgId = android.R.color.darker_gray;

        holder.cardViewReview.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.getContext(), bgId));
        holder.textViewAuthorName.setText(review.getName());
        holder.textViewReview.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {

        private final CardView cardViewReview;
        private final TextView textViewAuthorName;
        private final TextView textViewReview;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewReview = itemView.findViewById(R.id.cardViewReview);
            textViewAuthorName = itemView.findViewById(R.id.textViewAuthorName);
            textViewReview = itemView.findViewById(R.id.textViewReview);
        }
    }

}
