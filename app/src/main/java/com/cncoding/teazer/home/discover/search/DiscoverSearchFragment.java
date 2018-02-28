package com.cncoding.teazer.home.discover.search;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.home.discover.BaseDiscoverFragment;
import com.cncoding.teazer.model.BaseModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

import static com.cncoding.teazer.utilities.FabricAnalyticsUtil.logSearchEvent;
import static com.cncoding.teazer.utilities.ViewUtils.hideKeyboard;
import static com.cncoding.teazer.utilities.ViewUtils.showKeyboard;

public class DiscoverSearchFragment extends BaseDiscoverFragment {

    public static final String SEARCH_TERM = "searchTerm";

    @BindView(R.id.discover_search) ProximaNovaRegularAutoCompleteTextView searchBtn;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;

    private Handler handler;
    private String searchTerm;
    private SectionsPagerAdapter sectionsPagerAdapter;

    public DiscoverSearchFragment() {
        // Required empty public constructor
    }

    public static DiscoverSearchFragment newInstance() {
        return new DiscoverSearchFragment();
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_discover_search, container, false);
        ButterKnife.bind(this, rootView);
        previousTitle = getParentActivity().getToolbarTitle();

        sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), searchTerm);
        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                hideKeyboard(getParentActivity(), searchBtn);
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        return rootView;
    }

    @Override public void onResume() {
        super.onResume();
        getParentActivity().updateToolbarTitle(getString(R.string.discover));
        searchBtn.requestFocus();
        showKeyboard(getParentActivity());
    }

    @OnTextChanged(R.id.discover_search) public void performSearch(final CharSequence charSequence) {
        if (charSequence.length() > 0) {
            if (searchBtn.getCompoundDrawables()[2] == null)
                    searchBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_padded,
                            0, R.drawable.ic_clear_dark, 0);
        } else {
            if (searchBtn.getCompoundDrawables()[2] != null)
                searchBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_padded, 0, 0, 0);
        }

        search(charSequence);
    }

    /**
     * Performs search in {@link VideosTabFragment} and {@link PeopleTabFragment}
     * @param charSequence The search term to search for.
     * */
    private void search(CharSequence charSequence) {
        searchTerm = charSequence.toString();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!searchTerm.isEmpty() && searchTerm != null) {
                    if (searchTerm.length() > 1) {
                        logSearchEvent(searchTerm);
                    }
                }
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
        handler.postDelayed(runnable, 200);

    }

    @OnEditorAction(R.id.discover_search) public boolean searchByKeyboard(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            search(searchBtn.getText());
            hideKeyboard(getParentActivity(), searchBtn);
            return true;
        }
        return false;
    }

    @OnTouch(R.id.discover_search) public boolean clearText(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && searchBtn.getCompoundDrawables()[2] != null &&
                event.getRawX() >= searchBtn.getRight() - searchBtn.getCompoundDrawables()[2].getBounds().width() * 1.5) {
            searchBtn.setText("");
            sectionsPagerAdapter.setTextQueryChanged("");
            return true;
        }
        return false;
    }

    @Override public void onDetach() {
        getParentActivity().updateToolbarTitle(previousTitle);
        hideKeyboard(getActivity(), searchBtn);
        super.onDetach();
    }

    @Override protected void handleResponse(BaseModel resultObject) {}

    @Override protected void handleError(BaseModel baseModel) {}

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

        @Override public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return VideosTabFragment.newInstance(searchTerm);
                case 1:
                    return PeopleTabFragment.newInstance(searchTerm);
                default:
                    return null;
            }
        }

        @Override public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override public int getCount() {
            return 2;
        }

        void setTextQueryChanged(String newText) {
            searchTerm = newText;
            notifyDataSetChanged();
        }
    }
}