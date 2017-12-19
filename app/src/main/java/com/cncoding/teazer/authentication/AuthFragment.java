package com.cncoding.teazer.authentication;


import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.cncoding.teazer.MainActivity;
import com.cncoding.teazer.ui.fragment.activity.ForgotPasswordActivity;
import com.cncoding.teazer.utilities.NetworkStateReceiver;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthFragment extends Fragment implements NetworkStateReceiver.NetworkStateListener {

    public boolean isConnected;
    public Context context;
    private NetworkStateReceiver networkStateReceiver;
    protected  boolean isPasswodShown=true;

    public AuthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getParentActivity();
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        context.registerReceiver(networkStateReceiver, new IntentFilter(CONNECTIVITY_ACTION));
    }

    @NonNull
    public FragmentActivity getParentActivity() {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            return (MainActivity) getActivity();
        }
        else if (getActivity() != null && getActivity() instanceof ForgotPasswordActivity)
            return (ForgotPasswordActivity)getActivity();
        else throw new IllegalStateException("Fragment is not attached to MainActivity");
    }

    @Override
    public void onNetworkAvailable() {
        isConnected = true;
    }

    @Override
    public void onNetworkUnavailable() {
        isConnected = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        context.unregisterReceiver(networkStateReceiver);
    }
}
