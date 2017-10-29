package com.cncoding.teazer.home.post;

import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.MediaControllerView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.customViews.ResizableSurfaceView;
import com.cncoding.teazer.home.HomeScreenPostAdapter;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Post.PostList;
import com.cncoding.teazer.utilities.ViewUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostDetailsFragment extends Fragment implements MediaControllerView.MediaPlayerControlListener,
        SurfaceHolder.Callback, MediaPlayer.OnVideoSizeChangedListener {
    private static final String ARG_COLUMN_COUNT = "column-count";
    public static final int EXIT_FRAGMENT = 202;

    @BindView(R.id.video_container) RelativeLayout videoContainer;
    @BindView(R.id.video_surface) ResizableSurfaceView surfaceView;
    @BindView(R.id.video_surface_container) FrameLayout surfaceContainer;
    @BindView(R.id.loading) ProgressBar progressBar;
    @BindView(R.id.react_btn) ProximaNovaSemiboldButton reactBtn;
    @BindView(R.id.menu) CircularAppCompatImageView menu;
    @BindView(R.id.list) RecyclerView recyclerView;

    private int videoWidth;
    private int videoHeight;
    private int columnCount = 1;
    private boolean isComplete;
    private PostList postList;
    private MediaControllerView controller;
    private MediaPlayer mediaPlayer;
    private OnPostDetailsInteractionListener mListener;

    public PostDetailsFragment() {
        // Required empty public constructor
    }

    public static PostDetailsFragment newInstance(int columnCount) {
        PostDetailsFragment fragment = new PostDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postList = new PostList(false, new ArrayList<Pojos.Post.PostDetails>());
        if (getArguments() != null) {
            columnCount = getArguments().getInt(ARG_COLUMN_COUNT);
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
        initPostDetails();
        populateDummyLists();

        return rootView;
    }

    private void prepareMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getActivity().getApplicationContext(),
                    Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.video_1));
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

                controller = new MediaControllerView.Builder(getActivity(), PostDetailsFragment.this)
                        .withVideoTitle(postList.getPosts().get(0).getTitle())
                        .withVideoSurfaceView(surfaceView)
                        .withLocation("New Delhi")
                        .withProfileName("Prem Suman")
                        .withProfilePicUrl("https://timesofindia.indiatimes.com/thumb/msid-59564820,width-400,resizemode-4/59564820.jpg")
                        .withLikes("123")
                        .withViews("408")
                        .withCategories("Dance, Music, Entertainment, EDM, VideoGraphy")
                        .withDuration("00:32")
                        .withReactions("215")
                        .withReaction1Url("https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg")
                        .withReaction2Url("https://i.pinimg.com/736x/84/e1/57/84e15741767278781febecef51b8f6e7--portrait-photography-tips-people-photography.jpg")
                        .withReaction3Url("http://is5.mzstatic.com/image/thumb/Purple71/v4/90/ae/1b/90ae1b97-762d-82fa-1ff9-bcc76435ea88/source/1200x630bb.jpg")
                        .pauseIcon(R.drawable.ic_pause_outline)
                        .playIcon(R.drawable.ic_play_outline)
                        .build(surfaceContainer);
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

    @OnClick(R.id.menu) public void showMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(getContext(), anchor);
        popupMenu.inflate(R.menu.menu_post);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_hide:
                        return true;
                    case R.id.action_delete:
                        return true;
                    case R.id.action_report:
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    @OnClick(R.id.video_surface_container) public void toggleMediaControllerVisibility() {
        controller.toggleControllerView();
    }

    private void initPostDetails() {
        ArrayList<Pojos.Medias> medias1 = new ArrayList<>();
        medias1.add(new Pojos.Medias(0, "",
                "https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg",
                "", "", false, 0, ""));
        ArrayList<Pojos.Category> categories = new ArrayList<>();
        categories.add(new Pojos.Category(1, "Fun"));
        ArrayList<Pojos.Post.PostDetails> postDetails = new ArrayList<>();
        postDetails.add(new Pojos.Post.PostDetails(0, 0, 121, 232, false, "Last night bash at Delhi fort",
                false, false, false, new Pojos.MiniProfile(324, "prem", "Prem", "Suman", true,
                new Pojos.ProfileMedia(
                        "",
                        "https://timesofindia.indiatimes.com/thumb/msid-59564820,width-400,resizemode-4/59564820.jpg",
                        "00:35",
                        "20",
                        false
                )), "", new Pojos.CheckIn(1, 12, 17, "Bangalore"),
                medias1, categories));
        postList = new PostList(false, postDetails);
    }

    private void populateDummyLists() {
        StaggeredGridLayoutManager manager;
        if (columnCount <= 1) {
            manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        } else {
            manager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        }
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        ArrayList<Pojos.Medias> medias1 = new ArrayList<>();
        medias1.add(new Pojos.Medias(0, "",
                "https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg",
                "", "", false, 0, ""));
        ArrayList<Pojos.Medias> medias2 = new ArrayList<>();
        medias2.add(new Pojos.Medias(0, "",
                "https://i.pinimg.com/736x/84/e1/57/84e15741767278781febecef51b8f6e7--portrait-photography-tips-people-photography.jpg",
                "", "", false, 0, ""));
        ArrayList<Pojos.Medias> medias3 = new ArrayList<>();
        medias3.add(new Pojos.Medias(0, "",
                "http://is5.mzstatic.com/image/thumb/Purple71/v4/90/ae/1b/90ae1b97-762d-82fa-1ff9-bcc76435ea88/source/1200x630bb.jpg",
                "", "", false, 0, ""));
        ArrayList<Pojos.Category> categories = new ArrayList<>();
        categories.add(new Pojos.Category(1, "Fun"));
        Pojos.Post.PostDetails postDetails1 = new Pojos.Post.PostDetails(12, 324, 456, 12, false, "Watch this!", false, false, false,
                new Pojos.MiniProfile(324, "prem", "Prem", "Suman", true,
                        new Pojos.ProfileMedia(
                                "",
                                "https://timesofindia.indiatimes.com/thumb/msid-59564820,width-400,resizemode-4/59564820.jpg",
                                "00:35",
                                "20",
                                false
                        )),
                "", new Pojos.CheckIn(1, 12, 17, "Bangalore"),
                medias1,
                categories
        );
        Pojos.Post.PostDetails postDetails2 = new Pojos.Post.PostDetails(12, 324, 456, 12, false, "Weekend at Bahamas", false, false, false,
                new Pojos.MiniProfile(324, "prem", "Prem", "Suman", true,
                        new Pojos.ProfileMedia(
                                "",
                                "https://timesofindia.indiatimes.com/thumb/msid-59564820,width-400,resizemode-4/59564820.jpg",
                                "00:35",
                                "20",
                                false
                        )),
                "", new Pojos.CheckIn(1, 12, 17, "Bangalore"),
                medias2,
                categories
        );
        Pojos.Post.PostDetails postDetails3 = new Pojos.Post.PostDetails(12, 324, 456, 12, false, "See this!", false, false, false,
                new Pojos.MiniProfile(324, "prem", "Prem", "Suman", true,
                        new Pojos.ProfileMedia(
                                "",
                                "https://timesofindia.indiatimes.com/thumb/msid-59564820,width-400,resizemode-4/59564820.jpg",
                                "00:35",
                                "20",
                                false
                        )),
                "", new Pojos.CheckIn(1, 12, 17, "Bangalore"),
                medias3,
                categories
        );
        List<Pojos.Post.PostDetails> postDetailsList = new ArrayList<>();
        for (int i = 0; i < 20; i ++) {
            postDetailsList.add(postDetails1);
            postDetailsList.add(postDetails2);
            postDetailsList.add(postDetails3);
        }

        HomeScreenPostAdapter adapter = new HomeScreenPostAdapter(postDetailsList, null, getContext());

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPostDetailsInteractionListener) {
            mListener = (OnPostDetailsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPostDetailsInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying())
            mediaPlayer.start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (mediaPlayer != null)
            mediaPlayer.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        exit();
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

    private void resetMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
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
        mListener.onPostDetailsInteraction(EXIT_FRAGMENT);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
//        if (mediaPlayer == null)
//            prepareMediaPlayer();
        mediaPlayer.setDisplay(surfaceHolder);
//        mediaPlayer.prepareAsync();
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
