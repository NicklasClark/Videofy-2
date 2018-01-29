package com.cncoding.teazer.home.post;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allattentionhere.autoplayvideos.AAH_CustomViewHolder;
import com.allattentionhere.autoplayvideos.AAH_VideoImage;
import com.allattentionhere.autoplayvideos.AAH_VideosAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaBoldButton;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.utilities.audio.AudioVolumeContentObserver.OnAudioVolumeChangedListener;
import com.cncoding.teazer.utilities.audio.AudioVolumeObserver;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_PROFILE;
import static com.cncoding.teazer.data.model.base.MiniProfile.MALE;
import static com.cncoding.teazer.utilities.CommonUtilities.decodeUnicodeString;
import static com.cncoding.teazer.utilities.ViewUtils.adjustViewSize;
import static com.cncoding.teazer.utilities.ViewUtils.getPixels;
import static com.cncoding.teazer.utilities.ViewUtils.launchReactionCamera;

/**
 * {@link RecyclerView.Adapter} that can display {@link PostDetails} and make a call to the
 * specified {@link OnPostAdapterInteractionListener}.
 */
public class PostsListAdapter extends AAH_VideosAdapter implements OnAudioVolumeChangedListener {

    private SparseIntArray colorArray;
//    private SparseArray<Dimension> dimensionSparseArray;
    private OnPostAdapterInteractionListener listener;
    private ArrayList<PostDetails> posts;
    private Context context;
    private boolean isPostClicked = false;
    private boolean isMuted;
    private AudioVolumeObserver audioVolumeObserver;

    PostsListAdapter(ArrayList<PostDetails> posts, Context context) {
        this.posts = posts;
        this.context = context;
//        dimensionSparseArray = new SparseArray<>();
        colorArray = new SparseIntArray();

        if (context instanceof OnPostAdapterInteractionListener) {
            listener = (OnPostAdapterInteractionListener) context;
        }
        if (audioVolumeObserver == null) {
            audioVolumeObserver = new AudioVolumeObserver(context);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public AAH_CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_screen_post_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AAH_CustomViewHolder holder1, int position) {
        final ViewHolder holder = (ViewHolder) holder1;
        try {
            holder.postDetails = posts.get(position);

            holder.setImageUrl(holder.postDetails.getMedias().get(0).getThumbUrl());
            holder.setVideoUrl(holder.postDetails.getMedias().get(0).getMediaUrl());

            shimmerize(new View[]{holder.title, holder.location, holder.category}, new View[]{holder.username},
                    holder.popularityLayoutShimmer);

                /*Adjust view size before loading anything*/
            adjustViewSize(context, holder.postDetails.getMedias().get(0).getDimension().getWidth(),
                    holder.postDetails.getMedias().get(0).getDimension().getHeight(),
                    holder.content.getLayoutParams(), position, null, true);

            @DrawableRes int placeholder = holder.postDetails.getPostOwner().getGender() == MALE ?
                    R.drawable.ic_user_male_dp_small : R.drawable.ic_user_female_dp;

            Glide.with(context)
                    .load(holder.getImageUrl())
                    .placeholder(R.color.colorDisabled)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                       boolean isFromMemoryCache, boolean isFirstResource) {
                            deShimmerize(new View[]{holder.title, holder.location, holder.username}, holder.popularityLayoutShimmer);
                            holder.getAAH_ImageView().setImageDrawable(resource);

                            setFields(holder);

                            return false;
                        }
                    })
                    .into(holder.getAAH_ImageView());

            Glide.with(context)
                    .load(holder.postDetails.getPostOwner().getProfileMedia() != null ?
                            holder.postDetails.getPostOwner().getProfileMedia().getThumbUrl() : placeholder)
                    .placeholder(placeholder)
                    .into(holder.dp);

