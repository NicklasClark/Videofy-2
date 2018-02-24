package com.cncoding.teazer.home.discover.adapters;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
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
import com.cncoding.teazer.home.BaseRecyclerView;
import com.cncoding.teazer.home.discover.BaseDiscoverFragment;
import com.cncoding.teazer.home.post.detailspage.PostDetailsFragment;
import com.cncoding.teazer.home.profile.ProfileFragment;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.ui.fragment.activity.OthersProfileFragment;
import com.cncoding.teazer.utilities.diffutil.PostsDiffCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v7.util.DiffUtil.calculateDiff;
import static android.view.LayoutInflater.from;
import static com.cncoding.teazer.R.layout.item_my_interests_list_item;
import static com.cncoding.teazer.home.post.detailspage.PostDetailsFragment.SPACE;
import static com.cncoding.teazer.utilities.CommonUtilities.decodeUnicodeString;
import static com.cncoding.teazer.utilities.ViewUtils.BLANK_SPACE;
import static com.cncoding.teazer.utilities.diffutil.PostsDiffCallback.DIFF_POST_DETAILS;
import static com.cncoding.teazer.utilities.diffutil.PostsDiffCallback.updatePostDetailsAccordingToDiffBundle;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class MyInterestsListItemAdapter extends BaseRecyclerView.Adapter {

    private ArrayList<PostDetails> postDetailsArrayList;
    private BaseDiscoverFragment fragment;

    MyInterestsListItemAdapter(ArrayList<PostDetails> postDetailsArrayList, BaseDiscoverFragment fragment) {
        this.postDetailsArrayList = postDetailsArrayList;
        this.fragment = fragment;
    }

    @Override public MyInterestsListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyInterestsListItemViewHolder(from(parent.getContext()).inflate(item_my_interests_list_item, parent, false));
    }

    @Override public int getItemCount() {
        try {
            return postDetailsArrayList.size() < 3 ? 3 : postDetailsArrayList.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override public void release() {}

    @Override
    public void notifyDataChanged() {
        fragment.getParentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    void updatePosts(List<PostDetails> postDetailsList) {
        try {
            final DiffUtil.DiffResult result = calculateDiff(new PostsDiffCallback(new ArrayList<>(postDetailsArrayList), postDetailsList));
            postDetailsArrayList.clear();
            postDetailsArrayList.addAll(postDetailsList);
            fragment.getParentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    result.dispatchUpdatesTo(MyInterestsListItemAdapter.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            try {
                addPosts(postDetailsList);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void addPosts(List<PostDetails> postDetailsList) {
        if (postDetailsArrayList == null) postDetailsArrayList = new ArrayList<>();
        else postDetailsArrayList.clear();
        postDetailsArrayList.addAll(postDetailsList);
        notifyDataChanged();
    }

    class MyInterestsListItemViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.root_layout) RelativeLayout layout;
        @BindView(R.id.bottom_layout) RelativeLayout bottomLayout;
        @BindView(R.id.title) ProximaNovaSemiBoldTextView title;
        @BindView(R.id.name) ProximaNovaSemiBoldTextView name;
        @BindView(R.id.likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.views) ProximaNovaRegularTextView views;
        @BindView(R.id.reaction_count) ProximaNovaRegularTextView reactionCount;
        @BindView(R.id.video_thumbnail) ImageView thumbnail;
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        @BindView(R.id.reaction_1) CircularAppCompatImageView reactionImage1;
        @BindView(R.id.reaction_2) CircularAppCompatImageView reactionImage2;
        PostDetails postDetails;

        MyInterestsListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override public void bind() {
            if (postDetailsArrayList.size() <= getAdapterPosition()) {
                thumbnail.setBackgroundColor(fragment.getResources().getColor(R.color.material_grey200));
                bottomLayout.setVisibility(View.INVISIBLE);
                title.setBackgroundColor(fragment.getResources().getColor(R.color.material_grey200));
                reactionImage1.setVisibility(View.VISIBLE);
                reactionImage1.setBackgroundResource(R.drawable.bg_placeholder_light_round);
                reactionImage2.setVisibility(View.VISIBLE);
                reactionImage2.setBackgroundResource(R.drawable.bg_placeholder_light_round);
            }
            else {
                postDetails = postDetailsArrayList.get(getAdapterPosition());

                try {
                    /*Setting title*/
                    title.setText(decodeUnicodeString(postDetails.getTitle()));

                    /*Setting name*/
                    String nameText = postDetails.getPostOwner().getFirstName() +
                            BLANK_SPACE + postDetails.getPostOwner().getLastName();
                    name.setText(nameText);

                    /*Setting likes*/
                    String likeText = SPACE + String.valueOf(postDetails.getLikes());
                    likes.setText(likeText);

                    /*Setting views*/
                    String viewText = SPACE + String.valueOf(postDetails.getMedias().get(0).getViews());
                    views.setText(viewText);

                    /*Setting reaction count*/
                    reactionCount.setVisibility(postDetails.getTotalReactions() == 0 ? View.GONE : View.VISIBLE);
                    String reactionText = null;
                    if (postDetails.getTotalReactions() > 2)
                        reactionText = "+" + String.valueOf(postDetails.getTotalReactions() - 2) + " R";
                    reactionCount.setText(reactionText);

                    /*Setting thumbnail*/
                    Glide.with(fragment)
                            .load(postDetails.getMedias().get(0).getThumbUrl())
                            .apply(new RequestOptions().placeholder(R.drawable.bg_placeholder))
                            .into(thumbnail);

                    /*Setting DP*/
                    Glide.with(fragment)
                            .load(!(postDetails.getPostOwner().hasProfileMedia() &&
                                    postDetails.getPostOwner().getProfileMedia() != null) ?
                                    R.drawable.ic_user_male_dp_small :
                                    postDetails.getPostOwner().getProfileMedia().getThumbUrl())
                            .apply(new RequestOptions().placeholder(R.drawable.ic_user_male_dp_small))
                            .into(dp);

                    /*Setting reaction thumbnails*/
                    if (postDetails.getReactedUsers() != null && postDetails.getReactedUsers().size() > 0) {
                        reactionImage1.setVisibility(View.VISIBLE);
                        Glide.with(fragment)
                                .load(postDetails.getReactedUsers().get(0).getProfileMedia() == null ?
                                        R.drawable.ic_user_male_dp_small :
                                        postDetails.getReactedUsers().get(0).getProfileMedia().getThumbUrl())
                                .apply(new RequestOptions().placeholder(R.drawable.ic_user_male_dp_small))
                                .into(reactionImage1);

                        if (postDetails.getReactedUsers().size() > 1) {
                            reactionImage2.setVisibility(View.VISIBLE);
                            Glide.with(fragment)
                                    .load(postDetails.getReactedUsers().get(1).getProfileMedia() == null ?
                                            R.drawable.ic_user_male_dp_small :
                                            postDetails.getReactedUsers().get(1).getProfileMedia().getThumbUrl())
                                    .apply(new RequestOptions().placeholder(R.drawable.ic_user_male_dp_small))
                                    .into(reactionImage2);
                        } else {
                            reactionImage2.setVisibility(View.VISIBLE);
                            reactionImage2.setBackgroundResource(R.drawable.bg_placeholder_light_round);
                        }
                    } else {
                        reactionImage1.setVisibility(View.VISIBLE);
                        reactionImage1.setBackgroundResource(R.drawable.bg_placeholder_light_round);
                        reactionImage2.setVisibility(View.VISIBLE);
                        reactionImage2.setBackgroundResource(R.drawable.bg_placeholder_light_round);
                    }
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override public void bind(List<Object> payloads) {
            if (payloads == null || payloads.isEmpty()) return;

            if (payloads.get(0) instanceof PostDetails) {
                bind();
                return;
            }

            Bundle bundle = (Bundle) payloads.get(0);
            if (bundle.containsKey(DIFF_POST_DETAILS)) {
                postDetails = bundle.getParcelable(DIFF_POST_DETAILS);
                return;
            }

            updatePostDetailsAccordingToDiffBundle(postDetails, bundle);
        }

        @OnClick(R.id.root_layout) public void viewPost() {
            fragment.navigation.pushFragment(PostDetailsFragment.newInstance(postDetails,
                    null, false, false, null, null));
        }

        @OnClick(R.id.dp) public void dpClicked() {
            viewProfile();
        }

        @OnClick(R.id.name) public void nameClicked() {
            viewProfile();
        }

        private void viewProfile() {
            fragment.navigation.pushFragment(postDetails.canDelete() ?
                    ProfileFragment.newInstance() :
                    OthersProfileFragment.newInstance(String.valueOf(postDetails.getPostOwner().getUserId()),
                            "", postDetails.getPostOwner().getUserName()));
        }
    }
}