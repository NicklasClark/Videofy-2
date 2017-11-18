// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.tagsAndCategories;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class InterestsAdapter$ViewHolder_ViewBinding implements Unbinder {
  private InterestsAdapter.ViewHolder target;

  @UiThread
  public InterestsAdapter$ViewHolder_ViewBinding(InterestsAdapter.ViewHolder target, View source) {
    this.target = target;

    target.chip = Utils.findRequiredViewAsType(source, R.id.chip, "field 'chip'", ProximaNovaRegularCheckedTextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    InterestsAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.chip = null;
  }
}
