// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.home.post;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.TextureView;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PostDetailsFragment_ViewBinding implements Unbinder {
  private PostDetailsFragment target;

  private View view2131755403;

  private View view2131755406;

  private View view2131755407;

  private View view2131755271;

  private View view2131755409;

  @UiThread
  public PostDetailsFragment_ViewBinding(final PostDetailsFragment target, View source) {
    this.target = target;

    View view;
    target.videoContainer = Utils.findRequiredViewAsType(source, R.id.video_container, "field 'videoContainer'", RelativeLayout.class);
    target.relativeLayout = Utils.findRequiredViewAsType(source, R.id.relative_layout, "field 'relativeLayout'", RelativeLayout.class);
    target.textureView = Utils.findRequiredViewAsType(source, R.id.video_surface, "field 'textureView'", TextureView.class);
    target.placeholder = Utils.findRequiredViewAsType(source, R.id.placeholder, "field 'placeholder'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.video_surface_container, "field 'surfaceContainer' and method 'toggleMediaControllerVisibility'");
    target.surfaceContainer = Utils.castView(view, R.id.video_surface_container, "field 'surfaceContainer'", FrameLayout.class);
    view2131755403 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.toggleMediaControllerVisibility();
      }
    });
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.loading, "field 'progressBar'", ProgressBar.class);
    view = Utils.findRequiredView(source, R.id.react_btn, "field 'reactBtn' and method 'react'");
    target.reactBtn = Utils.castView(view, R.id.react_btn, "field 'reactBtn'", ProximaNovaSemiboldButton.class);
    view2131755406 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.react();
      }
    });
    view = Utils.findRequiredView(source, R.id.like, "field 'likeBtn' and method 'likePost'");
    target.likeBtn = Utils.castView(view, R.id.like, "field 'likeBtn'", CheckedTextView.class);
    view2131755407 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.likePost();
      }
    });
    target.taggedUserListView = Utils.findRequiredViewAsType(source, R.id.tagged_user_list, "field 'taggedUserListView'", ListView.class);
    target.horizontalScrollView = Utils.findRequiredViewAsType(source, R.id.horizontal_scroll_view, "field 'horizontalScrollView'", HorizontalScrollView.class);
    target.tagsCountBadge = Utils.findRequiredViewAsType(source, R.id.tags_badge, "field 'tagsCountBadge'", ProximaNovaSemiboldTextView.class);
    view = Utils.findRequiredView(source, R.id.menu, "field 'menu' and method 'showMenu'");
    target.menu = Utils.castView(view, R.id.menu, "field 'menu'", CircularAppCompatImageView.class);
    view2131755271 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showMenu(p0);
      }
    });
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.list, "field 'recyclerView'", RecyclerView.class);
    target.postLoadErrorTextView = Utils.findRequiredViewAsType(source, R.id.post_load_error, "field 'postLoadErrorTextView'", ProximaNovaBoldTextView.class);
    target.postLoadErrorSubtitle = Utils.findRequiredViewAsType(source, R.id.post_load_error_subtitle, "field 'postLoadErrorSubtitle'", ProximaNovaRegularTextView.class);
    target.postLoadErrorLayout = Utils.findRequiredViewAsType(source, R.id.post_load_error_layout, "field 'postLoadErrorLayout'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.tags, "method 'getTaggedList'");
    view2131755409 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.getTaggedList();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    PostDetailsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.videoContainer = null;
    target.relativeLayout = null;
    target.textureView = null;
    target.placeholder = null;
    target.surfaceContainer = null;
    target.progressBar = null;
    target.reactBtn = null;
    target.likeBtn = null;
    target.taggedUserListView = null;
    target.horizontalScrollView = null;
    target.tagsCountBadge = null;
    target.menu = null;
    target.recyclerView = null;
    target.postLoadErrorTextView = null;
    target.postLoadErrorSubtitle = null;
    target.postLoadErrorLayout = null;

    view2131755403.setOnClickListener(null);
    view2131755403 = null;
    view2131755406.setOnClickListener(null);
    view2131755406 = null;
    view2131755407.setOnClickListener(null);
    view2131755407 = null;
    view2131755271.setOnClickListener(null);
    view2131755271 = null;
    view2131755409.setOnClickListener(null);
    view2131755409 = null;
  }
}
