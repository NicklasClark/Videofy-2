// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.home.camera.upload;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.AppCompatImageView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaBoldButton;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class VideoUpload_ViewBinding implements Unbinder {
  private VideoUpload target;

  private View view2131755283;

  private View view2131755296;

  private View view2131755297;

  private View view2131755284;

  private View view2131755285;

  private View view2131755287;

  private View view2131755289;

  private View view2131755277;

  @UiThread
  public VideoUpload_ViewBinding(VideoUpload target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public VideoUpload_ViewBinding(final VideoUpload target, View source) {
    this.target = target;

    View view;
    target.thumbnailViewContainer = Utils.findRequiredViewAsType(source, R.id.video_preview_thumbnail_container, "field 'thumbnailViewContainer'", RelativeLayout.class);
    view = Utils.findRequiredView(source, R.id.video_preview_thumbnail, "field 'thumbnailView' and method 'playVideoPreview'");
    target.thumbnailView = Utils.castView(view, R.id.video_preview_thumbnail, "field 'thumbnailView'", ImageView.class);
    view2131755283 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.playVideoPreview();
      }
    });
    target.fragmentContainer = Utils.findRequiredViewAsType(source, R.id.fragment_container, "field 'fragmentContainer'", FrameLayout.class);
    target.videoDurationTextView = Utils.findRequiredViewAsType(source, R.id.video_duration, "field 'videoDurationTextView'", ProximaNovaRegularTextView.class);
    target.thumbnailProgressBar = Utils.findRequiredViewAsType(source, R.id.progress_bar, "field 'thumbnailProgressBar'", ProgressBar.class);
    view = Utils.findRequiredView(source, R.id.video_upload_cancel_btn, "field 'cancelBtn' and method 'retakeVideo'");
    target.cancelBtn = Utils.castView(view, R.id.video_upload_cancel_btn, "field 'cancelBtn'", Button.class);
    view2131755296 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.retakeVideo();
      }
    });
    view = Utils.findRequiredView(source, R.id.video_upload_check_btn, "field 'uploadBtn' and method 'onUploadBtnClick'");
    target.uploadBtn = Utils.castView(view, R.id.video_upload_check_btn, "field 'uploadBtn'", Button.class);
    view2131755297 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onUploadBtnClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.video_upload_title, "field 'videoTitle' and method 'titleDone'");
    target.videoTitle = Utils.castView(view, R.id.video_upload_title, "field 'videoTitle'", ProximaNovaRegularAutoCompleteTextView.class);
    view2131755284 = view;
    ((TextView) view).setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView p0, int p1, KeyEvent p2) {
        return target.titleDone(p0, p1);
      }
    });
    view = Utils.findRequiredView(source, R.id.video_upload_location, "field 'addLocationBtn' and method 'addLocation'");
    target.addLocationBtn = Utils.castView(view, R.id.video_upload_location, "field 'addLocationBtn'", ProximaNovaBoldButton.class);
    view2131755285 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.addLocation();
      }
    });
    target.addLocationText = Utils.findRequiredViewAsType(source, R.id.video_upload_location_text, "field 'addLocationText'", ProximaNovaRegularTextView.class);
    view = Utils.findRequiredView(source, R.id.video_upload_tag_friends, "field 'tagFriendsBtn' and method 'getMyFollowings'");
    target.tagFriendsBtn = Utils.castView(view, R.id.video_upload_tag_friends, "field 'tagFriendsBtn'", ProximaNovaBoldButton.class);
    view2131755287 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.getMyFollowings();
      }
    });
    target.tagFriendsText = Utils.findRequiredViewAsType(source, R.id.video_upload_tag_friends_text, "field 'tagFriendsText'", ProximaNovaRegularTextView.class);
    view = Utils.findRequiredView(source, R.id.video_upload_categories, "field 'uploadCategoriesBtn' and method 'getCategories'");
    target.uploadCategoriesBtn = Utils.castView(view, R.id.video_upload_categories, "field 'uploadCategoriesBtn'", ProximaNovaBoldButton.class);
    view2131755289 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.getCategories();
      }
    });
    target.uploadCategoriesText = Utils.findRequiredViewAsType(source, R.id.video_upload_categories_text, "field 'uploadCategoriesText'", ProximaNovaRegularTextView.class);
    view = Utils.findRequiredView(source, R.id.up_btn, "field 'upBtn' and method 'goBack'");
    target.upBtn = Utils.castView(view, R.id.up_btn, "field 'upBtn'", AppCompatImageView.class);
    view2131755277 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.goBack();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    VideoUpload target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.thumbnailViewContainer = null;
    target.thumbnailView = null;
    target.fragmentContainer = null;
    target.videoDurationTextView = null;
    target.thumbnailProgressBar = null;
    target.cancelBtn = null;
    target.uploadBtn = null;
    target.videoTitle = null;
    target.addLocationBtn = null;
    target.addLocationText = null;
    target.tagFriendsBtn = null;
    target.tagFriendsText = null;
    target.uploadCategoriesBtn = null;
    target.uploadCategoriesText = null;
    target.upBtn = null;

    view2131755283.setOnClickListener(null);
    view2131755283 = null;
    view2131755296.setOnClickListener(null);
    view2131755296 = null;
    view2131755297.setOnClickListener(null);
    view2131755297 = null;
    ((TextView) view2131755284).setOnEditorActionListener(null);
    view2131755284 = null;
    view2131755285.setOnClickListener(null);
    view2131755285 = null;
    view2131755287.setOnClickListener(null);
    view2131755287 = null;
    view2131755289.setOnClickListener(null);
    view2131755289 = null;
    view2131755277.setOnClickListener(null);
    view2131755277 = null;
  }
}
