// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.tagsAndCategories;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Interests_ViewBinding implements Unbinder {
  private Interests target;

  private View view2131755380;

  @UiThread
  public Interests_ViewBinding(final Interests target, View source) {
    this.target = target;

    View view;
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.list, "field 'recyclerView'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.save_interests_btn, "field 'saveBtn' and method 'saveInterests'");
    target.saveBtn = Utils.castView(view, R.id.save_interests_btn, "field 'saveBtn'", ProximaNovaSemiboldButton.class);
    view2131755380 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.saveInterests();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    Interests target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
    target.saveBtn = null;

    view2131755380.setOnClickListener(null);
    view2131755380 = null;
  }
}
