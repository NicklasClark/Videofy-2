package com.cncoding.teazer.ui.base;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.cncoding.teazer.ui.customviews.common.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.ui.home.BaseBottomBarActivity;
import com.cncoding.teazer.utilities.common.NetworkStateReceiver;
import com.cncoding.teazer.utilities.common.NetworkStateReceiver.NetworkStateListener;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static com.cncoding.teazer.utilities.common.AuthUtils.isConnected;

/**
 *
 * Created by Prem $ on 11/02/2017.
 */

public class BaseFragment extends Fragment implements NetworkStateListener {

    public FragmentNavigation navigation;
    protected EndlessRecyclerViewScrollListener scrollListener;
    protected boolean is_next_page = false;
    private BaseRecyclerView.Adapter adapter;
    public boolean isConnected;
    private NetworkStateReceiver networkStateReceiver;
    public Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        isConnected = isConnected(context);
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        context.registerReceiver(networkStateReceiver, new IntentFilter(CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        context.unregisterReceiver(networkStateReceiver);
    }

    protected void bindRecyclerViewAdapter(BaseRecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @NonNull
    public BaseBottomBarActivity getParentActivity() {
        try {
            if (getActivity() != null && getActivity() instanceof BaseBottomBarActivity) {
                return (BaseBottomBarActivity) getActivity();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (BaseBottomBarActivity) getActivity();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation) {
            navigation = (FragmentNavigation) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        navigation = null;
        if (scrollListener != null) {
            scrollListener.resetState();
            scrollListener = null;
        }
        if (adapter != null) adapter.release();
    }

    public interface FragmentNavigation {
        void pushFragment(Fragment fragment);
        void pushFragmentOnto(Fragment fragment);
        void popFragment();
    }
}
