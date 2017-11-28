package com.cncoding.teazer.home.camera;

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

    private CameraActivity cameraActivity;
    private ArrayList<Videos> videos;

    VideoGalleryAdapter(CameraActivity cameraActivity, ArrayList<Videos> videos) {
        this.cameraActivity = cameraActivity;
        this.videos = videos;
    }

    @Override
    public VideoGalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            Glide.with(cameraActivity).load("file://" + videos.get(position).getThumbnail())
                    .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
                    .crossFade(280)
                    .skipMemoryCache(false)
                    .into(holder.thumbnailView);

            holder.thumbnailView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cameraActivity.onVideoGalleryAdapterInteraction(videos.get(holder.getAdapterPosition()).getPath());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
