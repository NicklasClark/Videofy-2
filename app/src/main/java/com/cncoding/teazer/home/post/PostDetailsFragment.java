package com.cncoding.teazer.home.post;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.CustomStaggeredGridLayoutManager;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.MediaControllerView;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.Pojos.Post.PostReaction;
import com.cncoding.teazer.utilities.ViewUtils;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

public class PostDetailsFragment extends BaseFragment implements MediaControllerView.MediaPlayerControlListener,
        TextureView.SurfaceTextureListener, MediaPlayer.OnVideoSizeChangedListener {
    private static final String ARG_COLUMN_COUNT = "columnCount";
    private static final String ARG_POST_DETAILS = "postDetails";
    private static final String ARG_THUMBNAIL = "thumbnail";
    public static final int ACTION_DISMISS_PLACEHOLDER = 10;
    public static final int ACTION_OPEN_REACTION_CAMERA = 11;

    @BindView(R.id.video_container) RelativeLayout videoContainer;
    @BindView(R.id.video_surface) TextureView textureView;
    @BindView(R.id.placeholder) ImageView placeholder;
    @BindView(R.id.video_surface_container) FrameLayout surfaceContainer;
    @BindView(R.id.loading) ProgressBar progressBar;
    @BindView(R.id.react_btn) ProximaNovaSemiboldButton reactBtn;
    @BindView(R.id.menu) CircularAppCompatImageView menu;
    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.post_load_error) ProximaNovaBoldTextView postLoadErrorTextView;
    @BindView(R.id.post_load_error_subtitle) ProximaNovaRegularTextView postLoadErrorSubtitle;
    @BindView(R.id.post_load_error_layout) LinearLayout postLoadErrorLayout;

    private PostDetails postDetails;
    private int columnCount = 1;
    private boolean isComplete;
    private byte[] image;
    private ArrayList<PostReaction> postReactions;
    private MediaControllerView controller;
    private MediaPlayer mediaPlayer;
    private OnPostDetailsInteractionListener mListener;
    private PostReactionAdapter postReactionAdapter;

    public PostDetailsFragment() {
        // Required empty public constructor
    }

    public static PostDetailsFragment newInstance(int columnCount, PostDetails postDetails, byte[] image) {
        PostDetailsFragment fragment = new PostDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putParcelable(ARG_POST_DETAILS, postDetails);
        args.putByteArray(ARG_THUMBNAIL, image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postReactions = new ArrayList<>();
        if (getArguments() != null) {
            columnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            postDetails = getArguments().getParcelable(ARG_POST_DETAILS);
            image = getArguments().getByteArray(ARG_THUMBNAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((BaseBottomBarActivity) getActivity()).hideAppBar();
        View rootView = inflater.inflate(R.layout.fragment_post_details, container, false);
        ButterKnife.bind(this, rootView);

        updateTextureViewSize(postDetails.getMedias().get(0).getDimension().getWidth(),
                postDetails.getMedias().get(0).getDimension().getHeight());

        Glide.with(this)
                .load(image)
                .asBitmap()
                .animate(R.anim.fast_fade_in)
                .into(placeholder);

        progressBar.setVisibility(View.VISIBLE);
        prepareController();

        postReactionAdapter = new PostReactionAdapter(postReactions, getContext());
        CustomStaggeredGridLayoutManager manager = new CustomStaggeredGridLayoutManager(columnCount, VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(postReactionAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (page > 1)
                    getPostReactions(postDetails.getPostId(), page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        textureView.setSurfaceTextureListener(this);
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            isComplete = false;
            mediaPlayer.start();
        }

        if (postDetails != null)
            getPostReactions(postDetails.getPostId(), 1);
    }

    private void prepareMediaPlayer(Surface surface) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(postDetails.getMedias().get(0).getMediaUrl());
            mediaPlayer.setSurface(surface);
            mediaPlayer.setOnVideoSizeChangedListener(this);
            mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                dismissProgressBar();
                isComplete = false;
                mediaPlayer.start();
//                mListener.onPostDetailsInteraction(ACTION_DISMISS_PLACEHOLDER);

//                Increment the video view count
                ApiCallingService.Posts.incrementViewCount(postDetails.getMedias().get(0).getMediaId(), getContext());
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                isComplete = true;
            }
        });
        mediaPlayer.setLooping(true);
        mediaPlayer.prepareAsync();
    }

    private void prepareController() {
        String profilePicUrl = "https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg";
        if (postDetails.getPostOwner().getProfileMedia() != null)
            profilePicUrl = postDetails.getPostOwner().getProfileMedia().getThumbUrl();
        String location = "";
        if (postDetails.hasCheckin())
            location = postDetails.getCheckIn().getLocation();

        if (postDetails != null) {
            controller = new MediaControllerView.Builder(getActivity(), PostDetailsFragment.this)
                    .withVideoTitle(postDetails.getTitle())
                    .withVideoSurfaceView(textureView)
                    .withLocation(location)
                    .withProfileName(postDetails.getPostOwner().getFirstName() + " " + postDetails.getPostOwner().getLastName())
                    .withProfilePicUrl(profilePicUrl)
                    .withLikes(String.valueOf(postDetails.getLikes()))
                    .withViews(String.valueOf(postDetails.getMedias().get(0).getViews()))
                    .withCategories(getUserCategories())
                    .withDuration(postDetails.getMedias().get(0).getDuration())
                    .withReactions(String.valueOf(postDetails.getTotalReactions()))
                    .build(surfaceContainer);
        }
    }

    private String getUserCategories() {
        StringBuilder categories = new StringBuilder();
        for (int i = 0; i < postDetails.getCategories().size(); i++) {
            categories.append(postDetails.getCategories().get(i).getCategoryName());
            if (i < postDetails.getCategories().size() - 1)
                categories.append(", ");
        }
        return categories.toString();
    }

    private void getPostReactions(final int postId, final int pageNumber) {
        ApiCallingService.Posts.getReactionsOfPost(postId, pageNumber, getContext())
                .enqueue(new Callback<Pojos.Post.PostReactionsList>() {
                    @Override
                    public void onResponse(Call<Pojos.Post.PostReactionsList> call, Response<Pojos.Post.PostReactionsList> response) {
                        switch (response.code()) {
                            case 200:
                                if (response.body().getReactions().size() > 0) {
                                    postReactions.addAll(response.body().getReactions());
                                    recyclerView.setVisibility(View.VISIBLE);
                                    postReactionAdapter.notifyDataSetChanged();
                                    if (postReactions.size() > 0) {
                                        if (postReactions.size() >= 1) {
                                            controller.setReaction1Pic(postReactions.get(0).getMediaDetail().getThumbUrl());
                                        }
                                        if (postReactions.size() >= 2) {
                                            controller.setReaction2Pic(postReactions.get(1).getMediaDetail().getThumbUrl());
                                        }
                                        if (postReactions.size() >= 3) {
                                            controller.setReaction3Pic(postReactions.get(2).getMediaDetail().getThumbUrl());
                                        }
                                    }
                                } else showNoReactionMessage();
                                break;
                            default:
                                showErrorMessage("Error " + response.code() +": " + response.message());
                                break;
                        }
                    }

                    private void showErrorMessage(String message) {
                        dismissProgressBar();
                        recyclerView.setVisibility(View.INVISIBLE);
                        postLoadErrorLayout.animate().alpha(1).setDuration(280).start();
                        postLoadErrorLayout.setVisibility(View.VISIBLE);
                        postLoadErrorTextView.setText(getString(R.string.could_not_load_posts) + "\n" + message);
                        postLoadErrorSubtitle.setText(R.string.tap_to_retry);
                    }

                    @Override
                    public void onFailure(Call<Pojos.Post.PostReactionsList> call, Throwable t) {
                        ViewUtils.makeSnackbarWithBottomMargin(getActivity(), recyclerView, t.getMessage());
                    }
                });
    }

    private void showNoReactionMessage() {
        dismissProgressBar();
        recyclerView.setVisibility(View.INVISIBLE);
        postLoadErrorLayout.animate().alpha(1).setDuration(280).start();
        postLoadErrorLayout.setVisibility(View.VISIBLE);
        postLoadErrorTextView.setText(R.string.no_reactions_yet);
        postLoadErrorSubtitle.setText(R.string.be_the_first_one_to_react);
    }

    private void dismissProgressBar() {
        progressBar.animate().scaleX(0).setDuration(280).setInterpolator(new DecelerateInterpolator()).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, 280);
    }

    @OnClick(R.id.react_btn) public void react() {
        mListener.onPostDetailsInteraction(ACTION_OPEN_REACTION_CAMERA, postDetails.getPostId());
    }

    @OnClick(R.id.menu) public void showMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(getContext(), anchor);
