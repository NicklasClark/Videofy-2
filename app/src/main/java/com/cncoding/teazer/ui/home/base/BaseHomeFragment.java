package com.cncoding.teazer.ui.home.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.viewmodel.BaseViewModel;
import com.cncoding.teazer.ui.base.BaseFragment;
import com.cncoding.teazer.ui.base.BaseRecyclerView;
import com.cncoding.teazer.ui.base.FragmentNavigation;
import com.cncoding.teazer.ui.customviews.common.EndlessRecyclerViewScrollListener;

/**
 *
 * Created by Prem $ on 11/02/2017.
 */

public class BaseHomeFragment extends BaseFragment {

    public FragmentNavigation navigation;
    protected EndlessRecyclerViewScrollListener scrollListener;
    protected boolean is_next_page = false;
    private BaseRecyclerView.Adapter adapter;
    public boolean isConnected;
    protected int currentPage;
    protected BaseViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPage = 1;
        viewModel = getParentActivity().getBaseViewModel();
    }

    public BaseViewModel getViewModel() {
        return viewModel;
    }

    protected void bindRecyclerViewAdapter(BaseRecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @NonNull public BaseBottomBarActivity getParentActivity() {
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
}