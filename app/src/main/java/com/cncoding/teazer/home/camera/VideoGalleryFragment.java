package com.cncoding.teazer.home.camera;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.home.camera.dummy.DummyContent.DummyItem;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class VideoGalleryFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    @BindView(R.id.list) RecyclerView recyclerView;

    private ArrayList<Videos> videosList = new ArrayList<>();
    private int mColumnCount = 4;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VideoGalleryFragment() {
    }

    @SuppressWarnings("unused")
    public static VideoGalleryFragment newInstance(int columnCount) {
        VideoGalleryFragment fragment = new VideoGalleryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_gallery, container, false);

        ButterKnife.bind(this, view);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                videosList.clear();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                Uri uri;
                Cursor cursor;
                int columnIndexData;
//                int columnId;
//                int columnFolderName;
                int thumbnailData;
                uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

                String[] projection = {
                        MediaStore.MediaColumns.DATA,
//                        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
//                        MediaStore.Video.Media._ID,
                        MediaStore.Video.Thumbnails.DATA
                };
                final String orderByDateTaken = MediaStore.Video.Media.DATE_TAKEN;

                cursor = getActivity().getContentResolver().query(uri, projection, null, null,
                        orderByDateTaken + " DESC");
                if (cursor != null) {
                    columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//                    columnFolderName = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
//                    columnId = cursor.get ColumnIndexOrThrow(MediaStore.Video.Media._ID);
                    thumbnailData = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

                    while (cursor.moveToNext()) {
                        videosList.add(new Videos(
                                cursor.getString(columnIndexData),              //Video path
                                cursor.getString(thumbnailData)                 //Thumbnail
                        ));
                    }
                    cursor.close();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (mColumnCount <= 1) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                } else {
//                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), mColumnCount);
                    FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getActivity(), FlexboxLayout.LAYOUT_DIRECTION_LTR);
                    flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
//                    flexboxLayoutManager.setAlignContent(AlignContent.FLEX_START);
                    flexboxLayoutManager.setAlignItems(AlignItems.CENTER);
                    flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
                    recyclerView.setLayoutManager(flexboxLayoutManager);
                }
                recyclerView.setAdapter(new VideoGalleryAdapter(videosList, getActivity()));
                super.onPostExecute(aVoid);
            }
        }.execute();

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        }
//        else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(DummyItem item);
    }
}
