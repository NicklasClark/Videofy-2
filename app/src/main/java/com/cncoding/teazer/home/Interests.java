package com.cncoding.teazer.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.utilities.Pojos;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Interests extends Fragment implements View.OnClickListener {
    private static final String LAUNCH_TYPE = "launchType";
//    private static final String CATEGORIES_LIST = "categoriesList";

    public static final int LAUNCH_TYPE_SIGNUP = 1000;
    public static final int LAUNCH_TYPE_UPLOAD = 2000;

    @BindView(R.id.interests_selection_layout) FlexboxLayout flexboxLayout;
    @BindView(R.id.save_interests_btn) ProximaNovaSemiboldButton saveBtn;
    @BindView(R.id.interests_header) ProximaNovaRegularTextView header;

    private int categoriesLimit;
    private int launchType;
    private String previousTitle;
    private HashMap<String, Integer> interestsMap;
    private ArrayList<String> selectedInterestsValues;
    private ArrayList<Integer> selectedInterestsKeys;
    private int selectedCategoryCount = 0;

    private OnInterestsInteractionListener mListener;
    private ActionBar actionBar;

    public Interests() {
        // Required empty public constructor
    }

    /**
     * @param launchType can be {@value LAUNCH_TYPE_SIGNUP} for LAUNCH_TYPE_SIGNUP
     *                   or {@value LAUNCH_TYPE_UPLOAD} for LAUNCH_TYPE_UPLOAD.
     * */
    public static Interests newInstance(int launchType) {
        Interests fragment = new Interests();
        Bundle args = new Bundle();
        args.putInt(LAUNCH_TYPE, launchType);
//        args.putParcelableArrayList(CATEGORIES_LIST, args);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            launchType = getArguments().getInt(LAUNCH_TYPE);
            interestsMap = new HashMap<>();
            selectedInterestsValues = new ArrayList<>();
            selectedInterestsKeys = new ArrayList<>();
//            categories = getArguments().getParcelableArrayList(CATEGORIES_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_interests, container, false);
        ButterKnife.bind(this, rootView);

        switch (launchType) {
            case LAUNCH_TYPE_SIGNUP:
                header.setText(R.string.select_your_interests);
                saveBtn.setText(R.string.select_at_least_5_interests);
                break;
            case LAUNCH_TYPE_UPLOAD:
                header.setText("Select up to 5 categories for your video.");
                break;
            default:
                break;
        }

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            if (actionBar.getTitle() != null)
                previousTitle = actionBar.getTitle().toString();
            actionBar.setTitle("Select your interests");
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoriesLimit = (launchType == LAUNCH_TYPE_UPLOAD)? 5 : -1;

        ApiCallingService.Application.getCategories().enqueue(new Callback<ArrayList<Pojos.Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Pojos.Category>> call, Response<ArrayList<Pojos.Category>> response) {
                interestsMap = new HashMap<>();
                for (int i = 0; i < response.body().size(); i++) {
                    addChip(i, response.body().get(i).getCategoryName());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Pojos.Category>> call, Throwable t) {
                Log.e("getCategories", t.getMessage());
            }
        });
    }

    private void addChip(int position, final String text) {
        interestsMap.put(text, ++position);
        new Handler().postDelayed(new Runnable() {
            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(getPixels(8), getPixels(8), getPixels(8), getPixels(8));
                ProximaNovaRegularCheckedTextView textView = new ProximaNovaRegularCheckedTextView(
                        getContext());
                textView.setLayoutParams(layoutParams);
                textView.setId(View.generateViewId());
//              textView.setTextAppearance(getContext(), R.style.AppTheme_ChipLayout);
                textView.setPadding(getPixels(16), getPixels(8), getPixels(16), getPixels(8));
                textView.setBackground(getActivity().getResources().getDrawable(R.drawable.chip_default));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                textView.setTextColor(Color.WHITE);
                textView.setText("+ " + text);
                textView.setOnClickListener(Interests.this);
                flexboxLayout.addView(textView);
            }
        }, 200);
    }

    @OnClick(R.id.save_interests_btn) public void saveInterests() {
//        mListener.onFragmentInteraction(selectedInterestsValues);
        switch (launchType) {
            case LAUNCH_TYPE_SIGNUP:
                ApiCallingService.User.updateCategories(new Pojos.User.UpdateCategories(
                        Arrays.toString(selectedInterestsValues.toArray()).replace("[", "").replace("]", "")))
                        .enqueue(new Callback<ResultObject>() {
                            @Override
                            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                if (response.code() == 200) {
                                    Toast.makeText(getContext(), "Category updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResultObject> call, Throwable t) {
                                Log.e("Updating categories", t.getMessage());
                            }
                        });
                break;
            case LAUNCH_TYPE_UPLOAD:
                StringBuilder selectedInterest = new StringBuilder();
                for (int i = 0; i < selectedInterestsKeys.size(); i++) {
                    selectedInterest.append(selectedInterestsKeys.get(i));
                    if (i < selectedInterestsKeys.size() - 1)
                        selectedInterest.append(",");
                }
                mListener.onInterestsInteraction(selectedInterest.toString());
                break;
            default:
                break;
        }
    }

    private int getPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getActivity().getResources().getDisplayMetrics());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View view) {
        if (view instanceof ProximaNovaRegularCheckedTextView) {
            String text = ((ProximaNovaRegularCheckedTextView) view).getText().toString().replace("+ ", "");
            if (!((ProximaNovaRegularCheckedTextView) view).isChecked()) {
                switch (launchType) {
                    case LAUNCH_TYPE_SIGNUP:
                        incrementCategory((ProximaNovaRegularCheckedTextView) view, text);
                        if (selectedCategoryCount >= 5) {
                            saveBtn.setEnabled(true);
                            saveBtn.setText(R.string.save);
                            saveBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick_circle_outline, 0);
                        }
                        break;
                    case LAUNCH_TYPE_UPLOAD:
                        if (selectedCategoryCount < categoriesLimit) {
                            incrementCategory((ProximaNovaRegularCheckedTextView) view, text);
                        } else
                            Snackbar.make(view, "Sorry, but " + categoriesLimit + " is the limit for now", Snackbar.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            } else {
                selectedCategoryCount--;
                ((ProximaNovaRegularCheckedTextView) view).setChecked(false);
                view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.deselected));
                view.setBackground(getActivity().getResources().getDrawable(R.drawable.chip_default));
                ((ProximaNovaRegularCheckedTextView) view)
                        .setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/proxima_nova_regular.ttf"));
                selectedInterestsValues.remove(text);
                selectedInterestsKeys.remove(interestsMap.get(text));
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void incrementCategory(ProximaNovaRegularCheckedTextView view, String text) {
        selectedCategoryCount++;
        view.setChecked(true);
        view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.selected));
        view.setBackground(getActivity().getResources().getDrawable(R.drawable.chip_selected));
        view.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/proxima_nova_semibold.ttf"));
        selectedInterestsValues.add(text);
        selectedInterestsKeys.add(interestsMap.get(text));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnInterestsInteractionListener) {
//            mListener = (OnInterestsInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnInterestsInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (actionBar != null) {
            if (previousTitle != null)
                actionBar.setTitle(previousTitle);
            else actionBar.setTitle(getString(R.string.app_name));
        }
    }

    public interface OnInterestsInteractionListener {
        void onInterestsInteraction(String result);
    }
}
