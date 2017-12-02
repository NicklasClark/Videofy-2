package com.cncoding.teazer.home.discover.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.home.discover.DiscoverFragment.OnSearchInteractionListener;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_POST;
import static com.cncoding.teazer.customViews.MediaControllerView.SPACE;
import static com.cncoding.teazer.utilities.ViewUtils.BLANK_SPACE;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class MyInterestsListItemAdapter extends RecyclerView.Adapter<MyInterestsListItemAdapter.ViewHolder> {

    private ArrayList<PostDetails> postDetailsArrayList;
    private Context context;
    private OnSearchInteractionListener mListener;

    MyInterestsListItemAdapter(ArrayList<PostDetails> postDetailsArrayList, Context context, OnSearchInteractionListener mListener) {
        this.postDetailsArrayList = postDetailsArrayList;
        this.context = context;
        this.mListener = mListener;
    }

    @Override
    public MyInterestsListItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_interests_list_item, parent, false);
        return new MyInterestsListItemAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyInterestsListItemAdapter.ViewHolder holder, int position) {
        holder.postDetails = postDetailsArrayList.get(position);

        try {
        /*Setting title*/
            holder.title.setText(holder.postDetails.getTitle());

        /*Setting name*/
            String name = holder.postDetails.getPostOwner().getFirstName() + BLANK_SPACE + holder.postDetails.getPostOwner().getLastName();
            holder.name.setText(name);

        /*Setting likes*/
            String likes = SPACE + String.valueOf(holder.postDetails.getLikes());
            holder.likes.setText(likes);

        /*Setting views*/
            String views = SPACE + String.valueOf(holder.postDetails.getMedias().get(0).getViews());
            holder.views.setText(views);

        /*Setting reactions*/
            holder.reactions.setVisibility(holder.postDetails.getTotalReactions() == 0 ? View.GONE : View.VISIBLE);
            String reactionText;
            if (holder.postDetails.getTotalReactions() > 4)
                reactionText = "+" + String.valueOf(holder.postDetails.getTotalReactions() - 3) + " R";
            else
                reactionText = String.valueOf(holder.postDetails.getTotalReactions()) + " R";
            holder.reactions.setText(reactionText);

        /*Setting thumbnail*/
            Glide.with(context)
                    .load(holder.postDetails.getMedias().get(0).getThumbUrl())
                    .placeholder(R.drawable.bg_placeholder)
                    .crossFade()
                    .into(holder.thumbnail);

        /*Setting DP*/
            Glide.with(context)
                    .load(holder.postDetails.getPostOwner().hasProfileMedia() &&
                            holder.postDetails.getPostOwner().getProfileMedia() != null ?
                            holder.postDetails.getPostOwner().getProfileMedia().getThumbUrl() : R.drawable.ic_user_male_dp_small)
                    .placeholder(R.drawable.ic_user_male_dp_small)
                    .crossFade()
                    .into(holder.dp);

        /*Setting reaction thumbnails*/
            if (holder.postDetails.getReactedUsers() != null && holder.postDetails.getTotalReactions() > 0) {
                holder.reactionImage1.setVisibility(View.VISIBLE);
                if (holder.postDetails.getReactedUsers().get(0).hasProfileMedia() &&
                        holder.postDetails.getReactedUsers().get(0).getProfileMedia() != null)
                    Glide.with(context)
                            .load(holder.postDetails.getReactedUsers().get(0).getProfileMedia().getThumbUrl())
                            .placeholder(R.drawable.ic_user_male_dp_small)
                            .crossFade()
                            .into(holder.reactionImage1);

                if (holder.postDetails.getTotalReactions() > 1) {
                    holder.reactionImage2.setVisibility(View.VISIBLE);
                    if (holder.postDetails.getReactedUsers().get(1).hasProfileMedia() &&
                            holder.postDetails.getReactedUsers().get(1).getProfileMedia() != null)
                        Glide.with(context)
                                .load(holder.postDetails.getReactedUsers().get(1).getProfileMedia().getThumbUrl())
                                .placeholder(R.drawable.ic_user_male_dp_small)
                                .crossFade()
                                .into(holder.reactionImage2);
                }
            }

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onSearchInteraction(ACTION_VIEW_POST, null, null,
                            holder.postDetails, null);
                }
            });
        } catch(Resources.NotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return postDetailsArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout) RelativeLayout layout;
        @BindView(R.id.title) ProximaNovaSemiboldTextView title;
        @BindView(R.id.name) ProximaNovaSemiboldTextView name;
        @BindView(R.id.likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.views) ProximaNovaRegularTextView views;
        @BindView(R.id.reaction_count) ProximaNovaRegularTextView reactions;
        @BindView(R.id.video_thumbnail) ImageView thumbnail;
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