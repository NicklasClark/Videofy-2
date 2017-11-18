// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.home.camera;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CameraActivity_ViewBinding implements Unbinder {
  private CameraActivity target;

  @UiThread
  public CameraActivity_ViewBinding(CameraActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CameraActivity_ViewBinding(CameraActivity target, View source) {
    this.target = target;

    target.slidingUpPanelLayout = Utils.findRequiredViewAsType(source, R.id.sliding_layout, "field 'slidingUpPanelLayout'", SlidingUpPanelLayout.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.video_gallery_container, "field 'recyclerView'", RecyclerView.class);
    target.slidingPanelArrow = Utils.findRequiredViewAsType(source, R.id.sliding_panel_arrow, "field 'slidingPanelArrow'", AppCompatImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CameraActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.slidingUpPanelLayout = null;
    target.recyclerView = null;
    target.slidingPanelArrow = null;
  }
}
