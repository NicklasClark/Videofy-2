package com.cncoding.teazer.customViews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.utilities.ViewUtils;

import java.lang.ref.WeakReference;
import java.util.Formatter;
import java.util.Locale;

import static com.cncoding.teazer.customViews.ViewAnimator.Listeners;
import static com.cncoding.teazer.customViews.ViewAnimator.putOn;
import static com.cncoding.teazer.home.post.FragmentPostDetails.SPACE;

/**
 *
 * Created by Prem $ on 10/28/2017.
 */

public class MediaControllerView extends FrameLayout implements VideoGestureListener {

    private static final int HANDLER_ANIMATE_OUT = 1;// out animate
    private static final int HANDLER_UPDATE_PROGRESS = 2;//cycle update progress
//    private static final long PROGRESS_SEEK = 500;
    private static final long ANIMATE_TIME = 280;

//    private SeekBar mSeekBar; //seek bar for video
//    private boolean mIsDragging; //is dragging seekBar
    private ProximaNovaRegularTextView endTime, currentTime;
    private View rootView; // root view of this
    private boolean isShowing;//controller view showing
    private StringBuilder formatBuilder;
    private Formatter formatter;
//    private GestureDetector mGestureDetector;//gesture detector

//    private boolean mCanSeekVideo;
//    private boolean mCanControlVolume;
//    private boolean mCanControlBrightness;
//    @DrawableRes private int mExitIcon;
//    @DrawableRes private int mShrinkIcon;
//    @DrawableRes private int mStretchIcon;
    private Activity context;
    private AudioManager audioManager;
    private boolean isAudioEnabled;
    private SettingsContentObserver contentObserver;
    private String videoTitle;
    private String location;
    private String profileName;
    private String profilePicUrl;
    private int likes;
    private int views;
    private String categories;
    private String duration;
    private boolean isPlaying;
//    private String tagsCount;
    private int reactionCount;
    private String reaction1Url;
    private String reaction2Url;
    private String reaction3Url;
    private MediaPlayerControlListener mediaPlayerControlListener;
    private ViewGroup anchorView;
    private MotionEvent event;

    @DrawableRes private int pauseIcon;
    @DrawableRes private int playIcon;

    //top layout
    RelativeLayout topLayout;
    public ProgressBar progressBar;
    ProximaNovaSemiBoldTextView caption;
    ProximaNovaRegularTextView locationView;
    ProximaNovaRegularTextView remainingTime;

    //center layout
    AppCompatImageButton playPauseButton;

    //bottom layout
//    RelativeLayout bottomLayout;
    CircularAppCompatImageView profilePic;
    ProximaNovaSemiBoldTextView profileNameView;
    ProximaNovaRegularTextView likesView;
    ProximaNovaRegularTextView viewsView;
    ProximaNovaSemiBoldTextView categoriesView;
    ProximaNovaSemiBoldTextView reactionCountView;
    CircularAppCompatImageView reaction1Pic;
    CircularAppCompatImageView reaction2Pic;
    CircularAppCompatImageView reaction3Pic;

    private Handler handler = new ControllerViewHandler(this);
//    private CountDownTimer countDownTimer;
    private GestureDetector gestureDetector;
    private int currentVolume;

