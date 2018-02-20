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
import com.bumptech.glide.request.RequestOptions;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.home.discover.DiscoverFragment.OnDiscoverInteractionListener;
import com.cncoding.teazer.model.post.PostDetails;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_POST;
import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_PROFILE;
import static com.cncoding.teazer.home.post.detailspage.FragmentPostDetails.SPACE;
import static com.cncoding.teazer.utilities.CommonUtilities.decodeUnicodeString;
import static com.cncoding.teazer.utilities.ViewUtils.BLANK_SPACE;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class MyInterestsListItemAdapter extends RecyclerView.Adapter<MyInterestsListItemAdapter.ViewHolder> {

    private ArrayList<PostDetails> postDetailsArrayList;
    private Context context;
    private OnDiscoverInteractionListener mListener;

    MyInterestsListItemAdapter(ArrayList<PostDetails> postDetailsArrayList, Context context, OnDiscoverInteractionListener mListener) {
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
        if (postDetailsArrayList.size() <= position) {
            holder.thumbnail.setBackgroundColor(context.getResources().getColor(R.color.material_grey200));
            holder.bottomLayout.setVisibility(View.INVISIBLE);
            holder.title.setBackgroundColor(context.getResources().getColor(R.color.material_grey200));
            holder.reactionImage1.setVisibility(View.VISIBLE);
            holder.reactionImage1.setBackgroundResource(R.drawable.bg_placeholder_light_round);
            holder.reactionImage2.setVisibility(View.VISIBLE);
            holder.reactionImage2.setBackgroundResource(R.drawable.bg_placeholder_light_round);
        } else {
            holder.postDetails = postDetailsArrayList.get(position);

            try {
        /*Setting title*/
                holder.title.setText(decodeUnicodeString(holder.postDetails.getTitle()));

        /*Setting name*/
                String name = holder.postDetails.getPostOwner().getFirstName() +
                        BLANK_SPACE + holder.postDetails.getPostOwner().getLastName();
                holder.name.setText(name);

        /*Setting likes*/
                String likes = SPACE + String.valueOf(holder.postDetails.getLikes());
                holder.likes.setText(likes);

        /*Setting views*/
                String views = SPACE + String.valueOf(holder.postDetails.getMedias().get(0).getViews());
                holder.views.setText(views);

        /*Setting reactions*/
                holder.reactions.setVisibility(holder.postDetails.getTotalReactions() == 0 ? View.GONE : View.VISIBLE);
                String reactionText = "";
                if (holder.postDetails.getTotalReactions() > 2)
                    reactionText = "+" + String.valueOf(holder.postDetails.getTotalReactions() - 2) + " R";
//            else
//                reactionText = String.valueOf(holder.postDetails.getTotalReactions()) + " R";
                holder.reactions.setText(reactionText);

        /*Setting thumbnail*/
                Glide.with(context)
                        .load(holder.postDetails.getMedias().get(0).getThumbUrl())
                        .apply(new RequestOptions().placeholder(R.drawable.bg_placeholder))
                        .into(holder.thumbnail);

        /*Setting DP*/
                Glide.with(context)
                        .load(!(holder.postDetails.getPostOwner().hasProfileMedia() &&
                                holder.postDetails.getPostOwner().getProfileMedia() != null) ?
                                R.drawable.ic_user_male_dp_small :
                                holder.postDetails.getPostOwner().getProfileMedia().getThumbUrl())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_user_male_dp_small))
                        .into(holder.dp);

        /*Setting reaction thumbnails*/
                if (holder.postDetails.getReactedUsers() != null && holder.postDetails.getReactedUsers().size() > 0) {
                    holder.reactionImage1.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(holder.postDetails.getReactedUsers().get(0).getProfileMedia() == null ?
                                    R.drawable.ic_user_male_dp_small :
                                    holder.postDetails.getReactedUsers().get(0).getProfileMedia().getThumbUrl())
                            .apply(new RequestOptions().placeholder(R.drawable.ic_user_male_dp_small))
                            .into(holder.reactionImage1);

                    if (holder.postDetails.getReactedUsers().size() > 1) {
                        holder.reactionImage2.setVisibility(View.VISIBLE);
                        Glide.with(context)
                                .load(holder.postDetails.getReactedUsers().get(1).getProfileMedia() == null ?
                                        R.drawable.ic_user_male_dp_small :
                                        holder.postDetails.getReactedUsers().get(1).getProfileMedia().getThumbUrl())
                                .apply(new RequestOptions().placeholder(R.drawable.ic_user_male_dp_small))
                                .into(holder.reactionImage2);
                    } else {
                        holder.reactionImage2.setVisibility(View.VISIBLE);
                        holder.reactionImage2.setBackgroundResource(R.drawable.bg_placeholder_light_round);
                    }
                } else {
                    holder.reactionImage1.setVisibility(View.VISIBLE);
                    holder.reactionImage1.setBackgroundResource(R.drawable.bg_placeholder_light_round);
                    holder.reactionImage2.setVisibility(View.VISIBLE);
                    holder.reactionImage2.setBackgroundResource(R.drawable.bg_placeholder_light_round);
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
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        try {
            return postDetailsArrayList.size() < 3 ? 3 : postDetailsArrayList.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout) RelativeLayout layout;
        @BindView(R.id.bottom_layout) RelativeLayout bottomLayout;
        @BindView(R.id.title) ProximaNovaSemiBoldTextView title;
        @BindView(R.id.name) ProximaNovaSemiBoldTextView name;
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