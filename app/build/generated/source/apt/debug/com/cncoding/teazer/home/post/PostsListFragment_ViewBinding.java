// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.home.post;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PostsListFragment_ViewBinding implements Unbinder {
  private PostsListFragment target;

  private View view2131755415;

  private View view2131755256;

  @UiThread
  public PostsListFragment_ViewBinding(final PostsListFragment target, View source) {
    this.target = target;

    View view;
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progress_bar, "field 'progressBar'", ProgressBar.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.list, "field 'recyclerView'", RecyclerView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipe_refresh_layout, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
    target.pager = Utils.findRequiredViewAsType(source, R.id.transition_pager, "field 'pager'", ViewPager.class);
    target.background = Utils.findRequiredView(source, R.id.transition_full_background, "field 'background'");
    target.postLoadErrorTextView = Utils.findRequiredViewAsType(source, R.id.post_load_error, "field 'postLoadErrorTextView'", ProximaNovaBoldTextView.class);
    view = Utils.findRequiredView(source, R.id.post_load_error_layout, "field 'postLoadErrorLayout' and method 'reloadPosts'");
    target.postLoadErrorLayout = Utils.castView(view, R.id.post_load_error_layout, "field 'postLoadErrorLayout'", LinearLayout.class);
    view2131755415 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.reloadPosts();
      }
    });
    view = Utils.findRequiredView(source, R.id.fab, "method 'changeLayoutManager'");
    view2131755256 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.changeLayoutManager();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    PostsListFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.progressBar = null;
    target.recyclerView = null;
    target.swipeRefreshLayout = null;
    target.pager = null;
    target.background = null;
    target.postLoadErrorTextView = null;
    target.postLoadErrorLayout = null;

    view2131755415.setOnClickListener(null);
    view2131755415 = null;
    view2131755256.setOnClickListener(null);
    view2131755256 = null;
  }
}