    public MediaControllerView(Context context) {
        super(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    public MediaControllerView(Builder builder) {
        super(builder.context);
        this.context = builder.context;
        this.mediaPlayerControlListener = builder.mediaPlayerControlListener;
//        ButterKnife.bind(context.getApplicationContext(), setAnchorView(builder.anchorView));

        videoTitle = builder.videoTitle;
        pauseIcon = builder.pauseIcon;
        playIcon = builder.playIcon;
        location = builder.location;
        profileName = builder.profileName;
        profilePicUrl = builder.profilePicUrl;
        likes = builder.likes;
        views = builder.views;
        categories = builder.categories;
        duration = builder.duration;
//        tagsCount = builder.tagsCount;
        reactionCount = builder.reactionCount;
        reaction1Url = builder.reaction1Url;
        reaction2Url = builder.reaction2Url;
        reaction3Url = builder.reaction3Url;

        setAnchorView(builder.anchorView);

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (currentVolume <= 0 && audioManager != null) {
            currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        isAudioEnabled = currentVolume > 0;
        contentObserver = new SettingsContentObserver(context, new Handler());
        context.getContentResolver().registerContentObserver(
                android.provider.Settings.System.CONTENT_URI, true, contentObserver);

        initControllerView();
        isPlaying = true;
        show(true, false, true);

//        this.textureView.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                toggleControllerView();
//                return false;
//            }
//        });
    }

    /**
     * toggle pause or play
     */
    private void togglePlayPauseIcons() {
        if (rootView == null || playPauseButton == null || mediaPlayerControlListener == null) {
            return;
        }

        if (mediaPlayerControlListener.isPlaying()) {
            playPauseButton.setImageResource(android.R.color.transparent);
            playPauseButton.setImageResource(pauseIcon);
            isPlaying = true;

        } else {
            playPauseButton.setImageResource(android.R.color.transparent);
            playPauseButton.setImageResource(playIcon);
            isPlaying = false;
        }
    }

    /**
     * set anchor view
     *
     * @param view view that hold controller view
     */
    private void setAnchorView(ViewGroup view) {
        anchorView = view;
        LayoutParams frameParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
        );
        //remove all before add view
        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);

        gestureDetector = new GestureDetector(context, new ViewGestureListener(this));
//        setGestureListener();
    }

    /**
     * Inflate view from exit xml layout
     *
     * @return the root view of {@link MediaControllerView}
     */
    @SuppressLint("InflateParams")
    private View makeControllerView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            rootView = inflater.inflate(R.layout.media_controller, null);
        }
        return rootView;
    }

    /**
     * Init params for all views inside {@link MediaControllerView}
     */
    private void initControllerView() {
//        if (playPauseButton != null) {
//            playPauseButton.requestFocus();
//            playPauseButton.setOnClickListener(mPauseListener);
//        }
//        topLayout = findViewById(R.id.layout_top);
        progressBar = findViewById(R.id.progress_bar);
        caption = findViewById(R.id.media_controller_caption);
        locationView = findViewById(R.id.media_controller_location);
        remainingTime = findViewById(R.id.media_controller_eta);
        playPauseButton = findViewById(R.id.media_controller_play_pause);
//        bottomLayout = findViewById(R.id.layout_bottom);
        profilePic = findViewById(R.id.media_controller_dp);
        profileNameView = findViewById(R.id.media_controller_name);
        likesView = findViewById(R.id.media_controller_likes);
        viewsView = findViewById(R.id.media_controller_views);
        categoriesView = findViewById(R.id.media_controller_categories);
        categoriesView.setSelected(true);
        reactionCountView = findViewById(R.id.media_controller_reaction_count);
        reaction1Pic = findViewById(R.id.media_controller_reaction_1);
        reaction2Pic = findViewById(R.id.media_controller_reaction_2);
        reaction3Pic = findViewById(R.id.media_controller_reaction_3);

        refreshControllerView();

        playPauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayerControlListener == null) {
                    return;
                }

                if (mediaPlayerControlListener.isPlaying()) {
                    mediaPlayerControlListener.pause();
//                    isPlaying = false;
                } else {
                    mediaPlayerControlListener.start();
//                    isPlaying = true;
                }
                togglePlayPauseIcons();
                if (isPlaying)
                    hide();
            }
        });

        remainingTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVolume();
            }
        });

        //init formatter
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
    }

    private void refreshControllerView() {
        caption.setText(videoTitle);
        String locationText = SPACE + location;
        locationView.setText(locationText);
        profileNameView.setText(profileName);
        String likesText = SPACE + likes;
        likesView.setText(likesText);
        String viewsText = SPACE + views;
        viewsView.setText(viewsText);
        categoriesView.setText(categories);

        if (reactionCount > 4) {
            String reactionText = SPACE + "+" + (reactionCount - 3) + " R";
            reactionCountView.setText(reactionText);
        } else if (reactionCount == 0){
//            setNoReactions();
        }
        else {
            String reactionText = SPACE + reactionCount + " R";
            reactionCountView.setText(reactionText);
        }
        Glide.with(getContext())
                .load(profilePicUrl)
                .apply(new RequestOptions().placeholder(R.drawable.ic_user_male_dp_small))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
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
    }

    private void toggleVolume() {
        if (audioManager != null) {
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//            int percent;
            int volume;

            if (isAudioEnabled) {
                volume = 0;
                ViewUtils.setTextViewDrawableStart(remainingTime, R.drawable.ic_volumeoff);
                isAudioEnabled = false;
            } else {
                if (currentVolume > 0)
                    volume = currentVolume;
                else volume = maxVolume;
//                volume = 100 * maxVolume + currentVolume;
                ViewUtils.setTextViewDrawableStart(remainingTime, R.drawable.ic_volumeon);
                isAudioEnabled = true;
            }

            if (volume > maxVolume) {
                volume = maxVolume;
            }

            if (volume < 0) {
                volume = 0;
            }
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        }
    }

