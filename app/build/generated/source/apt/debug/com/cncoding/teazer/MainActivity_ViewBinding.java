// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  private View view2131755277;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(final MainActivity target, View source) {
    this.target = target;

    View view;
    target.welcomeVideo = Utils.findRequiredViewAsType(source, R.id.welcome_video, "field 'welcomeVideo'", TextureView.class);
    target.mainFragmentContainer = Utils.findRequiredViewAsType(source, R.id.main_fragment_container, "field 'mainFragmentContainer'", FrameLayout.class);
    view = Utils.findRequiredView(source, R.id.up_btn, "field 'upBtn' and method 'backPressed'");
    target.upBtn = Utils.castView(view, R.id.up_btn, "field 'upBtn'", ImageView.class);
    view2131755277 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.backPressed();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.welcomeVideo = null;
    target.mainFragmentContainer = null;
    target.upBtn = null;

    view2131755277.setOnClickListener(null);
    view2131755277 = null;
  }
}
