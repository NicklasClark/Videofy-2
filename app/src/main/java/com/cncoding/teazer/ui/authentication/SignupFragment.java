package com.cncoding.teazer.ui.authentication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
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

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.auth.ProceedSignup;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.ui.authentication.base.BaseAuthFragment;
import com.cncoding.teazer.ui.customviews.common.CircularAppCompatImageView;
import com.cncoding.teazer.ui.customviews.common.TypeFactory;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.utilities.common.Annotations;
import com.theartofdev.edmodo.cropper.CropImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

import static android.app.Activity.RESULT_OK;
import static com.cncoding.teazer.ui.authentication.base.MainActivity.EMAIL_SIGNUP_PROCEED_ACTION;
import static com.cncoding.teazer.utilities.common.AuthUtils.isConnected;
import static com.cncoding.teazer.utilities.common.ViewUtils.clearDrawables;
import static com.cncoding.teazer.utilities.common.ViewUtils.hideKeyboard;
import static com.cncoding.teazer.utilities.common.ViewUtils.setEditTextDrawableEnd;

@SuppressLint("SwitchIntDef")
public class SignupFragment extends BaseAuthFragment {

    @BindView(R.id.dp) CircularAppCompatImageView dp;
    @BindView(R.id.dp_edit_btn) CircularAppCompatImageView dpEditBtn;
    @BindView(R.id.signup_username) ProximaNovaRegularAutoCompleteTextView usernameView;
    @BindView(R.id.signup_password) ProximaNovaRegularAutoCompleteTextView passwordView;
    @BindView(R.id.signup__proceed_btn) AppCompatButton signupProceedBtn;

