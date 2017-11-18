// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.ui.fragment.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BlockUserList_ViewBinding implements Unbinder {
  private BlockUserList target;

  @UiThread
  public BlockUserList_ViewBinding(BlockUserList target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public BlockUserList_ViewBinding(BlockUserList target, View source) {
    this.target = target;

    target.progress_bar = Utils.findRequiredViewAsType(source, R.id.progress_bar, "field 'progress_bar'", ProgressBar.class);
    target.layout = Utils.findRequiredViewAsType(source, R.id.layout, "field 'layout'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    BlockUserList target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.progress_bar = null;
    target.layout = null;
  }
}
