// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.home.search;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SearchFragment_ViewBinding implements Unbinder {
  private SearchFragment target;

  @UiThread
  public SearchFragment_ViewBinding(SearchFragment target, View source) {
    this.target = target;

    target.searSearchView = Utils.findRequiredViewAsType(source, R.id.user_search, "field 'searSearchView'", ProximaNovaRegularAutoCompleteTextView.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.list, "field 'recyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SearchFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.searSearchView = null;
    target.recyclerView = null;
  }
}
