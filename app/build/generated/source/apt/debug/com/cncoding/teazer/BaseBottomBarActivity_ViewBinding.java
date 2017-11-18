// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.SignPainterTextView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BaseBottomBarActivity_ViewBinding implements Unbinder {
  private BaseBottomBarActivity target;

  private View view2131755231;

  private View view2131755226;

  private View view2131755224;

  @UiThread
  public BaseBottomBarActivity_ViewBinding(BaseBottomBarActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public BaseBottomBarActivity_ViewBinding(final BaseBottomBarActivity target, View source) {
    this.target = target;

    View view;
    target.appBar = Utils.findRequiredViewAsType(source, R.id.app_bar, "field 'appBar'", AppBarLayout.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.toolbarTitle = Utils.findRequiredViewAsType(source, R.id.toolbar_title, "field 'toolbarTitle'", SignPainterTextView.class);
    target.contentFrame = Utils.findRequiredViewAsType(source, R.id.main_fragment_container, "field 'contentFrame'", FrameLayout.class);
    target.bottomTabLayout = Utils.findRequiredViewAsType(source, R.id.bottom_tab_layout, "field 'bottomTabLayout'", TabLayout.class);
    view = Utils.findRequiredView(source, R.id.camera_btn, "field 'cameraButton' and method 'startCamera'");
    target.cameraButton = Utils.castView(view, R.id.camera_btn, "field 'cameraButton'", ImageButton.class);
    view2131755231 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.startCamera();
      }
    });
    target.uploadingStatusLayout = Utils.findRequiredViewAsType(source, R.id.uploading_status_layout, "field 'uploadingStatusLayout'", LinearLayout.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progress_bar, "field 'progressBar'", ProgressBar.class);
    view = Utils.findRequiredView(source, R.id.uploading_notification, "field 'uploadingNotificationTextView' and method 'retryUpload'");
    target.uploadingNotificationTextView = Utils.castView(view, R.id.uploading_notification, "field 'uploadingNotificationTextView'", ProximaNovaBoldTextView.class);
    view2131755226 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.retryUpload();
      }
    });
    target.uploadingNotificationDismiss = Utils.findRequiredViewAsType(source, R.id.dismiss, "field 'uploadingNotificationDismiss'", AppCompatImageView.class);
    view = Utils.findRequiredView(source, R.id.logout_btn, "method 'performLogout'");
    view2131755224 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.performLogout();
      }
    });

    Context context = source.getContext();
    Resources res = context.getResources();
    target.TABS = res.getStringArray(R.array.tab_name);
  }

  @Override
  @CallSuper
  public void unbind() {
    BaseBottomBarActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.appBar = null;
    target.toolbar = null;
    target.toolbarTitle = null;
    target.contentFrame = null;
    target.bottomTabLayout = null;
    target.cameraButton = null;
    target.uploadingStatusLayout = null;
    target.progressBar = null;
    target.uploadingNotificationTextView = null;
    target.uploadingNotificationDismiss = null;

    view2131755231.setOnClickListener(null);
    view2131755231 = null;
    view2131755226.setOnClickListener(null);
    view2131755226 = null;
    view2131755224.setOnClickListener(null);
    view2131755224 = null;
  }
}
