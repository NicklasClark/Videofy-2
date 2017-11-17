package com.cncoding.teazer.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 *
 * Created by Prem $ on 11/02/2017.
 */

public class BaseFragment extends Fragment {

    public FragmentNavigation fragmentNavigation;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation) {
            fragmentNavigation = (FragmentNavigation) context;
        }
    }

    public interface FragmentNavigation {
         void pushFragment(Fragment fragment);
    }
}
