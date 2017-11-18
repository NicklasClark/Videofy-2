// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.tagsAndCategories;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TagsAndCategoryFragment_ViewBinding implements Unbinder {
  private TagsAndCategoryFragment target;

  private View view2131755436;

  @UiThread
  public TagsAndCategoryFragment_ViewBinding(final TagsAndCategoryFragment target, View source) {
    this.target = target;

    View view;
    target.headerTextView = Utils.findRequiredViewAsType(source, R.id.headerTextView, "field 'headerTextView'", ProximaNovaRegularTextView.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.tags_categories_recycler_view, "field 'recyclerView'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.tags_categories_done, "field 'doneBtn' and method 'getResult'");
    target.doneBtn = Utils.castView(view, R.id.tags_categories_done, "field 'doneBtn'", FloatingActionButton.class);
    view2131755436 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.getResult();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    TagsAndCategoryFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.headerTextView = null;
    target.recyclerView = null;
    target.doneBtn = null;

    view2131755436.setOnClickListener(null);
    view2131755436 = null;
  }
}
