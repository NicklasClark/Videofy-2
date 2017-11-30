package com.cncoding.teazer.home.discover.search;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.utilities.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

import static com.cncoding.teazer.utilities.ViewUtils.showKeyboard;

public class DiscoverSearchFragment extends BaseFragment {

    public static final String SEARCH_TERM = "searchTerm";

    @BindView(R.id.discover_search) ProximaNovaRegularAutoCompleteTextView searchBtn;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;

//    private IDataCallback iDataCallback = null;
    private Handler handler;
    private OnFragmentInteractionListener mListener;
    private String searchTerm;
    private SectionsPagerAdapter sectionsPagerAdapter;

    public DiscoverSearchFragment() {
        // Required empty public constructor
    }

//    public void setiDataCallback(IDataCallback iDataCallback) {
//        this.iDataCallback = iDataCallback;
//        iDataCallback.onFragmentCreated(listData);
//    }

    public static DiscoverSearchFragment newInstance() {
        return new DiscoverSearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_discover_search, container, false);
        ButterKnife.bind(this, rootView);

        searchBtn.requestFocus();
        showKeyboard(getParentActivity(), searchBtn);

        sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), searchTerm);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        return rootView;
    }

    @OnTextChanged(R.id.discover_search) public void search(final CharSequence charSequence) {
        if (charSequence.length() > 0) {
            if (searchBtn.getCompoundDrawables()[2] == null)
                    searchBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_padded,
                            0, R.drawable.ic_cancel_dark_small, 0);
        } else {
            if (searchBtn.getCompoundDrawables()[2] != null)
                searchBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_padded, 0, 0, 0);
        }

        if (charSequence.length() > 2) {
            searchTerm = charSequence.toString();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    sectionsPagerAdapter.setTextQueryChanged(searchTerm);
                }
            };
            // only canceling the network calls will not help, you need to remove all callbacks as well
            // otherwise the pending callbacks and messages will again invoke the handler and will send the request
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            } else {
                handler = new Handler();
            }
            handler.postDelayed(runnable, 1000);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
//        else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        ViewUtils.hideKeyboard(getActivity(), searchBtn);
        mListener = null;
        super.onDetach();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private String searchTerm;

        SectionsPagerAdapter(FragmentManager fm, String searchTerm) {
            super(fm);
            this.searchTerm = searchTerm;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return VideosTabFragment.newInstance(searchTerm);
                case 1:
                    return PeopleTabFragment.newInstance(searchTerm);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        void setTextQueryChanged(String newText) {
            searchTerm = newText;
            getItem(0);
            getItem(1);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}