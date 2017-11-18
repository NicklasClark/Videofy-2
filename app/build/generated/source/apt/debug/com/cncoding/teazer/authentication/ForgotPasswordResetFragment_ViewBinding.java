// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.authentication;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ForgotPasswordResetFragment_ViewBinding implements Unbinder {
  private ForgotPasswordResetFragment target;

  private View view2131755376;

  private View view2131755372;

  @UiThread
  public ForgotPasswordResetFragment_ViewBinding(final ForgotPasswordResetFragment target,
      View source) {
    this.target = target;

    View view;
    target.resetOtpView = Utils.findRequiredViewAsType(source, R.id.forgot_pwd_reset_otp, "field 'resetOtpView'", ProximaNovaRegularAutoCompleteTextView.class);
    target.resetNewPasswordView = Utils.findRequiredViewAsType(source, R.id.forgot_pwd_reset_new_password, "field 'resetNewPasswordView'", ProximaNovaRegularAutoCompleteTextView.class);
    view = Utils.findRequiredView(source, R.id.forgot_pwd_reset_confirm_password, "field 'resetConfirmPasswordView' and method 'resetByKeyboard'");
    target.resetConfirmPasswordView = Utils.castView(view, R.id.forgot_pwd_reset_confirm_password, "field 'resetConfirmPasswordView'", ProximaNovaRegularAutoCompleteTextView.class);
    view2131755376 = view;
    ((TextView) view).setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView p0, int p1, KeyEvent p2) {
        return target.resetByKeyboard(p0, p1);
      }
    });
    target.resetPasswordStatusView = Utils.findRequiredViewAsType(source, R.id.password_reset_status_view, "field 'resetPasswordStatusView'", ProximaNovaRegularTextView.class);
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
    ForgotPasswordResetFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.resetOtpView = null;
    target.resetNewPasswordView = null;
    target.resetConfirmPasswordView = null;
    target.resetPasswordStatusView = null;

    ((TextView) view2131755376).setOnEditorActionListener(null);
    view2131755376 = null;
    view2131755372.setOnClickListener(null);
    view2131755372 = null;
  }
}
