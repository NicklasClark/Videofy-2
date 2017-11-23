package com.cncoding.teazer.home.search;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.utilities.Pojos.Category;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyInterestsFragment extends Fragment {

    private static final String ARG_CATEGORIES = "categories";
//    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.my_interests_tab_layout) TabLayout tabLayout;

    private ArrayList<Category> categories;
//    private String mParam2;

    private OnMyInterestsFragmentInteractionListener mListener;

    public MyInterestsFragment() {
        // Required empty public constructor
    }

    public static MyInterestsFragment newInstance(ArrayList<Category> categories) {
        MyInterestsFragment fragment = new MyInterestsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_CATEGORIES, categories);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categories = getArguments().getParcelableArrayList(ARG_CATEGORIES);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_interests, container, false);
        ButterKnife.bind(this, rootView);

        populateTabs();

        return rootView;
    }

    private void populateTabs() {
        for (int i = 0; i < categories.size(); i++)
            tabLayout.addTab(tabLayout.newTab().setText(categories.get(i).getCategoryName()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMyInterestsFragmentInteractionListener) {
            mListener = (OnMyInterestsFragmentInteractionListener) context;
        }
//        else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnMyInterestsFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMyInterestsFragmentInteractionListener {
        void onMyInterestsFragmentInteraction(Uri uri);
    }
}
