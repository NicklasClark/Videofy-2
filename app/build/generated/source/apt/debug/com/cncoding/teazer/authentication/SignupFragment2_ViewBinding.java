// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.authentication;

import android.annotation.SuppressLint;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import java.lang.CharSequence;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SignupFragment2_ViewBinding implements Unbinder {
  private SignupFragment2 target;

  private View view2131755431;

  private View view2131755432;

  private TextWatcher view2131755432TextWatcher;

  private View view2131755433;

  private TextWatcher view2131755433TextWatcher;

  private View view2131755434;

  @UiThread
  @SuppressLint("ClickableViewAccessibility")
  public SignupFragment2_ViewBinding(final SignupFragment2 target, View source) {
    this.target = target;

    View view;
    target.headerTextView = Utils.findRequiredViewAsType(source, R.id.signup_page_header, "field 'headerTextView'", ProximaNovaRegularTextView.class);
    view = Utils.findRequiredView(source, R.id.signup_username, "field 'usernameView' and method 'onUsernameFocusChanged'");
    target.usernameView = Utils.castView(view, R.id.signup_username, "field 'usernameView'", ProximaNovaRegularAutoCompleteTextView.class);
    view2131755431 = view;
    view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View p0, boolean p1) {
        target.onUsernameFocusChanged(p1);
      }
    });
    view = Utils.findRequiredView(source, R.id.signup_password, "field 'passwordView', method 'signupPasswordTextChanged', and method 'onSignupPasswordShow'");
    target.passwordView = Utils.castView(view, R.id.signup_password, "field 'passwordView'", ProximaNovaRegularAutoCompleteTextView.class);
    view2131755432 = view;
    view2131755432TextWatcher = new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence p0, int p1, int p2, int p3) {
        target.signupPasswordTextChanged(p0);
      }

      @Override
      public void beforeTextChanged(CharSequence p0, int p1, int p2, int p3) {
      }

      @Override
      public void afterTextChanged(Editable p0) {
      }
    };
    ((TextView) view).addTextChangedListener(view2131755432TextWatcher);
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View p0, MotionEvent p1) {
        return target.onSignupPasswordShow(p1);
      }
    });
    view = Utils.findRequiredView(source, R.id.signup_confirm_password, "field 'confirmPasswordView', method 'onLoginByKeyboard', method 'confirmPasswordTextChanged', and method 'onConfirmPasswordShow'");
    target.confirmPasswordView = Utils.castView(view, R.id.signup_confirm_password, "field 'confirmPasswordView'", ProximaNovaRegularAutoCompleteTextView.class);
    view2131755433 = view;
    ((TextView) view).setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView p0, int p1, KeyEvent p2) {
        return target.onLoginByKeyboard(p0, p1);
      }
    });
    view2131755433TextWatcher = new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence p0, int p1, int p2, int p3) {
        target.confirmPasswordTextChanged(p0);
      }

      @Override
      public void beforeTextChanged(CharSequence p0, int p1, int p2, int p3) {
      }

      @Override
      public void afterTextChanged(Editable p0) {
      }
    };
    ((TextView) view).addTextChangedListener(view2131755433TextWatcher);
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View p0, MotionEvent p1) {
        return target.onConfirmPasswordShow(p1);
      }
    });
    view = Utils.findRequiredView(source, R.id.signup_btn, "field 'signupBtn' and method 'performSignup'");
    target.signupBtn = Utils.castView(view, R.id.signup_btn, "field 'signupBtn'", ProximaNovaSemiboldButton.class);
    view2131755434 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.performSignup();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    SignupFragment2 target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.headerTextView = null;
    target.usernameView = null;
    target.passwordView = null;
    target.confirmPasswordView = null;
    target.signupBtn = null;

    view2131755431.setOnFocusChangeListener(null);
    view2131755431 = null;
    ((TextView) view2131755432).removeTextChangedListener(view2131755432TextWatcher);
    view2131755432TextWatcher = null;
    view2131755432.setOnTouchListener(null);
    view2131755432 = null;
    ((TextView) view2131755433).setOnEditorActionListener(null);
    ((TextView) view2131755433).removeTextChangedListener(view2131755433TextWatcher);
    view2131755433TextWatcher = null;
    view2131755433.setOnTouchListener(null);
    view2131755433 = null;
    view2131755434.setOnClickListener(null);
    view2131755434 = null;
  }
}
