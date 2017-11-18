// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.home.camera;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.AutoFitTextureView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CameraFragment_ViewBinding implements Unbinder {
  private CameraFragment target;

  private View view2131755355;

  private View view2131755354;

  private View view2131755361;

  @UiThread
  public CameraFragment_ViewBinding(final CameraFragment target, View source) {
    this.target = target;

    View view;
    target.mTextureView = Utils.findRequiredViewAsType(source, R.id.camera_preview, "field 'mTextureView'", AutoFitTextureView.class);
    view = Utils.findRequiredView(source, R.id.camera_record, "field 'mButtonVideo' and method 'toggleRecording'");
    target.mButtonVideo = Utils.castView(view, R.id.camera_record, "field 'mButtonVideo'", FrameLayout.class);
    view2131755355 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.toggleRecording();
      }
    });
    target.recordBtnInner = Utils.findRequiredViewAsType(source, R.id.camera_record_inner, "field 'recordBtnInner'", AppCompatImageView.class);
    target.recordBtnOuter = Utils.findRequiredViewAsType(source, R.id.camera_record_outer, "field 'recordBtnOuter'", AppCompatImageView.class);
    view = Utils.findRequiredView(source, R.id.camera_files, "field 'cameraFilesView' and method 'showGallery'");
    target.cameraFilesView = Utils.castView(view, R.id.camera_files, "field 'cameraFilesView'", AppCompatImageView.class);
    view2131755354 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showGallery();
      }
    });
    view = Utils.findRequiredView(source, R.id.camera_flip, "field 'cameraFlipView' and method 'flipCamera'");
    target.cameraFlipView = Utils.castView(view, R.id.camera_flip, "field 'cameraFlipView'", AppCompatImageView.class);
    view2131755361 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.flipCamera();
      }
    });
    target.cameraFlashView = Utils.findRequiredViewAsType(source, R.id.camera_flash, "field 'cameraFlashView'", AppCompatImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CameraFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mTextureView = null;
    target.mButtonVideo = null;
    target.recordBtnInner = null;
    target.recordBtnOuter = null;
    target.cameraFilesView = null;
    target.cameraFlipView = null;
    target.cameraFlashView = null;

    view2131755355.setOnClickListener(null);
    view2131755355 = null;
    view2131755354.setOnClickListener(null);
    view2131755354 = null;
    view2131755361.setOnClickListener(null);
    view2131755361 = null;
  }
}
