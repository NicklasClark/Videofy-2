// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.home.notifications;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.UniversalTextView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NotificationsAdapter$FollowingViewHolder_ViewBinding implements Unbinder {
  private NotificationsAdapter.FollowingViewHolder target;

  @UiThread
  public NotificationsAdapter$FollowingViewHolder_ViewBinding(NotificationsAdapter.FollowingViewHolder target,
      View source) {
    this.target = target;

    target.layout = Utils.findRequiredViewAsType(source, R.id.root_layout, "field 'layout'", LinearLayout.class);
    target.dp = Utils.findRequiredViewAsType(source, R.id.notification_dp, "field 'dp'", CircularAppCompatImageView.class);
    target.content = Utils.findRequiredViewAsType(source, R.id.notification_content, "field 'content'", UniversalTextView.class);
    target.thumbnail = Utils.findRequiredViewAsType(source, R.id.notification_thumb, "field 'thumbnail'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    NotificationsAdapter.FollowingViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.layout = null;
    target.dp = null;
    target.content = null;
    target.thumbnail = null;
  }
}
