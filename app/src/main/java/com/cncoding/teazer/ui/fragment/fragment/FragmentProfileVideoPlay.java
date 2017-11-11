package com.cncoding.teazer.ui.fragment.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.cncoding.teazer.R;

/**
 *
 * Created by farazhabib on 07/11/17.
 */

public class FragmentProfileVideoPlay  extends Fragment{
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    VideoView videoView;
    ProgressDialog mdialog;
    VideoView videoview;




//    public static FragmentProfileVideoPlay newInstance(int page) {
//        FragmentProfileMyCreations fragment = new FragmentProfileMyCreations();
//        return fragment;
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context context=container.getContext();
        View view = inflater.inflate(R.layout.frangment_profilevideoplay, container, false);
        videoview=view.findViewById(R.id.playvideo);
        MediaController mediaController=new MediaController(getActivity());
        mediaController.setAnchorView(mediaController);

        Uri video = Uri.parse("android.resource://" + getActivity().getPackageName() + "/"
                + R.raw.welcome_video);

        videoview.setMediaController(mediaController);
        videoview.setVideoURI(video);
        videoview.requestFocus();
        videoview.start();
        videoview.seekTo(100);
        return view;
    }
}
