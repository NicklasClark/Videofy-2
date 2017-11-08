package com.cncoding.teazer.ui.fragment.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;

/**
 * Created by MOHD ARIF on 07-11-2017.
 */

public class FragmentProfileMyCreations extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    VideoView videoView;
    ProgressDialog mdialog;
    VideoView videoview;
    ImageView playvideo;
    ImageView demoimage;
    FragmentManager fragmentManager;
    CircularAppCompatImageView menuitem;

    public static FragmentProfileMyCreations newInstance(int page) {
        FragmentProfileMyCreations fragment = new FragmentProfileMyCreations();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context context=container.getContext();
        View view = inflater.inflate(R.layout.fragment_profile_mycreations, container, false);
        playvideo=view.findViewById(R.id.playvideo);
        demoimage=view.findViewById(R.id.demoimage);
        menuitem=view.findViewById(R.id.menu);


        //Uri uri=Uri.parse();
        playvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fragmentManager= getChildFragmentManager();
               // final Fragment fragment1 = new FragmentProfileVideoPlay();
               // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
               // fragmentTransaction.replace(R.id.flContainer, fragment1).commit();
                Uri video = Uri.parse("android.resource://" + getActivity().getPackageName() + "/"
                        + R.raw.welcome_video);


               // videoview.setVisibility(View.VISIBLE);
                //demoimage.setVisibility(View.INVISIBLE);

//                MediaController mediaController=new MediaController(getActivity());
//                mediaController.setAnchorView(mediaController);
//
//                videoview.setMediaController(mediaController);
//                videoview.setVideoURI(video);
//                videoview.requestFocus();
//                videoview.start();
//                videoview.seekTo(100);
            }
        });


        menuitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.inflate(R.menu.menu_profile);
                popupMenu.show();
            }
        });
        return view;
    }
}
