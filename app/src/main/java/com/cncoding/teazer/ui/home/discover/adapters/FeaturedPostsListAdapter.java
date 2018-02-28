package com.cncoding.teazer.ui.home.discover.adapters;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.ui.base.BaseRecyclerView;
import com.cncoding.teazer.ui.customviews.CircularAppCompatImageView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.home.discover.BaseDiscoverFragment;
import com.cncoding.teazer.ui.home.post.detailspage.PostDetailsFragment;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentNewOtherProfile;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentNewProfile2;
import com.cncoding.teazer.utilities.diffutil.PostsDetailsDiffCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v7.util.DiffUtil.calculateDiff;
import static android.view.LayoutInflater.from;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.utilities.common.CommonUtilities.decodeUnicodeString;
import static com.cncoding.teazer.utilities.common.ViewUtils.getGenderSpecificDpSmall;
import static com.cncoding.teazer.utilities.diffutil.PostsDetailsDiffCallback.DIFF_POST_DETAILS;
import static com.cncoding.teazer.utilities.diffutil.PostsDetailsDiffCallback.updatePostDetailsAccordingToDiffBundle;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class FeaturedPostsListAdapter extends BaseRecyclerView.Adapter {

    private ArrayList<PostDetails> featuredPostsList;
    private BaseDiscoverFragment fragment;

    public FeaturedPostsListAdapter(BaseDiscoverFragment fragment) {
        this.fragment = fragment;
        if (this.featuredPostsList == null) this.featuredPostsList = new ArrayList<>();
    }

    @Override public FeaturedPostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeaturedPostsViewHolder(from(parent.getContext()).inflate(R.layout.item_featured_list, parent, false));
    }

    @Override public int getItemCount() {
        return featuredPostsList.size();
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

    public void updatePosts(List<PostDetails> postDetailsList) {
        try {
            final DiffUtil.DiffResult result = calculateDiff(new PostsDetailsDiffCallback(new ArrayList<>(featuredPostsList), postDetailsList));
            featuredPostsList.clear();
            featuredPostsList.addAll(postDetailsList);
            fragment.getParentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    result.dispatchUpdatesTo(FeaturedPostsListAdapter.this);
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
        if (featuredPostsList == null) featuredPostsList = new ArrayList<>();
        else featuredPostsList.clear();
        featuredPostsList.addAll(postDetailsList);
        notifyDataChanged();
    }

    class FeaturedPostsViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.root_layout) RelativeLayout layout;
        @BindView(R.id.shimmer_layout) RelativeLayout shimmerLayout;
        @BindView(R.id.top_layout) LinearLayout topLayout;
        @BindView(R.id.bottom_layout) RelativeLayout bottomLayout;
        @BindView(R.id.title) ProximaNovaSemiBoldTextView title;
        @BindView(R.id.name) ProximaNovaSemiBoldTextView name;
        @BindView(R.id.duration) ProximaNovaRegularTextView duration;
        @BindView(R.id.likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.views) ProximaNovaRegularTextView views;
        @BindView(R.id.thumbnail) ImageView thumbnail;
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        @BindView(R.id.reaction_1) CircularAppCompatImageView reactionImage1;
        @BindView(R.id.reaction_2) CircularAppCompatImageView reactionImage2;
        @BindView(R.id.reaction_count) ProximaNovaSemiBoldTextView reactionCount;
        PostDetails postDetails;

        FeaturedPostsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.root_layout) public void viewPost() {
            fragment.navigation.pushFragment(PostDetailsFragment.newInstance(postDetails,
                    null, false, null));
        }

        @OnClick(R.id.dp) public void dpClicked() {
            viewProfile();
        }

        @OnClick(R.id.name) public void nameClicked() {
            viewProfile();
        }

        private void viewProfile() {
            fragment.navigation.pushFragment(postDetails.canDelete() ?
                    FragmentNewProfile2.newInstance() :
                    FragmentNewOtherProfile.newInstance(String.valueOf(postDetails.getPostOwner().getUserId()),
                            "", postDetails.getPostOwner().getUserName()));
        }

        @Override public void bind() {
            try {
                toggleShimmer(VISIBLE, INVISIBLE);
                postDetails = featuredPostsList.get(getAdapterPosition());

                title.setText(decodeUnicodeString(postDetails.getTitle()));
                name.setText(postDetails.getPostOwner().getUserName());
                String durationText = postDetails.getMedias().get(0).getDuration();
                duration.setText(durationText);
                likes.setText(String.valueOf(postDetails.getLikes()));
                views.setText(String.valueOf(postDetails.getMedias().get(0).getViews()));

                if (postDetails.getTotalReactions() > 2) {
                    reactionCount.setVisibility(VISIBLE);
                    String reactionText = "+" + String.valueOf(postDetails.getTotalReactions() - 2) + " R";
                    reactionCount.setText(reactionText);
                }
                else reactionCount.setVisibility(View.GONE);

                Glide.with(fragment)
                        .load(postDetails.getPostOwner().getProfileMedia() != null ?
                                postDetails.getPostOwner().getProfileMedia().getThumbUrl() :
                                getGenderSpecificDpSmall(postDetails.getPostOwner().getGender()))
                        .apply(new RequestOptions().placeholder(getGenderSpecificDpSmall(postDetails.getPostOwner().getGender())))
                        .into(dp);

                if (postDetails.getReactedUsers() != null && postDetails.getReactedUsers().size() > 0) {
                    reactionImage1.setVisibility(VISIBLE);
                    Glide.with(fragment)
                            .load(postDetails.getReactedUsers().get(0).getProfileMedia() != null ?
                                    postDetails.getReactedUsers().get(0).getProfileMedia().getThumbUrl() :
                                    getGenderSpecificDpSmall(postDetails.getPostOwner().getGender()))
                            .apply(new RequestOptions().placeholder(getGenderSpecificDpSmall(postDetails.getPostOwner().getGender())))
                            .into(reactionImage1);
                    if (postDetails.getReactedUsers().size() > 1) {
                        reactionImage2.setVisibility(VISIBLE);
                        Glide.with(fragment)
                                .load(postDetails.getReactedUsers().get(1).getProfileMedia() != null ?
                                        postDetails.getReactedUsers().get(1).getProfileMedia().getThumbUrl() :
                                        getGenderSpecificDpSmall(postDetails.getPostOwner().getGender()))
                                .apply(new RequestOptions().placeholder(getGenderSpecificDpSmall(postDetails.getPostOwner().getGender())))
                                .into(reactionImage2);
                    } else {
                        reactionImage2.setVisibility(View.GONE);
                        reactionCount.setVisibility(View.GONE);
                    }
                } else {
                    reactionImage1.setVisibility(View.GONE);
                    reactionImage2.setVisibility(View.GONE);
                    reactionCount.setVisibility(View.GONE);
                }

                Glide.with(fragment)
                        .load(postDetails.getMedias().get(0).getThumbUrl())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                           DataSource dataSource, boolean isFirstResource) {
                                toggleShimmer(INVISIBLE, VISIBLE);
                                return false;
                            }
                        })
                        .into(thumbnail);
            } catch (Exception e) {
                e.printStackTrace();
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

        private void toggleShimmer(int shimmerVisibility, int layoutVisibility) {
            shimmerLayout.setVisibility(shimmerVisibility);
            topLayout.setVisibility(layoutVisibility);
            bottomLayout.setVisibility(layoutVisibility);
        }
    }
}