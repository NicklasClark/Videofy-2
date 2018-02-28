package com.cncoding.teazer.ui.home.discover.adapters;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.base.Dimension;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.ui.base.BaseRecyclerView;
import com.cncoding.teazer.ui.customviews.common.CircularAppCompatImageView;
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
import static com.cncoding.teazer.utilities.common.CommonUtilities.decodeUnicodeString;
import static com.cncoding.teazer.utilities.common.ViewUtils.BLANK_SPACE;
import static com.cncoding.teazer.utilities.common.ViewUtils.adjustViewSize;
import static com.cncoding.teazer.utilities.common.ViewUtils.getClassicCategoryBackground;
import static com.cncoding.teazer.utilities.common.ViewUtils.initializeShimmer;
import static com.cncoding.teazer.utilities.common.ViewUtils.prepareLayout;

/**
 *
 * Created by Prem $ on 11/24/2017.
 */

public class SubDiscoverAdapter extends BaseRecyclerView.Adapter {

    private ArrayList<PostDetails> postDetailsArrayList;
    private SparseArray<Dimension> dimensionSparseArray;
    private BaseDiscoverFragment fragment;

    public SubDiscoverAdapter(BaseDiscoverFragment fragment) {
        this.fragment = fragment;
        if (postDetailsArrayList == null) postDetailsArrayList = new ArrayList<>();
        if (dimensionSparseArray == null) dimensionSparseArray = new SparseArray<>();
    }

    @Override
    public SubDiscoverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubDiscoverViewHolder(from(parent.getContext()).inflate(R.layout.item_sub_discover_post, parent, false));
    }

    @Override
    public int getItemCount() {
        return postDetailsArrayList.size();
    }

    @Override
    public void release() {}

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
            final DiffUtil.DiffResult result = calculateDiff(new PostsDetailsDiffCallback(new ArrayList<>(postDetailsArrayList), postDetailsList));
            postDetailsArrayList.clear();
            postDetailsArrayList.addAll(postDetailsList);
            fragment.getParentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    result.dispatchUpdatesTo(SubDiscoverAdapter.this);
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

    protected class SubDiscoverViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.root_layout) RelativeLayout layout;
        @BindView(R.id.shimmer_layout) RelativeLayout shimmerLayout;
        @BindView(R.id.vignette_layout) FrameLayout vignetteLayout;
        @BindView(R.id.top_layout) RelativeLayout topLayout;
        @BindView(R.id.bottom_layout) RelativeLayout bottomLayout;
        @BindView(R.id.title) ProximaNovaSemiBoldTextView title;
        @BindView(R.id.category) ProximaNovaRegularTextView category;
        @BindView(R.id.name) ProximaNovaSemiBoldTextView name;
        @BindView(R.id.likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.views) ProximaNovaRegularTextView views;
        @BindView(R.id.thumbnail) ImageView postThumbnail;
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        PostDetails postDetails;

        SubDiscoverViewHolder(View itemView) {
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

        @Override
        public void bind() {
            try {
                initializeShimmer(shimmerLayout, topLayout, bottomLayout, vignetteLayout);

                postDetails = postDetailsArrayList.get(getAdapterPosition());

                /*Adjust view size before loading anything*/
                if (dimensionSparseArray.get(getAdapterPosition()) == null) {
                    adjustViewSize(fragment.getContext(), postDetails.getMedias().get(0).getMediaDimension().getWidth(),
                            postDetails.getMedias().get(0).getMediaDimension().getHeight(),
                            layout.getLayoutParams(), getAdapterPosition(), dimensionSparseArray, false);
                } else {
                    layout.getLayoutParams().width = dimensionSparseArray.get(getAdapterPosition()).getWidth();
                    layout.getLayoutParams().height = dimensionSparseArray.get(getAdapterPosition()).getHeight();
                }

                title.setText(decodeUnicodeString(postDetails.getTitle()));

                category.setVisibility(postDetails.getCategories() != null && postDetails.getCategories().size() > 0 ?
                        View.VISIBLE : View.GONE);
                if (postDetails.getCategories() != null && postDetails.getCategories().size() > 0) {
                    category.setText(postDetails.getCategories().get(0).getCategoryName());
                    category.setBackground(getClassicCategoryBackground(category, postDetails.getCategories().get(0).getColor()));
                }

                String nameText = postDetails.getPostOwner().getFirstName() + BLANK_SPACE + postDetails.getPostOwner().getLastName();
                name.setText(nameText);

                String likesText = BLANK_SPACE + String.valueOf(postDetails.getLikes());
                likes.setText(likesText);

                String viewsText = BLANK_SPACE + String.valueOf(postDetails.getMedias().get(0).getViews());
                views.setText(viewsText);

                if (postDetails.getPostOwner().hasProfileMedia() && postDetails.getPostOwner().getProfileMedia() != null) {
                    Glide.with(fragment)
                            .load(postDetails.getPostOwner().getProfileMedia().getThumbUrl())
                            .apply(new RequestOptions().placeholder(R.drawable.ic_user_male_dp_small))
                            .into(dp);
                } else {
                    Glide.with(fragment)
                            .load(R.drawable.ic_user_male_dp_small)
                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                            .into(dp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Glide.with(fragment)
                    .load(postDetails.getMedias().get(0).getThumbUrl())
                    .apply(new RequestOptions().skipMemoryCache(false))
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
                    .into(postThumbnail);
        }

        @Override
        public void bind(List<Object> payloads) {
            super.bind(payloads);
        }
    }
}