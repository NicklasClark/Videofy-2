package com.cncoding.teazer.ui.authentication.base;


import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.cncoding.teazer.R;
import com.cncoding.teazer.base.TeazerApplication;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.data.viewmodel.AuthViewModel;
import com.cncoding.teazer.ui.base.BaseFragment;
import com.cncoding.teazer.ui.home.profile.activity.ForgotPasswordActivity;
import com.cncoding.teazer.utilities.common.Annotations.ValidationType;

import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.STATUS_FALSE;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseAuthFragment extends BaseFragment {

    protected  boolean isPasswordShown = true;
    protected AuthViewModel viewModel;

    public BaseAuthFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = TeazerApplication.get(getParentActivity()).getAppComponent().authComponentBuilder().build().authViewModel();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getApiResponse().observe(this, new Observer<ResultObject>() {
            @SuppressWarnings("ThrowableNotThrown")
            @Override
            public void onChanged(@Nullable ResultObject resultObject) {
                if (resultObject != null) {
                    if (resultObject.getError() != null) {
                        if (resultObject.getError().getMessage().contains(STATUS_FALSE)) {
                            handleResponse(resultObject);
                        }
                        else handleError(resultObject);
                    }
                    else handleResponse(resultObject);
                }
                else handleError(new ResultObject(new Throwable(context.getString(R.string.something_went_wrong))));
            }
        });
    }

    @NonNull public Context getTheContext() {
        return context;
    }

    @NonNull @Override
    public FragmentActivity getParentActivity() {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            return getActivity();
        }
        else if (getActivity() != null && getActivity() instanceof ForgotPasswordActivity)
            return getActivity();
        else throw new IllegalStateException("Fragment is not attached to MainActivity");
    }

    protected abstract void handleResponse(ResultObject resultObject);

    protected abstract void handleError(ResultObject resultObject);

    protected abstract void notifyNoInternetConnection();

    protected abstract boolean isFieldValidated(@ValidationType int whichType);

    protected abstract boolean isFieldFilled(@ValidationType int whichType);
}