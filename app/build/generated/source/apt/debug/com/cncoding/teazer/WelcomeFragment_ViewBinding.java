// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.facebook.login.widget.LoginButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WelcomeFragment_ViewBinding implements Unbinder {
  private WelcomeFragment target;

  private View view2131755449;

  private View view2131755454;

  private View view2131755448;

  private View view2131755455;

  private View view2131755450;

  private View view2131755452;

  @UiThread
  public WelcomeFragment_ViewBinding(final WelcomeFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.login_page_btn, "field 'loginBtn' and method 'onLoginBtnClick'");
    target.loginBtn = Utils.castView(view, R.id.login_page_btn, "field 'loginBtn'", ProximaNovaSemiboldButton.class);
    view2131755449 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onLoginBtnClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.signup_with_facebook, "field 'signupWithFbBtn' and method 'onFacebookSignupClick'");
    target.signupWithFbBtn = Utils.castView(view, R.id.signup_with_facebook, "field 'signupWithFbBtn'", ProximaNovaSemiboldButton.class);
    view2131755454 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onFacebookSignupClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.fb_login_btn, "field 'fbLoginButton' and method 'fbSignup'");
    target.fbLoginButton = Utils.castView(view, R.id.fb_login_btn, "field 'fbLoginButton'", LoginButton.class);
    view2131755448 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.fbSignup();
      }
    });
    view = Utils.findRequiredView(source, R.id.signup_with_google, "field 'signupWithGoogleBtn' and method 'onGoogleSignupClick'");
    target.signupWithGoogleBtn = Utils.castView(view, R.id.signup_with_google, "field 'signupWithGoogleBtn'", ProximaNovaSemiboldButton.class);
    view2131755455 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onGoogleSignupClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.signup_page_btn, "field 'signupWithEmailBtn' and method 'onSignupOptionClick'");
    target.signupWithEmailBtn = Utils.castView(view, R.id.signup_page_btn, "field 'signupWithEmailBtn'", ProximaNovaSemiboldButton.class);
    view2131755450 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSignupOptionClick();
      }
    });
    target.marqueeText = Utils.findRequiredViewAsType(source, R.id.marquee_text, "field 'marqueeText'", ProximaNovaBoldTextView.class);
    view = Utils.findRequiredView(source, R.id.teazer_header, "method 'testBtnPressed'");
    view2131755452 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.testBtnPressed();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    WelcomeFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.loginBtn = null;
    target.signupWithFbBtn = null;
    target.fbLoginButton = null;
    target.signupWithGoogleBtn = null;
    target.signupWithEmailBtn = null;
    target.marqueeText = null;

    view2131755449.setOnClickListener(null);
    view2131755449 = null;
    view2131755454.setOnClickListener(null);
    view2131755454 = null;
    view2131755448.setOnClickListener(null);
    view2131755448 = null;
    view2131755455.setOnClickListener(null);
    view2131755455 = null;
    view2131755450.setOnClickListener(null);
    view2131755450 = null;
    view2131755452.setOnClickListener(null);
    view2131755452 = null;
  }
}
