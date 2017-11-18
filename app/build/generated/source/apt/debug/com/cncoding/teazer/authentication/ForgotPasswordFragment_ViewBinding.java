// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.authentication;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.hbb20.CountryCodePicker;
import java.lang.CharSequence;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ForgotPasswordFragment_ViewBinding implements Unbinder {
  private ForgotPasswordFragment target;

  private View view2131755370;

  private TextWatcher view2131755370TextWatcher;

  private View view2131755372;

  @UiThread
  public ForgotPasswordFragment_ViewBinding(final ForgotPasswordFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.forgot_pwd_email_mobile, "field 'forgotPasswordEditText' and method 'onTextEntered'");
    target.forgotPasswordEditText = Utils.castView(view, R.id.forgot_pwd_email_mobile, "field 'forgotPasswordEditText'", ProximaNovaRegularAutoCompleteTextView.class);
    view2131755370 = view;
    view2131755370TextWatcher = new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence p0, int p1, int p2, int p3) {
        target.onTextEntered(p0);
      }

      @Override
      public void beforeTextChanged(CharSequence p0, int p1, int p2, int p3) {
      }

      @Override
      public void afterTextChanged(Editable p0) {
      }
    };
    ((TextView) view).addTextChangedListener(view2131755370TextWatcher);
    target.countryCodePicker = Utils.findRequiredViewAsType(source, R.id.country_code_picker, "field 'countryCodePicker'", CountryCodePicker.class);
    view = Utils.findRequiredView(source, R.id.reset_pwd_btn, "method 'resetPassword'");
    view2131755372 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.resetPassword();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ForgotPasswordFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.forgotPasswordEditText = null;
    target.countryCodePicker = null;

    ((TextView) view2131755370).removeTextChangedListener(view2131755370TextWatcher);
    view2131755370TextWatcher = null;
    view2131755370 = null;
    view2131755372.setOnClickListener(null);
    view2131755372 = null;
  }
}
