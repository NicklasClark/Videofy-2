package com.cncoding.teazer.home.camera;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.utilities.PlaceHolderDrawableHelper;

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
    }

    @Override
    public VideoGalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Glide.with(context).load("file://" + videos.get(position).getThumbnail())
                .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
                .crossFade(280)
                .skipMemoryCache(false)
                .into(holder.thumbnailView);

        holder.thumbnailView.setOnClickListener(new View.OnClickListener() {
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

        ViewHolder(View view) {
            super(view);
            thumbnailView = view.findViewById(R.id.video_gallery_thumbnail);
        }
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (context instanceof VideoGalleryAdapterInteractionListener) {
            mListener = (VideoGalleryAdapterInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement VideoGalleryAdapterInteractionListener");
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        mListener = null;
        videos = null;
    }

    interface VideoGalleryAdapterInteractionListener {
        void onVideoGalleryAdapterInteraction(String videoPath);
    }
}
