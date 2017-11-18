// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.home.camera.upload;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.VideoView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class VideoPreview_ViewBinding implements Unbinder {
  private VideoPreview target;

  private View view2131755446;

  private View view2131755447;

  @UiThread
  public VideoPreview_ViewBinding(final VideoPreview target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.video_view_preview, "field 'videoViewPreview' and method 'pauseVideo'");
    target.videoViewPreview = Utils.castView(view, R.id.video_view_preview, "field 'videoViewPreview'", VideoView.class);
    view2131755446 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.pauseVideo();
      }
    });
    view = Utils.findRequiredView(source, R.id.play_pause_video_preview, "field 'playPauseBtn' and method 'playVideo'");
    target.playPauseBtn = Utils.castView(view, R.id.play_pause_video_preview, "field 'playPauseBtn'", AppCompatImageView.class);
    view2131755447 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.playVideo();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    VideoPreview target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.videoViewPreview = null;
    target.playPauseBtn = null;

    view2131755446.setOnClickListener(null);
    view2131755446 = null;
    view2131755447.setOnClickListener(null);
    view2131755447 = null;
  }
}
