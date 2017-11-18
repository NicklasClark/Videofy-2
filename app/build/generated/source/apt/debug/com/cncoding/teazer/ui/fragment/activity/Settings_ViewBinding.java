// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.ui.fragment.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Settings_ViewBinding implements Unbinder {
  private Settings target;

  @UiThread
  public Settings_ViewBinding(Settings target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Settings_ViewBinding(Settings target, View source) {
    this.target = target;

    target.text_block = Utils.findRequiredViewAsType(source, R.id.text_block, "field 'text_block'", TextView.class);
    target.simpleSwitch = Utils.findRequiredViewAsType(source, R.id.simpleSwitch, "field 'simpleSwitch'", Switch.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    Settings target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.text_block = null;
    target.simpleSwitch = null;
  }
}
