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
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import java.lang.CharSequence;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ConfirmOtpFragment_ViewBinding implements Unbinder {
  private ConfirmOtpFragment target;

  private View view2131755363;

  private TextWatcher view2131755363TextWatcher;

  private View view2131755364;

  private TextWatcher view2131755364TextWatcher;

  private View view2131755365;

  private TextWatcher view2131755365TextWatcher;

  private View view2131755366;

  private TextWatcher view2131755366TextWatcher;

  private View view2131755368;

  @UiThread
  public ConfirmOtpFragment_ViewBinding(final ConfirmOtpFragment target, View source) {
    this.target = target;

    View view;
    target.otpSentTextView = Utils.findRequiredViewAsType(source, R.id.otp_sent_text_view, "field 'otpSentTextView'", ProximaNovaRegularTextView.class);
    view = Utils.findRequiredView(source, R.id.otp_1, "field 'otp1EditText' and method 'Otp1TextChanged'");
    target.otp1EditText = Utils.castView(view, R.id.otp_1, "field 'otp1EditText'", ProximaNovaRegularAutoCompleteTextView.class);
    view2131755363 = view;
    view2131755363TextWatcher = new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence p0, int p1, int p2, int p3) {
        target.Otp1TextChanged(p0);
      }

      @Override
      public void beforeTextChanged(CharSequence p0, int p1, int p2, int p3) {
      }

      @Override
      public void afterTextChanged(Editable p0) {
      }
    };
    ((TextView) view).addTextChangedListener(view2131755363TextWatcher);
    view = Utils.findRequiredView(source, R.id.otp_2, "field 'otp2EditText' and method 'Otp2TextChanged'");
    target.otp2EditText = Utils.castView(view, R.id.otp_2, "field 'otp2EditText'", ProximaNovaRegularAutoCompleteTextView.class);
    view2131755364 = view;
    view2131755364TextWatcher = new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence p0, int p1, int p2, int p3) {
        target.Otp2TextChanged(p0);
      }

      @Override
      public void beforeTextChanged(CharSequence p0, int p1, int p2, int p3) {
      }

      @Override
      public void afterTextChanged(Editable p0) {
      }
    };
    ((TextView) view).addTextChangedListener(view2131755364TextWatcher);
    view = Utils.findRequiredView(source, R.id.otp_3, "field 'otp3EditText' and method 'Otp3TextChanged'");
    target.otp3EditText = Utils.castView(view, R.id.otp_3, "field 'otp3EditText'", ProximaNovaRegularAutoCompleteTextView.class);
    view2131755365 = view;
    view2131755365TextWatcher = new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence p0, int p1, int p2, int p3) {
        target.Otp3TextChanged(p0);
      }

      @Override
      public void beforeTextChanged(CharSequence p0, int p1, int p2, int p3) {
      }

      @Override
      public void afterTextChanged(Editable p0) {
      }
    };
    ((TextView) view).addTextChangedListener(view2131755365TextWatcher);
    view = Utils.findRequiredView(source, R.id.otp_4, "field 'otp4EditText' and method 'Otp4TextChanged'");
    target.otp4EditText = Utils.castView(view, R.id.otp_4, "field 'otp4EditText'", ProximaNovaRegularAutoCompleteTextView.class);
    view2131755366 = view;
    view2131755366TextWatcher = new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence p0, int p1, int p2, int p3) {
        target.Otp4TextChanged(p0);
      }

      @Override
      public void beforeTextChanged(CharSequence p0, int p1, int p2, int p3) {
      }

      @Override
      public void afterTextChanged(Editable p0) {
      }
    };
    ((TextView) view).addTextChangedListener(view2131755366TextWatcher);
    target.otpVerifiedTextView = Utils.findRequiredViewAsType(source, R.id.otp_verified_view, "field 'otpVerifiedTextView'", ProximaNovaRegularTextView.class);
    view = Utils.findRequiredView(source, R.id.otp_resend_btn, "field 'otpResendBtn' and method 'resendOtp'");
    target.otpResendBtn = Utils.castView(view, R.id.otp_resend_btn, "field 'otpResendBtn'", ProximaNovaSemiboldButton.class);
    view2131755368 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.resendOtp();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ConfirmOtpFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.otpSentTextView = null;
    target.otp1EditText = null;
    target.otp2EditText = null;
    target.otp3EditText = null;
    target.otp4EditText = null;
    target.otpVerifiedTextView = null;
    target.otpResendBtn = null;

    ((TextView) view2131755363).removeTextChangedListener(view2131755363TextWatcher);
    view2131755363TextWatcher = null;
    view2131755363 = null;
    ((TextView) view2131755364).removeTextChangedListener(view2131755364TextWatcher);
    view2131755364TextWatcher = null;
    view2131755364 = null;
    ((TextView) view2131755365).removeTextChangedListener(view2131755365TextWatcher);
    view2131755365TextWatcher = null;
    view2131755365 = null;
    ((TextView) view2131755366).removeTextChangedListener(view2131755366TextWatcher);
    view2131755366TextWatcher = null;
    view2131755366 = null;
    view2131755368.setOnClickListener(null);
    view2131755368 = null;
  }
}
