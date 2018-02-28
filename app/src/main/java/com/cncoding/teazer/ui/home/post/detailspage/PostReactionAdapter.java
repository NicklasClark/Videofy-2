package com.cncoding.teazer.ui.home.post.detailspage;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.util.SparseArray;
import android.view.LayoutInflater;
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
import com.cncoding.teazer.data.model.base.MiniProfile;
import com.cncoding.teazer.data.model.giphy.Images;
import com.cncoding.teazer.data.model.post.PostReaction;
import com.cncoding.teazer.ui.base.BaseRecyclerView;
import com.cncoding.teazer.ui.customviews.common.CircularAppCompatImageView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentNewOtherProfile;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentNewProfile2;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentReactionPlayer;
import com.cncoding.teazer.utilities.diffutil.PostReactionDiffCallback;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cncoding.teazer.utilities.common.CommonUtilities.MEDIA_TYPE_GIF;
import static com.cncoding.teazer.utilities.common.CommonUtilities.MEDIA_TYPE_GIPHY;
import static com.cncoding.teazer.utilities.common.CommonUtilities.MEDIA_TYPE_VIDEO;
import static com.cncoding.teazer.utilities.common.CommonUtilities.decodeUnicodeString;
import static com.cncoding.teazer.utilities.common.ViewUtils.adjustViewSize;
import static com.cncoding.teazer.utilities.common.ViewUtils.getGenderSpecificDpSmall;
import static com.cncoding.teazer.utilities.diffutil.PostReactionDiffCallback.DIFF_POST_REACTION;
import static com.cncoding.teazer.utilities.diffutil.PostReactionDiffCallback.updatePostReactionAccordingToDiffBundle;

public class PostReactionAdapter extends BaseRecyclerView.Adapter {

    private SparseArray<Dimension> dimensionSparseArray;
    private List<PostReaction> postReactions;
    private PostDetailsFragment fragment;

    PostReactionAdapter(PostDetailsFragment fragment) {
        this.fragment = fragment;
        this.postReactions = new ArrayList<>();
        dimensionSparseArray = new SparseArray<>();
    }

