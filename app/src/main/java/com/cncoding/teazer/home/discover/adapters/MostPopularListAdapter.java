package com.cncoding.teazer.home.discover.adapters;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.home.BaseRecyclerView;
import com.cncoding.teazer.home.discover.BaseDiscoverFragment;
import com.cncoding.teazer.home.post.detailspage.FragmentPostDetails;
import com.cncoding.teazer.home.profile.ProfileFragment;
import com.cncoding.teazer.model.base.Dimension;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.ui.fragment.activity.OthersProfileFragment;
import com.cncoding.teazer.utilities.diffutil.PostsDiffCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.LayoutInflater.from;
import static com.cncoding.teazer.home.post.detailspage.FragmentPostDetails.SPACE;
import static com.cncoding.teazer.utilities.CommonUtilities.decodeUnicodeString;
import static com.cncoding.teazer.utilities.ViewUtils.adjustViewSize;
import static com.cncoding.teazer.utilities.ViewUtils.getClassicCategoryBackground;
import static com.cncoding.teazer.utilities.ViewUtils.initializeShimmer;
import static com.cncoding.teazer.utilities.ViewUtils.prepareLayout;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class MostPopularListAdapter extends BaseRecyclerView.Adapter {

    private SparseArray<Dimension> dimensionSparseArray;
    private ArrayList<PostDetails> mostPopularList;
    private final BaseDiscoverFragment fragment;

    public MostPopularListAdapter(BaseDiscoverFragment fragment) {
        this.fragment = fragment;
        dimensionSparseArray = new SparseArray<>();
        if (this.mostPopularList == null) this.mostPopularList = new ArrayList<>();
    }

    @Override public MostPopularViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MostPopularViewHolder(from(parent.getContext()).inflate(R.layout.item_most_popular_list, parent, false));
    }

    @Override public int getItemCount() {
        return mostPopularList.size();
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

    public void updatePosts(int page, List<PostDetails> postDetailsList) {
        try {
            if (page == 1) {
                addPosts(postDetailsList);
            } else {
                final DiffUtil.DiffResult result = DiffUtil.calculateDiff(
                        new PostsDiffCallback(new ArrayList<>(mostPopularList.subList(0, 10)), postDetailsList));
                mostPopularList.clear();
                mostPopularList.addAll(postDetailsList);
                fragment.getParentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.dispatchUpdatesTo(MostPopularListAdapter.this);
                    }
                });
            }
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
        try {
            if (mostPopularList == null) mostPopularList = new ArrayList<>();
            else mostPopularList.clear();
            mostPopularList.addAll(postDetailsList);
            notifyDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MostPopularViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.root_layout) RelativeLayout layout;
        @BindView(R.id.title) ProximaNovaSemiBoldTextView title;
        @BindView(R.id.shimmer_layout) RelativeLayout shimmerLayout;
        @BindView(R.id.vignette_layout) FrameLayout vignetteLayout;
        @BindView(R.id.top_layout) RelativeLayout topLayout;
        @BindView(R.id.bottom_layout) RelativeLayout bottomLayout;
        @BindView(R.id.category) ProximaNovaRegularTextView category;
        @BindView(R.id.name) ProximaNovaSemiBoldTextView name;
        @BindView(R.id.likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.views) ProximaNovaRegularTextView views;
        @BindView(R.id.thumbnail) ImageView thumbnail;
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        PostDetails postDetails;

        MostPopularViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override public void bind() {
            initializeShimmer(shimmerLayout, topLayout, bottomLayout, vignetteLayout);

            postDetails = mostPopularList.get(getAdapterPosition());

            /*Adjust view size before loading anything*/
            if (dimensionSparseArray.get(getAdapterPosition()) == null) {
                adjustViewSize(fragment.getContext(), postDetails.getMedias().get(0).getMediaDimension().getWidth(),
                        postDetails.getMedias().get(0).getMediaDimension().getHeight(),
                        layout.getLayoutParams(), getAdapterPosition(), dimensionSparseArray, false);
            } else {
                layout.getLayoutParams().width = dimensionSparseArray.get(getAdapterPosition()).getWidth();
                layout.getLayoutParams().height = dimensionSparseArray.get(getAdapterPosition()).getHeight();
            }

            /*Setting title*/
            title.setText(decodeUnicodeString(postDetails.getTitle()));

            /*Setting category*/
            if (postDetails.getCategories() != null) {
                if (!postDetails.getCategories().isEmpty()) {
                    category.setVisibility(View.VISIBLE);
                    category.setText(postDetails.getCategories() != null ?
                            postDetails.getCategories().get(0).getCategoryName() : "");
                    category.setBackground(getClassicCategoryBackground(category, postDetails.getCategories().get(0).getColor()));
                }
                else category.setVisibility(View.GONE);
            }
            else category.setVisibility(View.GONE);

            /*Setting name*/
            String nameText = postDetails.getPostOwner().getUserName();
            name.setText(nameText);

            /*Setting likes*/
            String likesText = SPACE + String.valueOf(postDetails.getLikes());
            likes.setText(likesText);

            /*Setting views*/
            String viewsText = SPACE + String.valueOf(postDetails.getMedias().get(0).getViews());
            views.setText(viewsText);

            Glide.with(fragment)
                    .load(postDetails.getPostOwner().hasProfileMedia() ?
                            postDetails.getPostOwner().getProfileMedia().getThumbUrl() : R.drawable.ic_user_male_dp_small)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_user_male_dp_small))
                    .into(dp);

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
                            prepareLayout(layout, shimmerLayout, topLayout, bottomLayout,
                                    vignetteLayout, resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                            return false;
                        }
                    })
                    .into(thumbnail);
        }

        @Override public void bind(List<Object> payloads) {
            super.bind(payloads);
        }

        @OnClick(R.id.root_layout) public void viewPost() {
            fragment.navigation.pushFragment(FragmentPostDetails.newInstance(postDetails,
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