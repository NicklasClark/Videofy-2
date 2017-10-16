package com.cncoding.teazer.camera;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 10/12/2017.
 */

class VideoGalleryAdapter extends RecyclerView.Adapter<VideoGalleryAdapter.ViewHolder> {

    private ArrayList<Videos> videos;
    private Context context;
    private VideoGalleryAdapterInteractionListener mListener;

    VideoGalleryAdapter(ArrayList<Videos> videos, Context context) {
        this.videos = videos;
        this.context = context;

        if (context instanceof VideoGalleryAdapterInteractionListener) {
            mListener = (VideoGalleryAdapterInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement VideoGalleryAdapterInteractionListener");
        }
    }

    @Override
    public VideoGalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_gallery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Glide.with(context).load("file://" + videos.get(position).getThumbnail())
                .skipMemoryCache(false)
                .into(holder.thumbnailView);

        holder.selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onVideoGalleryAdapterInteraction(videos.get(holder.getAdapterPosition()).getPath());
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnailView;
        private FrameLayout selector;

        ViewHolder(View view) {
            super(view);
            thumbnailView = view.findViewById(R.id.video_gallery_thumbnail);
            selector = view.findViewById(R.id.video_gallery_selector);
        }
    }

    interface VideoGalleryAdapterInteractionListener {
        void onVideoGalleryAdapterInteraction(String videoPath);
    }
}