//    /**
//     * setMediaPlayerControlListener update play state
//     *
//     * @param mediaPlayerListener self
//     */
//    public void setMediaPlayerControlListener(MediaPlayerControlListener mediaPlayerListener) {
//        this.mediaPlayerControlListener = mediaPlayerListener;
////        togglePlayPause();
//    }

    /**
     * toggle {@link MediaControllerView} show or not
     * this can be called when {@link View#onTouchEvent(MotionEvent)} happened
     */
    public void toggleControllerView() {
        togglePlayPauseIcons();
        if (!isShowing()) {
            show(false, false, true);
        } else {
            hide();
            //animate out controller view
            Message msg = handler.obtainMessage(HANDLER_ANIMATE_OUT);
            //remove exist one first
            handler.removeMessages(HANDLER_ANIMATE_OUT);
            handler.sendMessageDelayed(msg, 5000);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.event = event;
        return performClick();
    }

    @Override
    public boolean performClick() {
        if(gestureDetector != null) {
            gestureDetector.onTouchEvent(event);
            return true;
        }
        return super.performClick();
    }

    /**
     * show controller view
     * @param autoHide whether to autoHide the media controller view.
     * @param toggle whether to toggle play-pause icons.
     * @param animate whether to aniate while showing.
     */
    public void show(boolean autoHide, boolean toggle, boolean animate) {
        if (anchorView != null) {
            //add controller view to bottom of the AnchorView
            LayoutParams frameParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
            );
            anchorView.removeView(MediaControllerView.this);
            anchorView.addView(MediaControllerView.this, frameParams);

            if (animate)
                putOn(playPauseButton)
                    .waitForSize(new Listeners.Size() {
                        @Override
                        public void onSize(ViewAnimator viewAnimator) {
                            viewAnimator.animate()
//                                    .translationY(-topLayout.getHeight(), 0)
//                                    .duration(ANIMATE_TIME)
//                                    .andAnimate(bottomLayout)
//                                    .translationY(bottomLayout.getHeight(), 0)
//                                    .duration(ANIMATE_TIME)
//                                    .andAnimate(playPauseButton)
                                    .scale(0, 1)
                                    .alpha(0, 1)
                                    .duration(ANIMATE_TIME)
                                    .start(new Listeners.Start() {
                                        @Override
                                        public void onStart() {
                                            isShowing = true;
                                            handler.sendEmptyMessage(HANDLER_UPDATE_PROGRESS);
                                        }
                                    });
                        }
                    });
            if (toggle)
                togglePlayPauseIcons();

            if (autoHide)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isShowing)
                            hide();
                    }
                }, 5000);

            //update progress
            handler.sendEmptyMessage(HANDLER_UPDATE_PROGRESS);
        }
    }

    /**
     * hide controller view with animation
     * With custom animation
     */
    public void hide() {
        if (anchorView == null) {
            return;
        }
        isShowing = false;
        putOn(playPauseButton)
                .animate()
//                .translationY(-topLayout.getHeight())
//                .duration(ANIMATE_TIME)
//                .andAnimate(bottomLayout)
//                .translationY(bottomLayout.getHeight())
//                .duration(ANIMATE_TIME)
//                .andAnimate(playPauseButton)
                .scale(1, 0)
                .alpha(1, 0)
                .duration(ANIMATE_TIME)
                .end(new Listeners.End() {
                    @Override
                    public void onEnd() {
                        if (!isPlaying) {
                            anchorView.removeView(MediaControllerView.this);
                        }
                        handler.removeMessages(HANDLER_UPDATE_PROGRESS);
                    }
                });
    }

    /**
     * set video play time {@link #remainingTime}
     *
     * @return current play position
     */
    private int updateRemainingTime() {
        if (mediaPlayerControlListener == null) {
            return 0;
        }

        int position = mediaPlayerControlListener.getCurrentPosition();
        int duration = convert(mediaPlayerControlListener.getDuration() + ".");

        String remainingTimeText;
        remainingTimeText = SPACE + stringToTime(duration - position);
        if(mediaPlayerControlListener.isComplete()){
            remainingTimeText = SPACE + stringToTime(duration);
        }
        remainingTime.setText(remainingTimeText);
        return position;
    }

    private int convert(String time) {
        int quoteInd = time.indexOf(":");
        int pointInd = time.indexOf(".");

        int min = Integer.valueOf(time.substring(0, quoteInd));
        int sec = Integer.valueOf(time.substring(++quoteInd, pointInd));

        return (((min * 60) + sec) * 1000);
    }

    /**
     * convert string to time
     *
     * @param timeMs time to be formatted
     * @return 00:00:00
     */
    private String stringToTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        formatBuilder.setLength(0);
        if (hours > 0) {
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return formatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public boolean isShowing() {
        return isShowing;
    }

    public static class Builder {
        private Activity context;
        private String videoTitle = "";
        private String location = "";
        private String profileName = "";
        private String profilePicUrl = "";
        private int likes;
        private int views;
        private String categories = "";
        public String duration;
//        private String tagsCount = "";
        private int reactionCount;
        private String reaction1Url = "";
        private String reaction2Url = "";
        private String reaction3Url = "";
        private MediaPlayerControlListener mediaPlayerControlListener;
        private ViewGroup anchorView;
        @DrawableRes private int playIcon;
        @DrawableRes private int pauseIcon;
//        private boolean canSeekVideo = true;
//        private boolean canControlVolume = true;
//        private boolean canControlBrightness = true;
//        @DrawableRes private int exitIcon = R.drawable.video_top_back;
//        @DrawableRes private int shrinkIcon = R.drawable.ic_media_fullscreen_shrink;
//        @DrawableRes private int stretchIcon = R.drawable.ic_media_fullscreen_stretch;

        //Required
        public Builder(@Nullable Activity context, @Nullable MediaPlayerControlListener mediaControlListener){
            this.context = context;
            this.mediaPlayerControlListener = mediaControlListener;
            playIcon = R.drawable.ic_play_big;
            pauseIcon = R.drawable.ic_pause_big;
        }
        public Builder with(@Nullable Activity context) {
            this.context = context;
            return this;
        }

//        public Builder withMediaControlListener(@Nullable MediaPlayerControlListener mediaControlListener) {
//            this.mediaPlayerControlListener = mediaControlListener;
//            return this;
//        }

        //Options
        public Builder withVideoTitle(String videoTitle) {
            this.videoTitle = videoTitle;
            return this;
        }

        public Builder withLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder withProfileName(String profileName) {
            this.profileName = profileName;
            return this;
        }

        public Builder withProfilePicUrl(String profilePicUrl) {
            this.profilePicUrl = profilePicUrl;
            return this;
        }

        public Builder withLikes(int likes) {
            this.likes = likes;
            return this;
        }

        public Builder withViews(int views) {
            this.views = views;
            return this;
        }

        public Builder withCategories(String categories) {
            this.categories = categories;
            return this;
        }

        public Builder withDuration(String duration) {
            this.duration = duration;
            return this;
        }

//        public void WithTagsCount(String tagsCount) {
//            this.tagsCount = tagsCount;
//            return this;
//        }

        public Builder withReactionCount(int reactionCount) {
            this.reactionCount = reactionCount;
            return this;
        }

//        public Builder withReaction1Url(@SuppressWarnings("SameParameterValue") String reaction1Url) {
//            this.reaction1Url = reaction1Url;
//            return this;
//        }
//
//        public Builder withReaction2Url(@SuppressWarnings("SameParameterValue") String reaction2Url) {
//            this.reaction2Url = reaction2Url;
//            return this;
//        }
//
//        public Builder withReaction3Url(@SuppressWarnings("SameParameterValue") String reaction3Url) {
//            this.reaction3Url = reaction3Url;
//            return this;
//        }

        public MediaControllerView build(@Nullable ViewGroup anchorView) {
            this.anchorView = anchorView;
            return new MediaControllerView(this);
        }

//        public Builder exitIcon(@DrawableRes int exitIcon) {
//            this.exitIcon = exitIcon;
//            return this;
//        }

//        public Builder shrinkIcon(@DrawableRes int shrinkIcon) {
//            this.shrinkIcon = shrinkIcon;
//            return this;
//        }

//        public Builder stretchIcon(@DrawableRes int stretchIcon) {
//            this.stretchIcon = stretchIcon;
//            return this;
//        }

//        public Builder canSeekVideo(boolean canSeekVideo) {
//            this.canSeekVideo = canSeekVideo;
//            return this;
//        }

//        public Builder canControlVolume(boolean canControlVolume) {
//            this.canControlVolume = canControlVolume;
//            return this;
//        }

//        public Builder canControlBrightness(boolean canControlBrightness) {
//            this.canControlBrightness = canControlBrightness;
//            return this;
//        }
    }

    /**
     * Handler prevent leak memory.
     */
    private static class ControllerViewHandler extends Handler {
        private final WeakReference<MediaControllerView> mView;

        ControllerViewHandler(MediaControllerView view) {
            mView = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            MediaControllerView view = mView.get();
            if (view == null || view.mediaPlayerControlListener == null) {
                return;
            }

            int pos;
            switch (msg.what) {
                case HANDLER_ANIMATE_OUT:
                    view.hide();
                    break;
                case HANDLER_UPDATE_PROGRESS://cycle update seek bar progress
                    pos = view.updateRemainingTime();
                    if (view.isShowing && view.mediaPlayerControlListener.isPlaying()) {//just in case
                        //cycle update
                        msg = obtainMessage(HANDLER_UPDATE_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    }

    @Override
    public void onSingleTap() {
        toggleControllerView();
    }

    public void exit() {
//        mediaPlayerControlListener.exit();
        context.getContentResolver().unregisterContentObserver(contentObserver);
    }

    /**
     * Interface of Media Controller View Which can be callBack
     * when {@link android.media.MediaPlayer} or some other media
     * players work
     */
    public interface MediaPlayerControlListener {
        /**
         * start play video
         */
        void start();

        /**
         * pause video
         */
        void pause();

        /**
         * get video total time
         *
         * @return total time
         */
        String getDuration();

        /**
         * get video current position
         *
         * @return current position
         */
        int getCurrentPosition();

        /**
         * video is playing state
         *
         * @return is video playing
         */
        boolean isPlaying();

        /**
         * video is complete
         * @return complete or not
         */
        boolean isComplete();

        /**
         * get buffer percent
         *
         * @return percent
         */
        int getBufferPercentage();

        /**
         * exit media player
         */
        void exit();
//        /**
//         * seek video to exactly position
//         *
//         * @param position position
//         */
//        void seekTo(int position);

//        /**
//         * video is full screen
//         * in order to control image src...
//         *
//         * @return fullScreen
//         */
//        boolean isFullScreen();
//
//        /**
//         * toggle fullScreen
//         */
//        void toggleFullScreen();
    }

    public class SettingsContentObserver extends ContentObserver {
        private AudioManager audioManager;

        SettingsContentObserver(Context context, Handler handler) {
            super(handler);
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return false;
        }

        @Override
        public void onChange(boolean selfChange) {
            currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
    }
}
