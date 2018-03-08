package com.cncoding.teazer.ui.home.profile.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.apiCalls.ResultObject;
import com.cncoding.teazer.ui.home.profile.activity.EditProfile;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by farazhabib on 12/12/17.
 */

public class FragmentChangeRemoveProfilePic extends DialogFragment {


    Activity context;

    public FragmentChangeRemoveProfilePic() {}

    public static FragmentChangeRemoveProfilePic newInstance(String details, String imageUrls) {
        FragmentChangeRemoveProfilePic frag = new FragmentChangeRemoveProfilePic();
        Bundle args = new Bundle();
        args.putString("details", details);
        args.putString("imageUrls", imageUrls);
        frag.setArguments(args);
        return frag;
    }
    public static FragmentChangeRemoveProfilePic newInstance2() {
        FragmentChangeRemoveProfilePic frag = new FragmentChangeRemoveProfilePic();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_change_remove_profilepic, container);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.chnageprofilepic)
    public void changeProfilepic()
    {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setFixAspectRatio(true)
                .start(context);
        getDialog().dismiss();
    }
    @OnClick(R.id.removeprofilepic)
    public void removeProfilePic()
    {
        getDialog().dismiss();
        ApiCallingService.User.removeProfilePicture(context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        if (response.body().getStatus()) {
                            Toast.makeText(context, "Your Profile pic has been removed", Toast.LENGTH_SHORT).show();
                            FragmentNewProfile2.checkprofileupdated = true;
                            FragmentNewProfile2. checkpicUpdated = true;
                            ((EditProfile)context).removeProfilePic();
                        } else {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Something went wrong Please try again", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(context, "Please check your data is correct", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "Network Issue Please check once again ", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        context=activity;
    }


}
