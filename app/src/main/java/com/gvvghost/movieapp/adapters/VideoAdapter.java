package com.gvvghost.movieapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gvvghost.movieapp.R;
import com.gvvghost.movieapp.data.movie.MovieVideo;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{

    private List<MovieVideo> videos = new ArrayList<>();
    private OnVideoClickListener onVideoClickListener;

    public void setVideos(List<MovieVideo> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    public void setOnVideoClickListener(OnVideoClickListener onVideoClickListener) {
        this.onVideoClickListener = onVideoClickListener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        MovieVideo video = videos.get(position);
        holder.textViewVideoName.setText(video.getName());
        holder.itemView.setOnClickListener(view -> {
            if (onVideoClickListener != null) onVideoClickListener.onVideoClick(video);
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewVideoName;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewVideoName = itemView.findViewById(R.id.textViewVideoName);
        }
    }

    public interface OnVideoClickListener {
        void onVideoClick(MovieVideo movieVideo);
    }
}