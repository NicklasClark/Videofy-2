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
 * Created by MOHD ARIF on 07-11-2017.
 */

public class FragmentProfileMyReactions extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    VideoView videoView;
    ProgressDialog mdialog;
    VideoView videoview;


    public static FragmentProfileMyReactions newInstance(int page) {
        Bundle args = new Bundle();
        //  args.putInt(ARG_PAGE, page);
        FragmentProfileMyReactions fragment = new FragmentProfileMyReactions();
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
        View view = inflater.inflate(R.layout.fragment_profile_myreactions, container, false);
        Context context=container.getContext();

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
