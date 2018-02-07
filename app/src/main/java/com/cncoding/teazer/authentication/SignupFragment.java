package com.cncoding.teazer.authentication;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.TypeFactory;
import com.cncoding.teazer.model.base.Authorize;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

import static android.app.Activity.RESULT_OK;
import static com.cncoding.teazer.MainActivity.EMAIL_SIGNUP_PROCEED_ACTION;
import static com.cncoding.teazer.utilities.AuthUtils.togglePasswordVisibility;
import static com.cncoding.teazer.utilities.ViewUtils.RESULT_CROP;
import static com.cncoding.teazer.utilities.ViewUtils.clearDrawables;
import static com.cncoding.teazer.utilities.ViewUtils.hideKeyboard;
import static com.cncoding.teazer.utilities.ViewUtils.setEditTextDrawableEnd;

public class SignupFragment extends AuthFragment {

    @BindView(R.id.dp) CircularAppCompatImageView dp;
    @BindView(R.id.dp_edit_btn) CircularAppCompatImageView dpEditBtn;
    @BindView(R.id.signup_username) ProximaNovaRegularAutoCompleteTextView usernameView;
    @BindView(R.id.signup_password) ProximaNovaRegularAutoCompleteTextView passwordView;
    @BindView(R.id.signup__proceed_btn) AppCompatButton signupProceedBtn;

    private OnInitialSignupInteractionListener mListener;
    private String picturePath;
    private Bitmap selectedBitmap;
    //    private Context context;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, rootView);
//        nameView.setFilters(new InputFilter[] {FilterFactory.nameFilter});
//        emailView.setFilters(new InputFilter[] {FilterFactory.emailFilter});
        if (selectedBitmap != null) {
            dp.setImageBitmap(selectedBitmap);
            dpEditBtn.setImageResource(R.drawable.ic_create_back_black);
        }

