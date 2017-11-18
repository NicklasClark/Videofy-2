// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.home.post;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PostReactionAdapter$ViewHolder_ViewBinding implements Unbinder {
  private PostReactionAdapter.ViewHolder target;

  @UiThread
  public PostReactionAdapter$ViewHolder_ViewBinding(PostReactionAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.layout = Utils.findRequiredViewAsType(source, R.id.root_layout, "field 'layout'", RelativeLayout.class);
    target.postThumbnail = Utils.findRequiredViewAsType(source, R.id.reaction_post_thumb, "field 'postThumbnail'", ImageView.class);
    target.caption = Utils.findRequiredViewAsType(source, R.id.reaction_post_caption, "field 'caption'", ProximaNovaSemiboldTextView.class);
    target.profilePic = Utils.findRequiredViewAsType(source, R.id.reaction_post_dp, "field 'profilePic'", CircularAppCompatImageView.class);
    target.name = Utils.findRequiredViewAsType(source, R.id.reaction_post_name, "field 'name'", ProximaNovaSemiboldTextView.class);
    target.likes = Utils.findRequiredViewAsType(source, R.id.reaction_post_likes, "field 'likes'", ProximaNovaRegularTextView.class);
    target.views = Utils.findRequiredViewAsType(source, R.id.reaction_post_views, "field 'views'", ProximaNovaRegularTextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PostReactionAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.layout = null;
    target.postThumbnail = null;
    target.caption = null;
    target.profilePic = null;
    target.name = null;
    target.likes = null;
    target.views = null;
  }
}
