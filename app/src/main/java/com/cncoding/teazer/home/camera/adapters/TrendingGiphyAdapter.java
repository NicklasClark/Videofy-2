package com.cncoding.teazer.home.camera.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cncoding.teazer.R;
import com.cncoding.teazer.home.camera.CameraActivity;
import com.cncoding.teazer.model.giphy.Datum;
import com.cncoding.teazer.model.giphy.Images;
import com.cncoding.teazer.utilities.PlaceHolderDrawableHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amit on 1/2/18.
 */


public class TrendingGiphyAdapter extends RecyclerView.Adapter<TrendingGiphyAdapter.ViewHolder> {

    private CameraActivity cameraActivity;
    private List<Datum> giphys;

    public TrendingGiphyAdapter(CameraActivity cameraActivity, List<Datum> videos) {
        this.cameraActivity = cameraActivity;
        this.giphys = videos;
    }

    @Override
    public TrendingGiphyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trending_giphy, parent, false);
        return new TrendingGiphyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrendingGiphyAdapter.ViewHolder holder, int position) {
        try {
            Images images = giphys.get(position).getImages();
            Glide.with(cameraActivity).load(images.getDownsized().getUrl())
                    .apply(new RequestOptions()
                            .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(holder.thumbnailView);

            holder.thumbnailView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cameraActivity.onTrendingGiphyAdapterInteraction(giphys.get(holder.getAdapterPosition()).getImages());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return giphys.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.giphy_thumbnail)
        ImageView thumbnailView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