    private OnInitialSignupInteractionListener mListener;
    private String picturePath;
    private ProceedSignup proceedSignup;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (proceedSignup == null) proceedSignup = new ProceedSignup();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, rootView);
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

    @OnTextChanged(R.id.signup_username) public void signupUsernameTextChanged(CharSequence charSequence) {
        proceedSignup.setUserName(charSequence.toString());
    }

    @OnTextChanged(R.id.signup_password) public void signupPasswordTextChanged(CharSequence charSequence) {
        try {
            proceedSignup.setPassword(charSequence.toString());
            if (charSequence.toString().equals(""))
                clearDrawables(passwordView);
            else
            if (passwordView.getCompoundDrawables()[2] == null)
                setEditTextDrawableEnd(passwordView, R.drawable.ic_view_filled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnFocusChange(R.id.signup_username) public void onUsernameFocusChanged(boolean isFocused) {
        try {
            if (!isFocused) {
                if (proceedSignup.getUserName() != null && !proceedSignup.getUserName().isEmpty()) {
                    checkUsernameAvailability(usernameView.getText().toString());
                } else {
                    setEditTextDrawableEnd(usernameView, R.drawable.ic_error);
                }
            } else {
                clearDrawables(usernameView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.dp_edit_btn) public void launchSelector() {
        launchImageSelector();
    }

    @OnClick(R.id.dp) public void launchImageSelector() {
        CropImage.activity().start(getParentActivity(), this);
//        CropImage.activity()
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .start(getActivity());
//
//
//        Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
//        startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
    }

    @OnClick(R.id.signup__proceed_btn) public void signupProceed() {
        try {
            if (isConnected(context)) {
                if (isFieldFilled(Annotations.CHECK_USERNAME) && isFieldFilled(Annotations.CHECK_PASSWORD)) {
                    if (isFieldValidated(Annotations.CHECK_USERNAME)) {
                        if (isFieldValidated(Annotations.CHECK_PASSWORD)) {
                            mListener.onInitialEmailSignupInteraction(EMAIL_SIGNUP_PROCEED_ACTION, proceedSignup, picturePath);
                        }
                        else Snackbar.make(signupProceedBtn, "Password must be 8 to 32 characters", Snackbar.LENGTH_SHORT).show();
                    }
                    else Snackbar.make(signupProceedBtn, "Username must be 4 to 50 characters", Snackbar.LENGTH_SHORT).show();
                }
                else Snackbar.make(signupProceedBtn, "All fields are required", Snackbar.LENGTH_SHORT).show();
            }
            else notifyNoInternetConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void performCrop(String picUri) {
//        try {
//            //Start Crop Activity
//
//            Intent cropIntent = new Intent("com.android.camera.action.CROP");
//            // indicate image type and Uri
//            File f = new File(picUri);
//            Uri contentUri = Uri.fromFile(f);
//
//            cropIntent.setDataAndType(contentUri, "image/*");
//            // set crop properties
//            cropIntent.putExtra("crop", "true");
//            // indicate aspect of desired crop
//            cropIntent.putExtra("aspectX", 1);
//            cropIntent.putExtra("aspectY", 1);
//            // indicate output X and Y
//            cropIntent.putExtra("outputX", 280);
//            cropIntent.putExtra("outputY", 280);
//
//            // retrieve data on return
//            cropIntent.putExtra("return-data", true);
//            // start the activity - we handle returning in onActivityResult
//            startActivityForResult(Intent.createChooser(cropIntent, getString(R.string.crop_selected_image)), RESULT_CROP);
//        }
//        // respond to users whose devices do not support the crop action
//        catch (Exception e) {
//            // display an error message
//            if (e instanceof ActivityNotFoundException)
//                Toast.makeText(context, R.string.no_crop_action_support_message, Toast.LENGTH_SHORT).show();
//            else
//                Toast.makeText(context, R.string.error_choosing_photo, Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            SharedPreferences prefs = getParentActivity().getSharedPreferences("AUTHENTICATION_USER_PROFILE", Context.MODE_PRIVATE);
            final String imageUri = prefs.getString("USER_DP_IMAGES", null);
            if(imageUri != null) {
                Glide.with(this)
                        .load(Uri.parse(imageUri))
                        .into(dp);
                dpEditBtn.setImageResource(R.drawable.ic_create_back_black);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnTouch(R.id.signup_password) public boolean onPasswordShow(MotionEvent event) {
        try {
            if (passwordView.getCompoundDrawables()[2] != null) {
                if (event.getAction() == MotionEvent.ACTION_UP &&
                        event.getRawX() >= passwordView.getRight() - passwordView.getCompoundDrawables()[2].getBounds().width() * 1.5) {
                    if(isPasswordShown) {
                        passwordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled_cross, 0);
                        passwordView.setSelection(passwordView.getText().length());
                        passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        passwordView.setTypeface(new TypeFactory(context).regular);
                        isPasswordShown =false;
                    }
                    else
                    {
                        passwordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled, 0);
                        passwordView.setSelection(passwordView.getText().length());
                        passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                        passwordView.setTypeface(new TypeFactory(context).regular);
                        isPasswordShown =true;
                    }
                    return true;
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences = getParentActivity().getSharedPreferences("AUTHENTICATION_USER_PROFILE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    protected void handleResponse(ResultObject resultObject) {
        if (resultObject.getAuthCallType() == Annotations.CHECK_USERNAME_AVAILABILITY) {
            setEditTextDrawableEnd(usernameView, resultObject.getStatus() ? R.drawable.ic_cross :
                    R.drawable.ic_tick_circle);
        }
    }

    @Override
    protected void handleError(ResultObject resultObject) {
    }

    @Override
    protected void notifyNoInternetConnection() {
        Snackbar.make(signupProceedBtn, R.string.no_internet_connection, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected boolean isFieldValidated(int whichType) {
        switch (whichType) {
            case Annotations.CHECK_USERNAME:
                return proceedSignup.getUserName() != null &&
                        proceedSignup.getUserName().length() > 4 && proceedSignup.getUserName().length() <= 50;
            case Annotations.CHECK_PASSWORD:
                return proceedSignup.getPassword() != null &&
                        proceedSignup.getPassword().length() >= 8 && proceedSignup.getPassword().length() <= 32;
            default:
                return false;
        }
    }

    @Override
    protected boolean isFieldFilled(int whichType) {
        switch (whichType) {
            case Annotations.CHECK_USERNAME:
                return proceedSignup.getUserName() != null && !proceedSignup.getUserName().isEmpty();
            case Annotations.CHECK_PASSWORD:
                return proceedSignup.getPassword() != null && !proceedSignup.getPassword().isEmpty();
            default:
                return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
               // Toast.makeText(context, String.valueOf(resultUri), Toast.LENGTH_SHORT).show();
                picturePath = resultUri.toString();
                SharedPreferences preferences = getParentActivity().getSharedPreferences("AUTHENTICATION_USER_PROFILE", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("USER_DP_IMAGES", resultUri.toString());
                editor.apply();
                Glide.with(this)
                        .load(resultUri)
                        .into(dp);
                dpEditBtn.setImageResource(R.drawable.ic_create_back_black);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInitialSignupInteractionListener) {
            mListener = (OnInitialSignupInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnInitialSignupInteractionListener {
        void onInitialEmailSignupInteraction(int action, ProceedSignup proceedSignup, String picturePath);
    }
}