    @Override
    public BaseRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reaction_post, parent, false));
    }

    @Override
    public int getItemCount() {
        return postReactions.size();
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

    public void updateReactions(List<PostReaction> postReactionList) {
        try {
            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(
                    new PostReactionDiffCallback(new ArrayList<>(postReactions), postReactionList));
            postReactions.clear();
            postReactions.addAll(postReactionList);
            fragment.getParentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    result.dispatchUpdatesTo(PostReactionAdapter.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            addPosts(1, postReactionList);
        }
    }

    public void addPosts(int page, List<PostReaction> postReactionList) {
        try {
            if (page == 1) {
                postReactions.clear();
                postReactions.addAll(postReactionList);
                notifyDataChanged();
            } else {
                postReactions.addAll(postReactionList);
                notifyItemRangeInserted((page - 1) * 10, postReactionList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.root_layout) RelativeLayout layout;
        @BindView(R.id.shimmer_layout) RelativeLayout shimmerLayout;
        @BindView(R.id.vignette_layout) FrameLayout vignetteLayout;
        @BindView(R.id.bottom_layout) RelativeLayout bottomLayout;
        @BindView(R.id.reaction_post_thumb) ImageView postThumbnail;
        @BindView(R.id.reaction_post_caption) ProximaNovaSemiBoldTextView caption;
        @BindView(R.id.reaction_post_dp) CircularAppCompatImageView profilePic;
        @BindView(R.id.reaction_post_name) ProximaNovaSemiBoldTextView name;
        @BindView(R.id.reaction_post_likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.reaction_post_views) ProximaNovaRegularTextView views;
        PostReaction postReaction;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override public void bind() {
            shimmerLayout.setVisibility(View.VISIBLE);
            vignetteLayout.setVisibility(View.INVISIBLE);
            caption.setVisibility(View.INVISIBLE);
            bottomLayout.setVisibility(View.INVISIBLE);

            postReaction = postReactions.get(getAdapterPosition());
            MiniProfile postOwner = postReaction.getReactOwner();

            if (dimensionSparseArray.get(getAdapterPosition()) == null) {
                adjustViewSize(fragment.getContext(), postReaction.getMediaDetail().getMediaDimension().getWidth(),
                        postReaction.getMediaDetail().getMediaDimension().getHeight(),
                        layout.getLayoutParams(), getAdapterPosition(), dimensionSparseArray, false);
            } else {
                layout.getLayoutParams().width = dimensionSparseArray.get(getAdapterPosition()).getWidth();
                layout.getLayoutParams().height = dimensionSparseArray.get(getAdapterPosition()).getHeight();
            }

            Glide.with(fragment)
                    .load(postOwner.getProfileMedia() != null ?
                            postOwner.getProfileMedia().getThumbUrl() :
                            getGenderSpecificDpSmall(postReaction.getReactOwner().getGender()))
                    .apply(new RequestOptions()
                            .placeholder(getGenderSpecificDpSmall(postReaction.getReactOwner().getGender()))
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                       DataSource dataSource, boolean isFirstResource) {
                            profilePic.setImageDrawable(resource);
                            return false;
                        }
                    })
                    .into(profilePic);

            String title = postReaction.getReactTitle();
            caption.setText(decodeUnicodeString(title));

            String nameText = postOwner.getFirstName() + " " + postOwner.getLastName();
            name.setText(nameText);

            String likesText = String.valueOf(postReaction.getLikes());
            likes.setText(likesText);

            String viewsText = String.valueOf(postReaction.getViews());
            views.setText(viewsText);

            RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
                                            boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model,
                                               Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    shimmerLayout.setVisibility(View.INVISIBLE);
                    vignetteLayout.setVisibility(View.VISIBLE);
                    caption.setVisibility(View.VISIBLE);
                    bottomLayout.setVisibility(View.VISIBLE);
                    return false;
                }
            };
            switch (postReaction.getMediaDetail().getMediaType()) {
                case MEDIA_TYPE_VIDEO:
                    Glide.with(fragment)
                            .load(postReaction.getMediaDetail().getReactThumbUrl())
                            .listener(requestListener)
                            .into(postThumbnail);
                    break;
                case MEDIA_TYPE_GIF:
                    Glide.with(fragment)
                            .load(postReaction.getMediaDetail().getReactMediaUrl())
                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).centerInside())
                            .listener(requestListener)
                            .into(postThumbnail);
                    break;
                case MEDIA_TYPE_GIPHY:
                    Gson gson = new Gson();
                    Images images = gson.fromJson(postReaction.getMediaDetail().getExternalMeta(), Images.class);
                    Glide.with(fragment)
                            .load(images.getDownsized().getUrl())
                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).centerInside())
                            .listener(requestListener)
                            .into(postThumbnail);
                    break;
            }
        }

        @Override public void bind(List<Object> payloads) {
            if (payloads.isEmpty()) return;

            if (payloads.get(0) instanceof PostReaction) {
                bind();
                return;
            }

            Bundle bundle = (Bundle) payloads.get(0);
            if (bundle.containsKey(DIFF_POST_REACTION)) {
                postReaction = bundle.getParcelable(DIFF_POST_REACTION);
                return;
            }

            updatePostReactionAccordingToDiffBundle(postReaction, bundle);
        }

        @OnClick(R.id.root_layout) void viewReaction() {
            switch (postReaction.getMediaDetail().getMediaType()) {
                case MEDIA_TYPE_GIF:
                    openReaction(postReaction, true);
                    break;
                case MEDIA_TYPE_GIPHY:
                    openReaction(postReaction, true);
                    break;
                default:
                    openReaction(postReaction, false);
                    break;
            }
        }

        @OnClick({R.id.reaction_post_dp, R.id.reaction_post_name}) void viewProfile() {
            fragment.navigation.pushFragment(postReaction.getMySelf() ? FragmentNewProfile2.newInstance() :
                    FragmentNewOtherProfile.newInstance(String.valueOf(postReaction.getPostOwnerId()), "", ""));
        }

        private void openReaction(PostReaction postReaction, boolean isGif) {
            fragment.navigation.pushFragment(FragmentReactionPlayer.newInstance(postReaction, isGif));
        }

        @Override public String toString() {
            return super.toString() + " '" + name.getText() + "' : \"" + caption.getText() + "\"";
        }
    }

//    void playFromDeepLink(PostReaction postReaction) {
//        playOnlineVideoInExoPlayer(fragment, POST_REACTION, postReaction, null);
//    }
}