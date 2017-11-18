// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.home.search;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaBoldButton;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SearchFragment$UserListAdapter$ViewHolder_ViewBinding implements Unbinder {
  private SearchFragment.UserListAdapter.ViewHolder target;

  @UiThread
  public SearchFragment$UserListAdapter$ViewHolder_ViewBinding(SearchFragment.UserListAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.rootLayout = Utils.findRequiredViewAsType(source, R.id.root_layout, "field 'rootLayout'", LinearLayout.class);
    target.profileName = Utils.findRequiredViewAsType(source, R.id.profile_name, "field 'profileName'", ProximaNovaRegularTextView.class);
    target.username = Utils.findRequiredViewAsType(source, R.id.username, "field 'username'", ProximaNovaRegularTextView.class);
    target.profilePic = Utils.findRequiredViewAsType(source, R.id.username_dp, "field 'profilePic'", CircularAppCompatImageView.class);
    target.addFriendBtn = Utils.findRequiredViewAsType(source, R.id.add_friend_btn, "field 'addFriendBtn'", ProximaNovaBoldButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SearchFragment.UserListAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rootLayout = null;
    target.profileName = null;
    target.username = null;
    target.profilePic = null;
    target.addFriendBtn = null;
  }
}
