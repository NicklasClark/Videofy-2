package com.cncoding.teazer.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;

/**
 *
 * Created by Prem $ on 11/02/2017.
 */

public class BaseFragment extends Fragment {

    protected String previousTitle;
    public FragmentNavigation navigation;
    protected EndlessRecyclerViewScrollListener scrollListener;
    protected boolean is_next_page = false;
    private BaseRecyclerViewAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void bindRecyclerViewAdapter(BaseRecyclerViewAdapter adapter) {
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
        return null;
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
        if (scrollListener != null) {
            scrollListener.resetState();
            scrollListener = null;
        }
        if (adapter != null) adapter.release();
    }

    public interface FragmentNavigation {
         void pushFragment(Fragment fragment);
    }
}
