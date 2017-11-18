// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.ui.fragment.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FollowingProfileActivity_ViewBinding implements Unbinder {
  private FollowingProfileActivity target;

  @UiThread
  public FollowingProfileActivity_ViewBinding(FollowingProfileActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public FollowingProfileActivity_ViewBinding(FollowingProfileActivity target, View source) {
    this.target = target;

    target._usernameTitle = Utils.findRequiredViewAsType(source, R.id.username_title, "field '_usernameTitle'", TextView.class);
    target._username = Utils.findRequiredViewAsType(source, R.id.username, "field '_username'", TextView.class);
    target._following = Utils.findRequiredViewAsType(source, R.id.following, "field '_following'", TextView.class);
    target._followers = Utils.findRequiredViewAsType(source, R.id.followers, "field '_followers'", TextView.class);
    target._recycler_view = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field '_recycler_view'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FollowingProfileActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target._usernameTitle = null;
    target._username = null;
    target._following = null;
    target._followers = null;
    target._recycler_view = null;
  }
}
