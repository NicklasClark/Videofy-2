package com.cncoding.teazer.home.camera;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.utilities.PlaceHolderDrawableHelper;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

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
            final long duration = videos.get(position).getDuration();
            holder.duration.setText(String.format(Locale.getDefault(), "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))));

            Glide.with(cameraActivity).load("file://" + videos.get(position).getPath())
                    .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
                    .crossFade()
                    .skipMemoryCache(false)
                    .into(holder.thumbnailView);

            holder.thumbnailView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (duration >= 3000)
                        cameraActivity.onVideoGalleryAdapterInteraction(videos.get(holder.getAdapterPosition()).getPath());
//                    else
//                        Toast.makeText(cameraActivity, "Selected video should at least be 3 seconds", Toast.LENGTH_SHORT).show();
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

        @BindView(R.id.video_gallery_thumbnail) ImageView thumbnailView;
        @BindView(R.id.duration) ProximaNovaRegularTextView duration;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
