package com.cncoding.teazer.ui.fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.cncoding.teazer.R;

/**
 * Created by MOHD ARIF on 07-11-2017.
 */

public class FragmentProfileMyCreations extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    VideoView videoView;
    ProgressDialog mdialog;
    VideoView videoview;


    public static FragmentProfileMyCreations newInstance(int page) {
      //  Bundle args = new Bundle();
      //  args.putInt(ARG_PAGE, page);
        FragmentProfileMyCreations fragment = new FragmentProfileMyCreations();
      //  fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_mycreations, container, false);
        TextView textView = view.findViewById(R.id.text1);
        //Uri uri=Uri.parse();
        videoview=view.findViewById(R.id.videoview);
        Uri video = Uri.parse("android.resource://" + getActivity().getPackageName() + "/"
                + R.raw.welcome_video);
        MediaController mediaController=new MediaController(getActivity());
        mediaController.setAnchorView(mediaController);
        videoview.setMediaController(mediaController);
        videoview.setVideoURI(video);
        videoview.requestFocus();
        videoview.start();



        return view;
    }
}
