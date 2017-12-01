package com.cncoding.teazer.home.discover.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.utilities.ViewUtils.BLANK_SPACE;
import static com.cncoding.teazer.utilities.ViewUtils.getByteArrayFromImage;

/**
 *
 * Created by Prem $ on 11/24/2017.
 */

public class SubDiscoverAdapter extends RecyclerView.Adapter<SubDiscoverAdapter.ViewHolder> {

    private OnSubSearchInteractionListener mListener;
    private ArrayList<PostDetails> postDetailsArrayList;
    private SparseIntArray dimensionSparseArray;
    private Context context;

    public SubDiscoverAdapter(ArrayList<PostDetails> postDetailsArrayList, Context context) {
        this.postDetailsArrayList = postDetailsArrayList;
        this.context = context;
        dimensionSparseArray = new SparseIntArray();
        if (context instanceof OnSubSearchInteractionListener) {
            mListener = (SubDiscoverAdapter.OnSubSearchInteractionListener) context;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_discover_post, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            holder.postDetails = postDetailsArrayList.get(position);

            if (dimensionSparseArray.get(position) != 0) {
                holder.layout.getLayoutParams().height = dimensionSparseArray.get(position);
                holder.layout.setVisibility(View.VISIBLE);
            }

            holder.title.setText(holder.postDetails.getTitle());

            holder.category.setVisibility(holder.postDetails.getCategories() != null && holder.postDetails.getCategories().size() > 0 ?
                    View.VISIBLE : View.GONE);
            if (holder.postDetails.getCategories() != null && holder.postDetails.getCategories().size() > 0)
                holder.category.setText(holder.postDetails.getCategories().get(0).getCategoryName());

            String name = holder.postDetails.getPostOwner().getFirstName() + BLANK_SPACE + holder.postDetails.getPostOwner().getLastName();
            holder.name.setText(name);

            String likes = BLANK_SPACE + String.valueOf(holder.postDetails.getLikes());
            holder.likes.setText(likes);

            String views = BLANK_SPACE + String.valueOf(holder.postDetails.getMedias().get(0).getViews());
            holder.views.setText(views);

            if (holder.postDetails.getPostOwner().hasProfileMedia() && holder.postDetails.getPostOwner().getProfileMedia() != null) {
                Glide.with(context)
                        .load(holder.postDetails.getPostOwner().getProfileMedia().getThumbUrl())
                        .placeholder(R.drawable.ic_user_male_dp_small)
                        .crossFade()
                        .into(holder.profilePic);
            } else {
                Glide.with(context)
                        .load(R.drawable.ic_user_male_dp_small)
                        .crossFade()
                        .into(holder.profilePic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Glide.with(context)
                .load(holder.postDetails.getMedias().get(0).getThumbUrl())
                .placeholder(R.drawable.bg_placeholder)
                .crossFade()
                .skipMemoryCache(false)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        int height = (holder.layout.getWidth() * resource.getIntrinsicHeight()) / resource.getIntrinsicWidth();
                        if (height < holder.layout.getWidth())
                            height = holder.layout.getWidth();

                        holder.layout.getLayoutParams().height = height;

                        dimensionSparseArray.put(holder.getAdapterPosition(), height);
//                        holder.layout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fast_fade_in));
                        holder.layout.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.postThumbnail);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onSubSearchInteraction(holder.postDetails, getByteArrayFromImage(holder.postThumbnail));
            }
        });
    }

    @Override
    public int getItemCount() {
        return postDetailsArrayList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout) RelativeLayout layout;
        @BindView(R.id.title) ProximaNovaSemiboldTextView title;
        @BindView(R.id.category) ProximaNovaRegularTextView category;
        @BindView(R.id.name) ProximaNovaSemiboldTextView name;
        @BindView(R.id.likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.views) ProximaNovaRegularTextView views;
        @BindView(R.id.thumbnail) ImageView postThumbnail;
        @BindView(R.id.dp) CircularAppCompatImageView profilePic;
        PostDetails postDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnSubSearchInteractionListener {
        void onSubSearchInteraction(PostDetails postDetails, byte[] byteArrayFromImage);
    }
}