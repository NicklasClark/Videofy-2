package com.cncoding.teazer.home.post;

import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.CustomStaggeredGridLayoutManager;
import com.cncoding.teazer.customViews.MediaControllerView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.customViews.ResizableSurfaceView;
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

public class PostDetailsFragment extends BaseFragment implements MediaControllerView.MediaPlayerControlListener,
        SurfaceHolder.Callback, MediaPlayer.OnVideoSizeChangedListener {
    private static final String ARG_COLUMN_COUNT = "columnCount";
    private static final String ARG_POST_DETAILS = "postDetails";

    @BindView(R.id.video_container) RelativeLayout videoContainer;
    @BindView(R.id.video_surface) ResizableSurfaceView surfaceView;
    @BindView(R.id.video_surface_container) FrameLayout surfaceContainer;
    @BindView(R.id.loading) ProgressBar progressBar;
    @BindView(R.id.react_btn) ProximaNovaSemiboldButton reactBtn;
    @BindView(R.id.menu) CircularAppCompatImageView menu;
    @BindView(R.id.list) RecyclerView recyclerView;

    private PostDetails postDetails;
    private int videoWidth;
    private int videoHeight;
    private int columnCount = 1;
    private boolean isComplete;
    private ArrayList<PostReaction> postReactions;
    private MediaControllerView controller;
    private MediaPlayer mediaPlayer;
    private OnPostDetailsInteractionListener mListener;

    public PostDetailsFragment() {
        // Required empty public constructor
    }

    public static PostDetailsFragment newInstance(int columnCount, PostDetails postDetails) {
        PostDetailsFragment fragment = new PostDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putParcelable(ARG_POST_DETAILS, postDetails);
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_details, container, false);
        ButterKnife.bind(this, rootView);


        surfaceView.getHolder().addCallback(this);
        progressBar.setVisibility(View.VISIBLE);
        prepareMediaPlayer();
        getPostReactions(postDetails.getPostId(), 1);

        return rootView;
    }

    private void prepareMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getContext(), Uri.parse(
                    postDetails.getMedias().get(0).getMediaUrl().equals("")?
                            "android.resource://" + getActivity().getPackageName() + "/" + R.raw.welcome_video
                            : postDetails.getMedias().get(0).getMediaUrl()));
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnVideoSizeChangedListener(this);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                progressBar.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.zoom_out));
                progressBar.setVisibility(View.INVISIBLE);
                isComplete = false;
                mediaPlayer.start();

                String profilePicUrl = "https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg";
                if (postDetails.getPostOwner().getProfileMedia() != null)
                    profilePicUrl = postDetails.getPostOwner().getProfileMedia().getThumbUrl();
                String location = "";
                if (postDetails.hasCheckin())
                    location = postDetails.getCheckIn().getLocation();

                if (postDetails != null) {
                    controller = new MediaControllerView.Builder(getActivity(), PostDetailsFragment.this)
                            .withVideoTitle(postDetails.getTitle())
                            .withVideoSurfaceView(surfaceView)
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
                else {
                    initDummyPostDetails();
                    controller = new MediaControllerView.Builder(getActivity(), PostDetailsFragment.this)
                            .withVideoTitle(postDetails.getTitle())
                            .withVideoSurfaceView(surfaceView)
                            .withLocation("New Delhi")
                            .withProfileName("Prem Suman")
                            .withProfilePicUrl(postDetails.getPostOwner().getProfileMedia().getThumbUrl())
                            .withLikes("123")
                            .withViews("408")
                            .withCategories("Dance, Music, Entertainment, EDM, VideoGraphy")
                            .withDuration("00:32")
                            .withReactions("215")
                            .withReaction1Url("https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg")
                            .withReaction2Url("https://i.pinimg.com/736x/84/e1/57/84e15741767278781febecef51b8f6e7--portrait-photography-tips-people-photography.jpg")
                            .withReaction3Url("http://is5.mzstatic.com/image/thumb/Purple71/v4/90/ae/1b/90ae1b97-762d-82fa-1ff9-bcc76435ea88/source/1200x630bb.jpg")
                            .build(surfaceContainer);
                }
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
                        if (response.code() == 200) {
//                            Reactions came! now fetching the reactions and checking if next page is true (More reactions are available) or not.
                            postReactions.addAll(response.body().getReactions());
                            if (response.body().isNextPage()) {
//                                More Reactions are available, so incrementing the page number and re-calling this method.
                                getPostReactions(postId, pageNumber + 1);
                            }
                            else {
//                                No more reactions available. So populating the reactions in the recyclerView.
                                if (postReactions.size() > 0) {
                                    populateRecyclerView(postReactions);
                                    if (postReactions.size() >= 3) {
                                        controller.setReaction3Pic(postReactions.get(2).getMediaDetail().getThumbUrl());
                                    }
                                    if (postReactions.size() >= 2) {
                                        controller.setReaction2Pic(postReactions.get(1).getMediaDetail().getThumbUrl());
                                    }
                                    if (postReactions.size() >= 1) {
                                        controller.setReaction1Pic(postReactions.get(0).getMediaDetail().getThumbUrl());
                                    }
                                } else {
                                    populateDummyReactions();
                                }
                            }
                        } else {
                            Toast.makeText(getContext(), response.code() +": " + response.message(), Toast.LENGTH_SHORT).show();
                            populateDummyReactions();
                        }
                    }

                    @Override
                    public void onFailure(Call<Pojos.Post.PostReactionsList> call, Throwable t) {
                        ViewUtils.makeSnackbarWithBottomMargin(getActivity(), recyclerView, t.getMessage());
                    }
                });
    }

    private void populateRecyclerView(ArrayList<Pojos.Post.PostReaction> postReactions) {
        CustomStaggeredGridLayoutManager manager;
        manager = new CustomStaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new PostReactionAdapter(postReactions, getContext()));
    }

    private void initDummyPostDetails() {
        ArrayList<Pojos.Medias> medias1 = new ArrayList<>();
        medias1.add(new Pojos.Medias(0, "",
                "https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg",
                "", new Pojos.Dimension(100, 100),false, 0, ""));
        ArrayList<Pojos.Category> categories = new ArrayList<>();
        categories.add(new Pojos.Category(1, "Fun"));
        postDetails = new PostDetails(0, 0, 121, 232, false, "Last night bash at Delhi fort",
                false, false, false,
                new Pojos.MiniProfile(324, "prem",
                        "Prem", "Suman", false, false, true,
                        new Pojos.ProfileMedia(
                                "",
                                "https://timesofindia.indiatimes.com/thumb/msid-59564820,width-400,resizemode-4/59564820.jpg",
                                "00:35",
                                new Pojos.Dimension(100, 100),
                                false
                        )), "", new Pojos.CheckIn(1, 12, 17, "Bangalore"),
                medias1, categories);
    }

    private void populateDummyReactions() {
        CustomStaggeredGridLayoutManager manager;
        manager = new CustomStaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        Pojos.ReactionMediaDetail mediaDetail1 = new Pojos.ReactionMediaDetail(0, "",
                "https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg",
                "", new Pojos.Dimension(100, 100), false);
        Pojos.ReactionMediaDetail mediaDetail2 = new Pojos.ReactionMediaDetail(0, "",
                "https://i.pinimg.com/736x/84/e1/57/84e15741767278781febecef51b8f6e7--portrait-photography-tips-people-photography.jpg",
                "", new Pojos.Dimension(100, 100), false);
        Pojos.ReactionMediaDetail mediaDetail3 = new Pojos.ReactionMediaDetail(0, "",
                "http://is5.mzstatic.com/image/thumb/Purple71/v4/90/ae/1b/90ae1b97-762d-82fa-1ff9-bcc76435ea88/source/1200x630bb.jpg",
                "", new Pojos.Dimension(100, 100), false);
        ArrayList<Pojos.Post.PostReaction> postReactions = new ArrayList<>();
        Pojos.Post.PostReaction postReaction1 = new Pojos.Post.PostReaction(0, 0, "Dude please!", 0, 0, 0, false,
                false, mediaDetail1,
                new Pojos.MiniProfile(324, "prem", "Prem", "Suman", false, false, true,
                        new Pojos.ProfileMedia(
                                "",
                                "https://timesofindia.indiatimes.com/thumb/msid-59564820,width-400,resizemode-4/59564820.jpg",
                                "00:35",
                                new Pojos.Dimension(100, 100),
                                false
                        )),"");
        Pojos.Post.PostReaction postReaction2 = new Pojos.Post.PostReaction(0, 0, "Wait till you see this", 0, 0, 0, false,
                false, mediaDetail2,
                new Pojos.MiniProfile(324, "prem", "Prem", "Suman", false, false, true,
                        new Pojos.ProfileMedia(
                                "",
                                "https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg",
                                "00:35",
                                new Pojos.Dimension(100, 100),
                                false
                        )),"");
        Pojos.Post.PostReaction postReaction3 = new Pojos.Post.PostReaction(0, 0, "Mine is better", 0, 0, 0, false,
                false, mediaDetail3,
                new Pojos.MiniProfile(324, "prem", "Prem", "Suman", false, false, true,
                        new Pojos.ProfileMedia(
                                "",
                                "https://timesofindia.indiatimes.com/thumb/msid-59564820,width-400,resizemode-4/59564820.jpg",
                                "00:35",
                                new Pojos.Dimension(100, 100),
                                false
                        )),"");
        for (int i = 0; i < 20; i++) {
            postReactions.add(postReaction3);
            postReactions.add(postReaction2);
            postReactions.add(postReaction1);
        }

        PostReactionAdapter adapter = new PostReactionAdapter(postReactions, getContext());

        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.menu) public void showMenu(View anchor)
    {
        PopupMenu popupMenu = new PopupMenu(getContext(), anchor);
        popupMenu.setOnDismissListener(new OnDismissListener());
        popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener());
        popupMenu.inflate(R.menu.menu_post);
        popupMenu.show();
    }

    @OnClick(R.id.video_surface_container)
    public void toggleMediaControllerVisibility() {
        if (controller != null)
            controller.toggleControllerView();
    }

    private class OnDismissListener implements PopupMenu.OnDismissListener {
        @Override
        public void onDismiss(PopupMenu menu) {
            Toast.makeText(getContext(), "Popup Menu is dismissed", Toast.LENGTH_SHORT).show();
        }

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
    public void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            isComplete = false;
            mediaPlayer.start();
        }
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
        videoHeight = mediaPlayer.getVideoHeight();
        videoWidth = mediaPlayer.getVideoWidth();
        if (videoHeight > 0 && videoWidth > 0)
            surfaceView.adjustSize(videoContainer.getWidth(), videoContainer.getHeight(),
                    this.mediaPlayer.getVideoWidth(), this.mediaPlayer.getVideoHeight());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (videoWidth > 0 && videoHeight > 0)
            surfaceView.adjustSize(ViewUtils.getDeviceWidth(getContext()), ViewUtils.getDeviceHeight(getContext()),
                    surfaceView.getWidth(), surfaceView.getHeight());
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
    public int getDuration() {
        if (mediaPlayer != null)
            return mediaPlayer.getDuration();
        return 0;
    }

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

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mediaPlayer.setDisplay(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        resetMediaPlayer();
    }

    public interface OnPostDetailsInteractionListener {
        void onPostDetailsInteraction(int action);
    }
}