package com.cncoding.teazer.ui.home.camera.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.giphy.Datum;
import com.cncoding.teazer.data.model.giphy.Images;
import com.cncoding.teazer.ui.base.BaseRecyclerView;
import com.cncoding.teazer.ui.home.camera.CameraActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cncoding.teazer.utilities.common.PlaceHolderDrawableHelper.getBackgroundDrawable;

/**
 *
 * Created by amit on 1/2/18.
 */


public class TrendingGiphyAdapter extends BaseRecyclerView.Adapter {

    private CameraActivity cameraActivity;
    private List<Datum> giphys;

    public TrendingGiphyAdapter(CameraActivity cameraActivity) {
        this.cameraActivity = cameraActivity;
        this.giphys = new ArrayList<>();
    }

    @Override
    public TrendingGiphyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trending_giphy, parent, false);
        return new TrendingGiphyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return giphys.size();
    }

    @Override
    public void release() {}

    @Override
    public void notifyDataChanged() {}

    private void notifyItemChange(final int positionStart, final int itemCount) {
        cameraActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyItemRangeChanged(positionStart, itemCount);
            }
        });
    }

    public void addPosts(int offset, List<Datum> giphys) {
        this.giphys.addAll(giphys);
        notifyItemChange(offset, giphys.size());
    }

    public void clearData() {
        giphys.clear();
    }

    class TrendingGiphyViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.giphy_thumbnail) ImageView thumbnailView;

        TrendingGiphyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bind() {
            try {
                Images images = giphys.get(getAdapterPosition()).getImages();
                Glide.with(cameraActivity).load(images.getDownsized().getUrl())
                        .apply(new RequestOptions().placeholder(getBackgroundDrawable(getAdapterPosition()))
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                        .into(thumbnailView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @OnClick(R.id.giphy_thumbnail) public void gifSelected() {
            cameraActivity.onTrendingGiphyAdapterInteraction(giphys.get(getAdapterPosition()).getImages());
        }
    }
}
