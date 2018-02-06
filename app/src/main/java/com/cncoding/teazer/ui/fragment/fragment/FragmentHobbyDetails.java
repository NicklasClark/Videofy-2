package com.cncoding.teazer.ui.fragment.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ReportPostTitleAdapter;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularCheckedTextView;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.blurry.Blurry;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by farazhabib on 12/12/17.
 */

public class FragmentHobbyDetails extends DialogFragment {

    @BindView(R.id.text)
    ProximaNovaRegularCheckedTextView text;
    @BindView(R.id.background_profile)
    ImageView background_profile;
    @BindView(R.id.close)
    ImageView close;
    ReportPostTitleAdapter reportPostTitleAdapter = null;
    private Integer selectedReportId;
    private String details;
    private String imageUrls;
    private static final int RC_REQUEST_STORAGE = 1001;

    public FragmentHobbyDetails() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static FragmentHobbyDetails newInstance(String details, String imageUrls) {
        FragmentHobbyDetails frag = new FragmentHobbyDetails();
        Bundle args = new Bundle();
        args.putString("details", details);
        args.putString("imageUrls", imageUrls);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        details = getArguments().getString("details");
        imageUrls = getArguments().getString("imageUrls");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alert_label_editor, container);
        ButterKnife.bind(this, rootView);
        text.setText(details);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  getActivity().onBackPressed();
                getDialog().dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (imageUrls != null) {
            profileBlur(imageUrls);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @AfterPermissionGranted(RC_REQUEST_STORAGE)
    public void profileBlur(final String pic) {

        String perm = android.Manifest.permission.READ_EXTERNAL_STORAGE;
        if (!EasyPermissions.hasPermissions(getContext(), perm)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage),
                    RC_REQUEST_STORAGE, perm);
        } else {

            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(final Void... params) {
                    Bitmap bitmap = null;
                    try {
                        final URL url = new URL(pic);
                        try {
                            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return bitmap;
                }

                @Override
                protected void onPostExecute(final Bitmap result) {
                    try {

                        Bitmap photobitmap = Bitmap.createScaledBitmap(result,
                                300, 300, false);

                        Blurry.with(getContext()).from(photobitmap).into(background_profile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }.execute();


        }

    }


}