//        popupMenu.setOnDismissListener(new OnDismissListener());
        popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener());
        popupMenu.inflate(R.menu.menu_post);
        popupMenu.show();
    }

    @OnClick(R.id.video_surface_container)
    public void toggleMediaControllerVisibility() {
        if (controller != null)
            controller.toggleControllerView();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        Surface surface = new Surface(surfaceTexture);
//        mediaPlayer.setDisplay(surfaceHolder);
        prepareMediaPlayer(surface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
//        textureView.setAspectRatio(width, height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        surfaceTexture.release();
        resetMediaPlayer();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

//    private class OnDismissListener implements PopupMenu.OnDismissListener {
//        @Override
//        public void onDismiss(PopupMenu menu) {
//            Toast.makeText(getContext(), "Popup Menu is dismissed", Toast.LENGTH_SHORT).show();
//        }
//
//    }

    private void updateTextureViewSize(int viewWidth, int viewHeight) {
        int systemWidth = getActivity().getWindow().getDecorView().getWidth();
        viewHeight = systemWidth * viewHeight / viewWidth;
        viewWidth = systemWidth;
//        float scaleX = 1.0f;
//        float scaleY = 1.0f;
//
//        if (videoWidth > viewWidth && videoHeight > viewHeight) {
//            scaleX = videoWidth / viewWidth;
//            scaleY = videoHeight / viewHeight;
//        } else if (videoWidth < viewWidth && videoHeight < viewHeight) {
//            scaleY = viewWidth / videoWidth;
//            scaleX = viewHeight / videoHeight;
//        } else if (viewWidth > videoWidth) {
//            scaleY = (viewWidth / videoWidth) / (viewHeight / videoHeight);
//        } else if (viewHeight > videoHeight) {
//            scaleX = (viewHeight / videoHeight) / (viewWidth / videoWidth);
//        }

        // Calculate pivot points, in our case crop from center
        int pivotPointX = viewWidth / 2;
        int pivotPointY = viewHeight / 2;

        Matrix matrix = new Matrix();
        matrix.setScale(1, 1, pivotPointX, pivotPointY);

        textureView.setTransform(matrix);
        textureView.setLayoutParams(new RelativeLayout.LayoutParams(viewWidth, viewHeight));
        textureView.animate().alpha(1).setDuration(280).start();
        textureView.setVisibility(View.VISIBLE);
    }

    private class OnMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_hide:
                    Toast.makeText(getContext(), "Hide", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_delete:
                    Toast.makeText(getContext(), "Delete", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_report:
                    Toast.makeText(getContext(), "Report", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPostDetailsInteractionListener) {
            mListener = (OnPostDetailsInteractionListener) context;
        }
//        else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnPostDetailsInteractionListener");
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null)
            mediaPlayer.pause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        exit();
        resetMediaPlayer();
        controller.exit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        postReactionAdapter = null;
    }

    private void resetMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
//        videoHeight = mediaPlayer.getVideoHeight();
//        videoWidth = mediaPlayer.getVideoWidth();
//        if (videoHeight > 0 && videoWidth > 0)
//            textureView.setAspectRatio(this.mediaPlayer.getVideoWidth(), this.mediaPlayer.getVideoHeight());
//            textureView.adjustSize(videoContainer.getWidth(), videoContainer.getHeight(),
//                    this.mediaPlayer.getVideoWidth(), this.mediaPlayer.getVideoHeight());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        if (videoWidth > 0 && videoHeight > 0)
//            textureView.setAspectRatio(textureView.getWidth(), textureView.getHeight());
//            textureView.adjustSize(ViewUtils.getDeviceWidth(getContext()), ViewUtils.getDeviceHeight(getContext()),
//                    textureView.getWidth(), textureView.getHeight());
    }

    @Override
    public void start() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            isComplete = false;
            mediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer != null)
            mediaPlayer.pause();
    }

    @Override
    public String getDuration() {
//        if (mediaPlayer != null)
//            return convert(mediaPlayer.getDuration());
//        else
            return postDetails.getMedias().get(0).getDuration();
    }

//    private String convert(final int duration) {
//        int dur, min, sec, mil;
//
//        dur = duration;
//        min = dur / 60000;
//        dur -= min * 60000;
//        sec = dur / 1000;
//        dur -= sec * 1000;
//        mil = dur;
//
//        return min + ":" + sec + "." + mil;
//    }

    @Override
    public int getCurrentPosition() {
        if (mediaPlayer != null)
            return mediaPlayer.getCurrentPosition();
        return 0;
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public void exit() {
        resetMediaPlayer();
        controller.exit();
    }

    public interface OnPostDetailsInteractionListener {
        void onPostDetailsInteraction(int action, int postId);
    }
}