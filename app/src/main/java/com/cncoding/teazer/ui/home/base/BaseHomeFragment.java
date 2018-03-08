package com.cncoding.teazer.ui.home.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.cncoding.teazer.ui.base.BaseRecyclerView;
import com.cncoding.teazer.ui.base.BaseViewModelFragment;
import com.cncoding.teazer.ui.customviews.common.EndlessRecyclerViewScrollListener;

/**
 *
 * Created by Prem $ on 11/02/2017.
 */

public class BaseHomeFragment extends BaseViewModelFragment {

    public FragmentNavigation navigation;
    protected EndlessRecyclerViewScrollListener scrollListener;
    protected boolean is_next_page = false;
    private BaseRecyclerView.Adapter adapter;
    public boolean isConnected;
    public Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation) {
            navigation = (FragmentNavigation) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
