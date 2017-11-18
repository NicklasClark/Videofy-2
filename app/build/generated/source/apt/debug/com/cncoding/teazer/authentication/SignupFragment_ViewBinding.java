// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.authentication;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.hbb20.CountryCodePicker;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SignupFragment_ViewBinding implements Unbinder {
  private SignupFragment target;

  private View view2131755425;

  private View view2131755427;

  private View view2131755428;

  @UiThread
  public SignupFragment_ViewBinding(final SignupFragment target, View source) {
    this.target = target;

    View view;
    target.nameView = Utils.findRequiredViewAsType(source, R.id.signup_name, "field 'nameView'", ProximaNovaRegularAutoCompleteTextView.class);
    view = Utils.findRequiredView(source, R.id.signup_email, "field 'emailView' and method 'onEmailFocusChanged'");
    target.emailView = Utils.castView(view, R.id.signup_email, "field 'emailView'", ProximaNovaRegularAutoCompleteTextView.class);
    view2131755425 = view;
    view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View p0, boolean p1) {
        target.onEmailFocusChanged(p1);
      }
    });
    target.countryCodeView = Utils.findRequiredViewAsType(source, R.id.signup_country_code, "field 'countryCodeView'", CountryCodePicker.class);
    view = Utils.findRequiredView(source, R.id.signup_phone_number, "field 'phoneNumberView' and method 'onPhoneNumberFocusChanged'");
    target.phoneNumberView = Utils.castView(view, R.id.signup_phone_number, "field 'phoneNumberView'", ProximaNovaRegularAutoCompleteTextView.class);
    view2131755427 = view;
    view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View p0, boolean p1) {
        target.onPhoneNumberFocusChanged(p1);
      }
    });
    view = Utils.findRequiredView(source, R.id.signup__proceed_btn, "field 'signupProceedBtn' and method 'signupProceed'");
    target.signupProceedBtn = Utils.castView(view, R.id.signup__proceed_btn, "field 'signupProceedBtn'", AppCompatButton.class);
    view2131755428 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.signupProceed();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    SignupFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.nameView = null;
    target.emailView = null;
    target.countryCodeView = null;
    target.phoneNumberView = null;
    target.signupProceedBtn = null;

    view2131755425.setOnFocusChangeListener(null);
    view2131755425 = null;
    view2131755427.setOnFocusChangeListener(null);
    view2131755427 = null;
    view2131755428.setOnClickListener(null);
    view2131755428 = null;
  }
}
