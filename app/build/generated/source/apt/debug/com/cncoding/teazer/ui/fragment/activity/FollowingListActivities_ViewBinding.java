// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.ui.fragment.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FollowingListActivities_ViewBinding implements Unbinder {
  private FollowingListActivities target;

  @UiThread
  public FollowingListActivities_ViewBinding(FollowingListActivities target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public FollowingListActivities_ViewBinding(FollowingListActivities target, View source) {
    this.target = target;

    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progress_bar, "field 'progressBar'", ProgressBar.class);
    target.layout = Utils.findRequiredViewAsType(source, R.id.layout, "field 'layout'", RelativeLayout.class);
    target.nousertext = Utils.findRequiredViewAsType(source, R.id.nousertext, "field 'nousertext'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FollowingListActivities target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.progressBar = null;
    target.layout = null;
    target.nousertext = null;
  }
}
