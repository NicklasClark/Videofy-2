// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.home.notifications;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FollowingNotificationsTabFragment_ViewBinding implements Unbinder {
  private FollowingNotificationsTabFragment target;

  @UiThread
  public FollowingNotificationsTabFragment_ViewBinding(FollowingNotificationsTabFragment target,
      View source) {
    this.target = target;

    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.list, "field 'recyclerView'", RecyclerView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipe_refresh_layout, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FollowingNotificationsTabFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
    target.swipeRefreshLayout = null;
  }
}
