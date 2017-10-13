package com.cncoding.teazer.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import static com.cncoding.teazer.camera.VideoPreview.VIDEO_PATH;

/**
 *
 * Created by Prem $ on 10/12/2017.
 */

class VideoGalleryAdapter extends RecyclerView.Adapter<VideoGalleryAdapter.ViewHolder> {

    private ArrayList<Videos> videos;
    private Context context;
    private Activity activity;

    VideoGalleryAdapter(ArrayList<Videos> videos, Context context, Activity activity) {
        this.videos = videos;
        this.context = context;
        this.activity = activity;
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
                Intent intent = new Intent(context, VideoPreview.class);
                intent.putExtra(VIDEO_PATH, videos.get(holder.getAdapterPosition()).getPath());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView thumbnailView;
        private FrameLayout selector;

        ViewHolder(View view) {
            super(view);
            thumbnailView = view.findViewById(R.id.video_gallery_thumbnail);
            selector = view.findViewById(R.id.video_gallery_selector);
        }
    }
}