            if (holder.postDetails.getReactions() != null && holder.postDetails.getReactions().size() > 0) {
                holder.reactionListView.setVisibility(VISIBLE);
                holder.noReactionPlaceholder.setVisibility(GONE);
                holder.reactionListView.setLayoutManager(
                        new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                holder.reactionListView.setAdapter(new ReactionAdapter(context, holder.postDetails.getReactions()));
            } else {
                holder.reactionListView.setVisibility(GONE);
                holder.noReactionPlaceholder.setVisibility(GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFields(ViewHolder holder) {
//            SETTING TITLE
        String title = holder.postDetails.getTitle();
        holder.title.setText(decodeUnicodeString(title));

//            SETTING LOCATION
        if (holder.postDetails.getCheckIn() != null) {
            String location = holder.postDetails.getCheckIn().getLocation();
            holder.location.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_place_small, 0, 0, 0);
            holder.location.setText(location);
        } else holder.location.setVisibility(GONE);

//            SETTING CATEGORY AND EXTRA CATEGORIES, IF ANY
        if (holder.postDetails.getCategories() != null) {
            holder.category.setVisibility(holder.postDetails.getCategories().isEmpty() ? GONE : VISIBLE);
            if (holder.category.getVisibility() == VISIBLE) {
                holder.category.setText(holder.postDetails.getCategories().get(0).getCategoryName());
                holder.category.setBackground(
                        getBackground(holder.category, holder.getAdapterPosition(),
                                Color.parseColor(holder.postDetails.getCategories().get(0).getColor())));
                int categoriesSize = holder.postDetails.getCategories().size();
                if (categoriesSize > 1) {
                    holder.categoryExtra.setVisibility(VISIBLE);
                    String extraCategoryCount = "+ " + (categoriesSize - 1) + ((categoriesSize - 1 == 1) ? " Category" : " Categories");
                    holder.categoryExtra.setText(extraCategoryCount);
                } else holder.categoryExtra.setVisibility(GONE);
            }
        } else {
            holder.category.setVisibility(GONE);
            holder.categoryExtra.setVisibility(GONE);
        }

//            SETTING USERNAME
        holder.username.setText(holder.postDetails.getPostOwner().getUserName());

//            SETTING LIKES
        holder.likes.setText(String.valueOf(holder.postDetails.getLikes()));

//            SETTING VIEWS
        holder.views.setText(String.valueOf(holder.postDetails.getMedias().get(0).getViews()));

//            SETTING REACTIONS
        holder.reactions.setText(String.valueOf(holder.postDetails.getTotalReactions()));
    }

    private void shimmerize(View[] viewsToShimmerizeLight, View[] viewsToShimmerizeDark, View popularityLayoutShimmer) {
        for (View view : viewsToShimmerizeLight) {
            view.setBackground(context.getResources().getDrawable(R.drawable.bg_shimmer_light));
            if (view instanceof TextView)
                ((TextView) view).setText(null);
        }
        for (View view : viewsToShimmerizeDark) {
            view.setBackground(context.getResources().getDrawable(R.drawable.bg_shimmer_dark));
            if (view instanceof TextView)
                ((TextView) view).setText(null);
        }
        popularityLayoutShimmer.setVisibility(VISIBLE);
    }

    private void deShimmerize(View[] views, View popularityLayoutShimmer) {
        for (View view : views) {
            view.setBackground(null);
        }
        popularityLayoutShimmer.setVisibility(GONE);
    }

//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position, List<Object> payloads) {
//        try {
//            if ((payloads != null && payloads.isEmpty()) || payloads == null) {
//
//            }
//            else {
//                Bundle bundle = (Bundle) payloads.get(0);
//                for (String key : bundle.keySet()) {
//                    switch (key) {
//                        case POST_DETAILS:
//                            posts.remove(position);
//                            posts.add(position, (PostDetails) bundle.getParcelable(POST_DETAILS));
//                            return;
//                        case LIKES:
//                            posts.get(position).likes = bundle.getInt(LIKES);
//                            break;
//                        case TOTAL_REACTIONS:
//                            posts.get(position).total_reactions = bundle.getInt(TOTAL_REACTIONS);
//                            break;
//                        case TOTAL_TAGS:
//                            posts.get(position).total_tags = bundle.getInt(TOTAL_TAGS);
//                            break;
//                        case HAS_CHECKIN:
//                            posts.get(position).has_checkin = bundle.getBoolean(HAS_CHECKIN);
//                            break;
//                        case TITLE:
//                            posts.get(position).title = bundle.getString(TITLE);
//                            break;
//                        case CAN_REACT:
//                            posts.get(position).canReact = bundle.getBoolean(CAN_REACT);
//                            break;
//                        case CAN_LIKE:
//                            posts.get(position).canLike = bundle.getBoolean(CAN_LIKE);
//                            break;
//                        case CHECKIN:
//                            posts.get(position).check_in = bundle.getParcelable(CHECKIN);
//                            break;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private GradientDrawable getBackground(ProximaNovaRegularTextView title, int position, int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (colorArray.get(position) == 0) {
            colorArray.put(position, color);
        }
        gradientDrawable.setColor(Color.TRANSPARENT);
        gradientDrawable.setCornerRadius(getPixels(context, 2));
        gradientDrawable.setStroke(getPixels(context, 0.5f), colorArray.get(position));
        title.setTextColor(colorArray.get(position));
        return gradientDrawable;
    }

//    void clearDimensions() {
//        dimensionSparseArray.clear();
//    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends AAH_CustomViewHolder {

