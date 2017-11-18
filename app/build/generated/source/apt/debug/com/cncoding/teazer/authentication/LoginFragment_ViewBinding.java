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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.hbb20.CountryCodePicker;
import java.lang.CharSequence;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginFragment_ViewBinding implements Unbinder {
  private LoginFragment target;

  private View view2131755386;

  private View view2131755385;

  private View view2131755381;

  private TextWatcher view2131755381TextWatcher;

  private View view2131755382;

  private TextWatcher view2131755382TextWatcher;

  private View view2131755384;

  private View view2131755387;

  @UiThread
  @SuppressLint("ClickableViewAccessibility")
  public LoginFragment_ViewBinding(final LoginFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.login_btn, "field 'loginBtn' and method 'onLoginBtnClick'");
    target.loginBtn = Utils.castView(view, R.id.login_btn, "field 'loginBtn'", ProximaNovaSemiboldButton.class);
    view2131755386 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onLoginBtnClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.forgot_password_btn, "field 'forgotPasswordBtn' and method 'onForgotPasswordClick'");
    target.forgotPasswordBtn = Utils.castView(view, R.id.forgot_password_btn, "field 'forgotPasswordBtn'", ProximaNovaSemiboldButton.class);
    view2131755385 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onForgotPasswordClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.login_username, "field 'usernameView', method 'onUsernameFocusChanged', and method 'usernameTextChanged'");
    target.usernameView = Utils.castView(view, R.id.login_username, "field 'usernameView'", ProximaNovaRegularAutoCompleteTextView.class);
    view2131755381 = view;
    view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View p0, boolean p1) {
        target.onUsernameFocusChanged(p1);
      }
    });
    view2131755381TextWatcher = new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence p0, int p1, int p2, int p3) {
        target.usernameTextChanged(p0);
      }

      @Override
      public void beforeTextChanged(CharSequence p0, int p1, int p2, int p3) {
      }

      @Override
      public void afterTextChanged(Editable p0) {
      }
    };
    ((TextView) view).addTextChangedListener(view2131755381TextWatcher);
    view = Utils.findRequiredView(source, R.id.login_password, "field 'passwordView', method 'onLoginByKeyboard', method 'passwordTextChanged', and method 'onPasswordShow'");
    target.passwordView = Utils.castView(view, R.id.login_password, "field 'passwordView'", ProximaNovaRegularAutoCompleteTextView.class);
    view2131755382 = view;
    ((TextView) view).setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView p0, int p1, KeyEvent p2) {
        return target.onLoginByKeyboard(p1);
      }
    });
    view2131755382TextWatcher = new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence p0, int p1, int p2, int p3) {
        target.passwordTextChanged(p0);
      }

      @Override
      public void beforeTextChanged(CharSequence p0, int p1, int p2, int p3) {
      }

      @Override
      public void afterTextChanged(Editable p0) {
      }
    };
    ((TextView) view).addTextChangedListener(view2131755382TextWatcher);
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View p0, MotionEvent p1) {
        return target.onPasswordShow(p1);
      }
    });
    view = Utils.findRequiredView(source, R.id.login_through_otp, "field 'loginThroughOtpBtn' and method 'onLoginThroughOtpClicked'");
    target.loginThroughOtpBtn = Utils.castView(view, R.id.login_through_otp, "field 'loginThroughOtpBtn'", ProximaNovaSemiboldButton.class);
    view2131755384 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onLoginThroughOtpClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.login_through_password, "field 'loginThroughPasswordBtn' and method 'onLoginThroughPasswordClicked'");
    target.loginThroughPasswordBtn = Utils.castView(view, R.id.login_through_password, "field 'loginThroughPasswordBtn'", ProximaNovaSemiboldButton.class);
    view2131755387 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onLoginThroughPasswordClicked();
      }
    });
    target.countryCodePicker = Utils.findRequiredViewAsType(source, R.id.country_code_picker, "field 'countryCodePicker'", CountryCodePicker.class);
    target.loginOptionsLayout = Utils.findRequiredViewAsType(source, R.id.login_options_layout, "field 'loginOptionsLayout'", RelativeLayout.class);
    target.revealLayout = Utils.findRequiredViewAsType(source, R.id.reveal_layout, "field 'revealLayout'", LinearLayout.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progress_bar, "field 'progressBar'", ProgressBar.class);
    target.uploadingNotification = Utils.findRequiredViewAsType(source, R.id.uploading_notification, "field 'uploadingNotification'", ProximaNovaRegularTextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.loginBtn = null;
    target.forgotPasswordBtn = null;
    target.usernameView = null;
    target.passwordView = null;
    target.loginThroughOtpBtn = null;
    target.loginThroughPasswordBtn = null;
    target.countryCodePicker = null;
    target.loginOptionsLayout = null;
    target.revealLayout = null;
    target.progressBar = null;
    target.uploadingNotification = null;

    view2131755386.setOnClickListener(null);
    view2131755386 = null;
    view2131755385.setOnClickListener(null);
    view2131755385 = null;
    view2131755381.setOnFocusChangeListener(null);
    ((TextView) view2131755381).removeTextChangedListener(view2131755381TextWatcher);
    view2131755381TextWatcher = null;
    view2131755381 = null;
    ((TextView) view2131755382).setOnEditorActionListener(null);
    ((TextView) view2131755382).removeTextChangedListener(view2131755382TextWatcher);
    view2131755382TextWatcher = null;
    view2131755382.setOnTouchListener(null);
    view2131755382 = null;
    view2131755384.setOnClickListener(null);
    view2131755384 = null;
    view2131755387.setOnClickListener(null);
    view2131755387 = null;
  }
}
