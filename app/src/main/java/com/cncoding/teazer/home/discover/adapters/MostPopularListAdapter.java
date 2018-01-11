package com.cncoding.teazer.home.discover.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.home.discover.DiscoverFragment.OnDiscoverInteractionListener;
import com.cncoding.teazer.model.post.PostDetails;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_POST;
import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_PROFILE;
import static com.cncoding.teazer.home.post.FragmentPostDetails.SPACE;
import static com.cncoding.teazer.utilities.CommonUtilities.decodeUnicodeString;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class MostPopularListAdapter extends RecyclerView.Adapter<MostPopularListAdapter.ViewHolder> {

    private ArrayList<PostDetails> mostPopularList;
    private Context context;
    private OnDiscoverInteractionListener mListener;

    public MostPopularListAdapter(ArrayList<PostDetails> mostPopularList, Context context, OnDiscoverInteractionListener mListener) {
        this.mostPopularList = mostPopularList;
        this.context = context;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_most_popular_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            holder.shimmerLayout.setVisibility(View.VISIBLE);
            holder.topLayout.setVisibility(View.INVISIBLE);
            holder.bottomLayout.setVisibility(View.INVISIBLE);

            holder.postDetails = mostPopularList.get(position);

            holder.title.setText(decodeUnicodeString(holder.postDetails.getTitle()));
            holder.name.setText(holder.postDetails.getPostOwner().getUserName());
            String durationText = holder.postDetails.getMedias().get(0).getDuration() + " secs";
            holder.duration.setText(durationText);
            String likesText = SPACE + String.valueOf(holder.postDetails.getLikes());
            holder.likes.setText(likesText);
            String viewsText = SPACE + String.valueOf(holder.postDetails.getMedias().get(0).getViews());
            holder.views.setText(viewsText);

            holder.reactions.setVisibility(holder.postDetails.getTotalReactions() == 0 ? View.INVISIBLE : View.VISIBLE);
            String reactionText = "";
            if (holder.postDetails.getTotalReactions() > 2)
                reactionText = "+" + String.valueOf(holder.postDetails.getTotalReactions() - 2) + " R";
            else
                holder.reactions.getLayoutParams().width = 0;
//                reactionText = String.valueOf(holder.postDetails.getTotalReactions()) + " R";
            holder.reactions.setText(reactionText);

            Glide.with(context)
                    .load(holder.postDetails.getPostOwner().getProfileMedia() != null &&
                            holder.postDetails.getPostOwner().hasProfileMedia() ?
                            holder.postDetails.getPostOwner().getProfileMedia().getThumbUrl() : "")
                    .placeholder(R.drawable.ic_user_male_dp_small)
                    .crossFade()
                    .into(holder.dp);

            if (holder.postDetails.getReactedUsers() != null && holder.postDetails.getReactedUsers().size() > 0) {
                holder.reactionImage1.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(holder.postDetails.getReactedUsers().get(0).getProfileMedia() == null ?
                                R.drawable.ic_user_male_dp_small
                                : holder.postDetails.getReactedUsers().get(0).getProfileMedia().getThumbUrl())
                        .placeholder(R.drawable.ic_user_male_dp_small)
                        .crossFade()
                        .into(holder.reactionImage1);
                if (holder.postDetails.getReactedUsers().size() > 1) {
                    holder.reactionImage2.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(holder.postDetails.getReactedUsers().get(1).getProfileMedia() == null ?
                                    R.drawable.ic_user_male_dp_small
                                    : holder.postDetails.getReactedUsers().get(1).getProfileMedia().getThumbUrl())
                            .placeholder(R.drawable.ic_user_male_dp_small)
                            .crossFade()
                            .into(holder.reactionImage2);
                }
            }

            Glide.with(context)
                    .load(holder.postDetails.getMedias().get(0).getThumbUrl())
                    .crossFade()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                       boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.shimmerLayout.setVisibility(View.INVISIBLE);
                            holder.topLayout.setVisibility(View.VISIBLE);
                            holder.bottomLayout.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(holder.thumbnail);
        } catch (Exception e) {
            e.printStackTrace();
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.root_layout:
                        mListener.onDiscoverInteraction(ACTION_VIEW_POST, null, null,
                                holder.postDetails);
                        break;
                    case R.id.dp:
                        mListener.onDiscoverInteraction(ACTION_VIEW_PROFILE, null, null,
                                holder.postDetails);
                        break;
                    case R.id.name:
                        mListener.onDiscoverInteraction(ACTION_VIEW_PROFILE, null, null,
                                holder.postDetails);
                        break;
                    default:
                        break;
                }
            }
        };

        holder.layout.setOnClickListener(listener);
        holder.name.setOnClickListener(listener);
        holder.dp.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return mostPopularList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout) RelativeLayout layout;
        @BindView(R.id.shimmer_layout) RelativeLayout shimmerLayout;
        @BindView(R.id.top_layout) LinearLayout topLayout;
        @BindView(R.id.bottom_layout) RelativeLayout bottomLayout;
        @BindView(R.id.title) ProximaNovaSemiboldTextView title;
        @BindView(R.id.name) ProximaNovaSemiboldTextView name;
        @BindView(R.id.duration) ProximaNovaRegularTextView duration;
        @BindView(R.id.likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.views) ProximaNovaRegularTextView views;
        @BindView(R.id.reaction_count) ProximaNovaSemiboldTextView reactions;
        @BindView(R.id.thumbnail) ImageView thumbnail;
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        @BindView(R.id.reaction_1) CircularAppCompatImageView reactionImage1;
        @BindView(R.id.reaction_2) CircularAppCompatImageView reactionImage2;
        PostDetails postDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}