        @BindView(R.id.root_layout) LinearLayout layout;
        @BindView(R.id.title) ProximaNovaSemiBoldTextView title;
        @BindView(R.id.location) ProximaNovaRegularTextView location;
        @BindView(R.id.category) ProximaNovaRegularTextView category;
        @BindView(R.id.category_extra) ProximaNovaRegularTextView categoryExtra;
        @BindView(R.id.content) AAH_VideoImage content;
        @BindView(R.id.volume_control) ImageView volumeControl;
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        @BindView(R.id.username) ProximaNovaSemiBoldTextView username;
        @BindView(R.id.likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.views) ProximaNovaRegularTextView views;
        @BindView(R.id.reactions) ProximaNovaRegularTextView reactions;
        @BindView(R.id.popularity_layout_shimmer) ProximaNovaRegularTextView popularityLayoutShimmer;
        @BindView(R.id.react_btn) ProximaNovaBoldButton reactBtn;
        @BindView(R.id.list) RecyclerView reactionListView;
        @BindView(R.id.no_reaction_placeholder) LinearLayout noReactionPlaceholder;
        PostDetails postDetails;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            volumeControl.setImageResource(isMuted ? R.drawable.ic_volume_off : R.drawable.ic_volume_up);
        }

        @OnClick(R.id.content) void viewPost() {
            if (!isPostClicked) {
                isPostClicked = true;
                fetchPostDetails(postDetails.getPostId(), getAdapterPosition());
            }
        }

        @OnClick(R.id.dp) void viewProfileThroughDp() {
            viewProfile();
        }

        @OnClick(R.id.username) void viewProfileThroughUsername() {
            viewProfile();
        }

        private void viewProfile() {
            if (listener != null) {
                listener.onPostInteraction(ACTION_VIEW_PROFILE, postDetails);
            }
        }

        @OnClick(R.id.volume_control) void controlVolume() {
            if (isMuted) unmuteVideo(); else muteVideo();
        }

        @OnClick(R.id.react_btn) public void react() {
            launchReactionCamera(context, postDetails);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + username.getText() + "' : \"" + title.getText() + "\"";
        }

//        /**
//         * override this method to get callback when video starts to play
//         */
//        @Override
//        public void videoStarted() {
//            super.videoStarted();
//        }

        @Override
        public void muteVideo() {
            super.muteVideo();
            isMuted = true;
            volumeControl.setImageResource(R.drawable.ic_volume_off);
        }

        @Override
        public void unmuteVideo() {
            super.unmuteVideo();
            isMuted = false;
            volumeControl.setImageResource(R.drawable.ic_volume_up);
        }
    }

    @Override
    public void onVolumeChanged(int currentVolume, int maxVolume) {
        isMuted = currentVolume == 0;
    }

    void registerAudioObserver() {
        try {
            if (audioVolumeObserver != null) {
                audioVolumeObserver.register(AudioManager.STREAM_MUSIC, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void unregisterAudioObserver() {
        if (audioVolumeObserver != null) {
            audioVolumeObserver.unregister();
        }
    }

    public interface OnPostAdapterInteractionListener {
        void onPostInteraction(int action, PostDetails postDetails);
        void postDetails(PostDetails postDetails, byte[] image, boolean isComingFromHomePage,
                         boolean isDeepLink, String getThumbUrl, String reactId);
    }

    private void fetchPostDetails(int postId, final int adapterPosition) {
        ApiCallingService.Posts.getPostDetails(postId, context)
                .enqueue(new Callback<PostDetails>() {
                    @Override
                    public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                PostsListFragment.positionToUpdate = adapterPosition;
                                PostsListFragment.postDetails = response.body();
//                                 PostDetailsActivity.newInstance(context, response.body(), null, true,
//                                        true, response.body().getMedias().get(0).getThumbUrl(), null);
//                                listener.onPostInteraction(ACTION_VIEW_POST, postDetails, holder.postThumbnail, holder.layout);

                                listener.postDetails(response.body(), null, true,
                                        false, response.body().getMedias().get(0).getThumbUrl(), null);
                            } else {
                                Toast.makeText(context, "Either post is not available or deleted by owner", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            Toast.makeText(context, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<PostDetails> call, Throwable t) {
                        Toast.makeText(context, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}