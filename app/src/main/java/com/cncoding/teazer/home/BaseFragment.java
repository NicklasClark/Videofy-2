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

    public FragmentNavigation fragmentNavigation;
    protected EndlessRecyclerViewScrollListener scrollListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    public void updateToolbar(boolean showHomeAsUp) {
//        ActionBar actionBar = ((BaseBottomBarActivity) getActivity()).getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
//            actionBar.setDisplayShowHomeEnabled(showHomeAsUp);
//            if (showHomeAsUp)
//                actionBar.setHomeAsUpIndicator(R.drawable.ic_previous);
//        }
//    }

    @NonNull
    public BaseBottomBarActivity getParentActivity() {
        if (getActivity() != null && getActivity() instanceof BaseBottomBarActivity) {
            return (BaseBottomBarActivity) getActivity();
        }
        else throw new IllegalStateException("Fragment is not attached to BaseBottomBarActivity");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation) {
            fragmentNavigation = (FragmentNavigation) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (scrollListener != null) {
            scrollListener.resetState();
            scrollListener = null;
        }
    }

    public interface FragmentNavigation {
         void pushFragment(Fragment fragment);
    }
}
