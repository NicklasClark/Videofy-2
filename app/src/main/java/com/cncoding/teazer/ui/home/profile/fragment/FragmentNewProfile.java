package com.cncoding.teazer.ui.home.profile.fragment;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.cncoding.teazer.R;
import com.cncoding.teazer.ui.base.BaseFragment;

import butterknife.ButterKnife;

    /**
     * Created by farazhabib on 19/02/18.
     */

    public class FragmentNewProfile extends BaseFragment {
        Context context;
        private CollapsingToolbarLayout collapsingToolbarLayout;
        Toolbar toolbar;




        public static FragmentNewProfile newInstance(int page) {
            return new FragmentNewProfile();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }


        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            context = container.getContext();
            View view = inflater.inflate(R.layout.fragment_new_profile, container, false);
            ButterKnife.bind(this, view);
            collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setTitle("arifrocks");
            dynamicToolbarColor();

            return view;
        }


        @Override
        public void onResume() {
            super.onResume();
            getParentActivity().hideToolbar();

        }

        @Override
        public void onPause() {
            super.onPause();
            getParentActivity().showToolbar();

        }

        @Override
        public void onStop() {
            super.onStop();

        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

        }

        @Override
        public void onAttach(Context context) {

            super.onAttach(context);

        }

        @Override
        public void onDetach() {
            super.onDetach();
            getParentActivity().showToolbar();

        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            //  menu.clear();
            // inflater.inflate(R.menu.menu_new_profile, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            return true;
        }


        private void dynamicToolbarColor() {

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.profiledp);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

                @Override
                public void onGenerated(Palette palette) {
                    collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(R.attr.colorPrimary));
                    collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(R.attr.colorPrimaryDark));
                }
            });
        }
    }