        return rootView;
    }

    @OnEditorAction(R.id.signup_password) public boolean onLoginByKeyboard(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            hideKeyboard(getParentActivity(), passwordView);
            signupProceed();
            return true;
        }
        return false;
    }

    @OnTextChanged(R.id.signup_password) public void signupPasswordTextChanged(CharSequence charSequence) {
        if (charSequence.toString().equals(""))
            clearDrawables(passwordView);
        else
        if (passwordView.getCompoundDrawables()[2] == null)
            setEditTextDrawableEnd(passwordView, R.drawable.ic_view_filled);
    }

    @OnClick(R.id.signup_password) public void onSignupPasswordShow() {
        togglePasswordVisibility(passwordView, context);
    }

    @OnFocusChange(R.id.signup_username) public void onUsernameFocusChanged(boolean isFocused) {
        if (!isFocused) {
            String username = usernameView.getText().toString();
            if (!username.isEmpty()) {
                ApiCallingService.Auth.checkUsername(getContext(), usernameView, true);
            } else {
                setEditTextDrawableEnd(usernameView, R.drawable.ic_error);
            }
        }
    }

    @OnTextChanged(R.id.signup_username) public void signupUsernametextChanged(CharSequence charSequence) {
        if (charSequence.toString().equals(""))
            clearDrawables(usernameView);
                  setEditTextDrawableEnd(passwordView, R.drawable.ic_view_filled);
    }


    @OnClick(R.id.dp_edit_btn) public void launchSelector() {
        launchImageSelector();
    }

    @OnClick(R.id.dp) public void launchImageSelector() {

//        CropImage.activity()
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .start(getActivity());
//
        CropImage.activity()
                .start(getContext(), this);


//
//        Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
//        startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
    }

    @OnClick(R.id.signup__proceed_btn) public void signupProceed() {
        if (areAllViewsFilled()) {
            if (usernameView.getText().toString().length() >= 4 && usernameView.getText().toString().length() <= 50) {

                if (isPasswordValid(passwordView.getText().toString())) {

                    mListener.onInitialEmailSignupInteraction(EMAIL_SIGNUP_PROCEED_ACTION,
                                    new Authorize(usernameView.getText().toString(), passwordView.getText().toString()), picturePath);

                } else
                    Snackbar.make(signupProceedBtn, "Password must be 8 to 32 characters", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(signupProceedBtn, "Username must be 4 to 50 characters", Snackbar.LENGTH_SHORT).show();
            }
        } else
            Snackbar.make(signupProceedBtn, "All fields are required", Snackbar.LENGTH_SHORT).show();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.length() <= 32;
    }

    private boolean areAllViewsFilled() {
        return !usernameView.getText().toString().isEmpty() &&
                !passwordView.getText().toString().isEmpty();
    }

    private void performCrop(String picUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(Intent.createChooser(cropIntent, getString(R.string.crop_selected_image)), RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (Exception e) {
            // display an error message
            if (e instanceof ActivityNotFoundException)
                Toast.makeText(context, R.string.no_crop_action_support_message, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, R.string.error_choosing_photo, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prfs = getActivity().getSharedPreferences("AUTHENTICATION_USER_PROFILE", Context.MODE_PRIVATE);
        final String imageUri = prfs.getString("USER_DP_IMAGES", null);
        if(imageUri != null) {
            Glide.with(getActivity())
                    .load(Uri.parse(imageUri))
                    .into(dp);
        }
    }

    @OnTouch(R.id.signup_password)
    public boolean onPasswordShow(MotionEvent event) {
        if (passwordView.getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP &&
                    event.getRawX() >= passwordView.getRight() - passwordView.getCompoundDrawables()[2].getBounds().width() * 1.5) {
                if(isPasswodShown) {
                    passwordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled_cross, 0);
                    passwordView.setSelection(passwordView.getText().length());
                    passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordView.setTypeface(new TypeFactory(context).regular);
                    isPasswodShown=false;
                }
                else
                {
                    passwordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled, 0);
                    passwordView.setSelection(passwordView.getText().length());
                    passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    passwordView.setTypeface(new TypeFactory(context).regular);
                    isPasswodShown=true;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences = getActivity().getSharedPreferences("AUTHENTICATION_USER_PROFILE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
               // Toast.makeText(context, String.valueOf(resultUri), Toast.LENGTH_SHORT).show();
                picturePath = resultUri.toString();
                SharedPreferences preferences = getActivity().getSharedPreferences("AUTHENTICATION_USER_PROFILE", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("USER_DP_IMAGES", resultUri.toString());
                editor.apply();
                Glide.with(getActivity())
                        .load(resultUri)
                        .into(dp);
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);


                //    Blurry.with(getActivity()).from(scaledBitmap).into(bgImage);
                  //   dp.setImageBitmap(scaledBitmap);
                    //  byte[] bte = bitmapToByte(scaledBitmap);
                    // File profileImage = new File(r.getPath());
                  //  RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), bte);
                  //  MultipartBody.Part body = MultipartBody.Part.createFormData("media", "profile_image.jpg", reqFile);
                 //   saveDataToDatabase(body);




                }catch (Exception e)
                {
                    e.printStackTrace();
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

            }
        }
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case GALLERY_ACTIVITY_CODE:
//                    picturePath = data.getStringExtra("picturePath");
//                    //perform Crop on the Image Selected from Gallery
//                    performCrop(picturePath);
//                    break;
//                case RESULT_CROP:
//                    if (data != null) {
//                        Bundle extras = data.getExtras();
//                        Bitmap selectedBitmap = null;
//                        if (extras != null) {
//                            selectedBitmap = extras.getParcelable("data");
//                        }
//                        if (selectedBitmap == null && data.getData() != null)
//                            selectedBitmap = BitmapFactory.decodeFile(data.getData().getEncodedPath());
//                        // Set The Bitmap Data To ImageView
//                        dp.setImageBitmap(selectedBitmap);
//                        dpEditBtn.setImageResource(R.drawable.ic_create_back_black);
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInitialSignupInteractionListener) {
            mListener = (OnInitialSignupInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LoginInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnInitialSignupInteractionListener {
        void onInitialEmailSignupInteraction(int action, Authorize signUpDetails, String picturePath);
    }


}