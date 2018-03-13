package com.cncoding.teazer.ui.home.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostReaction;
import com.cncoding.teazer.data.model.react.ReactVideoDetailsResponse;
import com.cncoding.teazer.ui.customviews.coachMark.MaterialShowcaseSequence;
import com.cncoding.teazer.ui.home.camera.UploadFragment;
import com.cncoding.teazer.ui.home.discover.DiscoverFragment;
import com.cncoding.teazer.ui.home.post.detailspage.PostDetailsFragment;
import com.cncoding.teazer.ui.home.post.homepage.PostsListFragment;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentNewOtherProfile;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentNewProfile2;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentReactionPlayer;
import com.cncoding.teazer.utilities.common.SharedPrefs;
import com.expletus.mobiruck.MobiruckEvent;
import com.expletus.mobiruck.MobiruckSdk;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.jetbrains.annotations.Contract;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.ui.customviews.coachMark.MaterialShowcaseView.TYPE_DISCOVER;
import static com.cncoding.teazer.ui.customviews.coachMark.MaterialShowcaseView.TYPE_NORMAL;
import static com.cncoding.teazer.ui.home.base.BaseBottomBarActivity.COACH_MARK_DELAY;
import static com.cncoding.teazer.ui.home.base.BaseBottomBarActivity.NOTIFICATION_TYPE;
import static com.cncoding.teazer.ui.home.base.BaseBottomBarActivity.POST_ID;
import static com.cncoding.teazer.ui.home.base.BaseBottomBarActivity.SOURCE_ID;
import static com.cncoding.teazer.utilities.common.CommonUtilities.MEDIA_TYPE_GIF;
import static com.cncoding.teazer.utilities.common.CommonUtilities.MEDIA_TYPE_GIPHY;
import static com.cncoding.teazer.utilities.common.NavigationController.TAB1;
import static com.cncoding.teazer.utilities.common.NavigationController.TAB2;
import static com.cncoding.teazer.utilities.common.NavigationController.TAB5;
import static com.cncoding.teazer.utilities.common.ViewUtils.getCoachMark;
import static com.cncoding.teazer.utilities.common.ViewUtils.getTabChild;
import static com.cncoding.teazer.utilities.common.ViewUtils.openProfile;
import static com.cncoding.teazer.utilities.common.ViewUtils.showCoachMark;

/**
 *
 * Created by Prem$ on 3/12/2018.
 */

public class IntentHandler {

    public static final String IS_SIGNUP = "signupOrLogin";

    static void logMobiruckEvent() {
        //logging mobiruck event
        new Thread(new Runnable() {
            @Override
            public void run() {
                MobiruckEvent mobiruckEvent = new MobiruckEvent();

                mobiruckEvent.setEvent("logged_in");  // event name should match as added in the dashboard.

                MobiruckSdk.getInstance().logEvent(mobiruckEvent);
            }
        }).start();
    }

    @NonNull @Contract(pure = true)
    static BroadcastReceiver getBroadcastReceiver(final BaseBottomBarActivity activity) {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int postId = Integer.parseInt(intent.getStringExtra("PostID"));
                String postTitle = intent.getStringExtra("PostTitle");
                String postUrl = intent.getStringExtra("PostURL");
                String postOwnerId = intent.getStringExtra("PostOwner");

                BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                        .setCanonicalIdentifier(postOwnerId)
                        .setTitle(postTitle)
                        .setContentDescription("Watch " + postTitle + "awesome video on Teazer app")
                        .setContentImageUrl(postUrl);

                LinkProperties linkProperties = new LinkProperties()
                        .setChannel("facebook")
                        .setFeature("sharing")
                        .addControlParameter("post_id", String.valueOf(postId))
                        .addControlParameter("$desktop_url", "https://teazer.in/")
                        .addControlParameter("$ios_url", "https://teazer.in/");

                branchUniversalObject.generateShortUrl(activity.getApplicationContext(), linkProperties, new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if (error == null) {

                            if (UploadFragment.checkFacebookButtonPressed) {
                                ShareLinkContent content = new ShareLinkContent.Builder().setContentUrl(Uri.parse(url)).build();
                                ShareDialog shareDialog = new ShareDialog(activity);
                                shareDialog.show(content);
                                ShareApi.share(content, null);
                                UploadFragment.checkFacebookButtonPressed = false;
                            }

                            if (UploadFragment.checkedTwitterButton) {
                                shareTwitter(url, activity);
                                UploadFragment.checkFacebookButtonPressed = false;
                            }
                        }
                    }
                });
            }
        };
    }

    private static void shareTwitter(String message, BaseBottomBarActivity activity) {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, message);
        tweetIntent.setType("text/plain");

        PackageManager packManager = activity.getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            activity.startActivity(tweetIntent);
        } else {
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(message)));
            activity.startActivity(i);
            Toast.makeText(activity, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("TAG", "UTF-8 should always be supported", e);
            return "";
        }
    }

    static void handleNotificationIntentIfAny(BaseBottomBarActivity activity) {
        try {
            Bundle extras = activity.getIntent().getExtras();
            if (extras != null) {
                try {
                    Log.d("NOTIFY", "BUNDLE Exists in onStart");
                    Log.d("NOTIFY", extras.toString());
                    String notification_type = extras.getString("notification_type");
                    String source_id = extras.getString("source_id");
                    String post_id = extras.getString("post_id");
                    notificationAction(activity, Integer.valueOf(notification_type), Integer.valueOf(source_id), Integer.valueOf(post_id));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Log.e("NOTIFY", "BUNDLE does not exist in onStart");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("NOTIFY", "No Intent in activity");
        }
    }

    static void handleNewIntent(BaseBottomBarActivity activity, Intent intent) {
        try {
            if (intent.getExtras() != null) {
                //noinspection ConstantConditions
                Bundle profileBundle = activity.getIntent().getExtras().getBundle("profileBundle");
                Bundle notificationBundle = intent.getExtras().getBundle("bundle");
//                Bundle likedUserProfile = intent.getExtras().getBundle("LikedUserprofileBundle");

                if (notificationBundle != null) {
                    Log.d("NOTIFY", "BUNDLE Exists on new Intent");
                    int notification_type = notificationBundle.getInt(NOTIFICATION_TYPE);
                    int source_id = notificationBundle.getInt(SOURCE_ID);
                    int post_id = notificationBundle.getInt(POST_ID);
                    notificationAction(activity, notification_type, source_id, post_id);
                } else if (profileBundle != null) {

                    int userId = profileBundle.getInt("userId");
                    boolean isSelf = profileBundle.getBoolean("isSelf");
                    activity.postDetails = profileBundle.getParcelable("PostDetails");
                    openProfile(activity, isSelf, userId);
                } else {
                    Log.d("NOTIFY", "BUNDLE not present on new Intent");
                    activity.switchTabDynamically();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void notificationAction(final BaseBottomBarActivity activity, int notification_type, int source_id, int post_id) {
        if (notification_type == 11) {
            //do nothing as of now
            Log.d("Notification", String.valueOf(notification_type));
        }
        if (notification_type == 1 || notification_type == 2 || notification_type == 3 || notification_type == 10) {
            activity.pushFragment(FragmentNewOtherProfile.newInstance3(String.valueOf(source_id), String.valueOf(notification_type)));
        } else if (notification_type == 5 || notification_type == 7 || notification_type == 9) {
            ApiCallingService.Posts.getPostDetails(source_id, activity)
                    .enqueue(new Callback<PostDetails>() {
                        @Override
                        public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    activity.pushFragment(PostDetailsFragment.newInstance(response.body(), true, null));
                                } else {
                                    Toast.makeText(activity, "Either post is not available or deleted by owner", Toast.LENGTH_SHORT).show();
                                }
                            } else
                                Toast.makeText(activity, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<PostDetails> call, Throwable t) {
                            Toast.makeText(activity, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            if (post_id != 0) {
                ApiCallingService.React.getReactionDetail2(source_id, activity.getApplicationContext())
                        .enqueue(new Callback<ReactVideoDetailsResponse>() {
                            @Override
                            public void onResponse(Call<ReactVideoDetailsResponse> call, Response<ReactVideoDetailsResponse> response) {
                                if (response.code() == 200) {
                                    if (response.body() != null) {
                                        PostReaction postReactDetail = response.body().getPostReactDetail();
                                        if (postReactDetail.getMediaDetail().getMediaType() == MEDIA_TYPE_GIF || postReactDetail.getMediaDetail().getMediaType() == MEDIA_TYPE_GIPHY) {
                                            activity.pushFragment(FragmentReactionPlayer.newInstance(postReactDetail, true));
                                        } else {
                                            activity.pushFragment(FragmentReactionPlayer.newInstance(postReactDetail, true));
                                        }
                                    } else {
                                        Toast.makeText(activity, "Either post is not available or deleted by owner", Toast.LENGTH_SHORT).show();
                                    }
                                } else
                                    Toast.makeText(activity, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<ReactVideoDetailsResponse> call, Throwable t) {
                                Toast.makeText(activity, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                ApiCallingService.Posts.getPostDetails(source_id, activity)
                        .enqueue(new Callback<PostDetails>() {
                            @Override
                            public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                                if (response.code() == 200)
                                    activity.pushFragment(PostDetailsFragment.newInstance(response.body(), false, null));
                                else if (response.code() == 412 && response.message().contains("Precondition Failed"))
                                    Toast.makeText(activity, "This post no longer exists", Toast.LENGTH_SHORT).show();
                                else {
                                    Log.d("FETCHING PostDetails", response.code() + " : " + response.message());
                                    Toast.makeText(activity, "Error fetching post", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PostDetails> call, Throwable t) {
                                t.printStackTrace();
                                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    static void getBranchDynamicLinks(final BaseBottomBarActivity activity) {
        //noinspection ConstantConditions
        Branch.getInstance().initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(final JSONObject referringParams, BranchError error) {
                if (error == null) {
                    try {
                        if (referringParams != null) {
                            Log.d("BranchLog", referringParams.toString());
                            if (referringParams.has("post_id")) {
                                String postId = referringParams.getString("post_id");
                                ApiCallingService.Posts.getPostDetails(Integer.parseInt(postId), activity)
                                        .enqueue(new Callback<PostDetails>() {
                                            @Override
                                            public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                                                if (response.code() == 200) {
                                                    if (response.body() != null) {
                                                        if (referringParams.has("react_id")) {
                                                            try {
                                                                activity.pushFragment(PostDetailsFragment.newInstance(response.body(), true, referringParams.getString("react_id")));
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        } else {
                                                            activity.pushFragment(PostDetailsFragment.newInstance(response.body(), true, null));
                                                        }
                                                    } else {
                                                        Toast.makeText(activity, "Either post is not available or deleted by owner", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else
                                                    Toast.makeText(activity, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onFailure(Call<PostDetails> call, Throwable t) {
                                                Toast.makeText(activity, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else if (referringParams.has("user_id")) {
                                String userId = referringParams.getString("user_id");
                                openProfile(activity, SharedPrefs.getUserId(activity) == Integer.parseInt(userId), userId);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("BRANCH SDK", error.getMessage());
                }
            }
        }, activity.getIntent().getData(), activity);
    }

    static void mayeShowCoachMarks(final BaseBottomBarActivity activity, int position, final Fragment currentFragment) {
        switch (position) {
            case TAB1:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (currentFragment instanceof PostsListFragment && currentFragment.isAdded())
                            getCoachMark(activity, currentFragment,
                                    getTabChild(activity.bottomTabLayout, TAB1), "homePage", R.string.welcome_to_teazer,
                                    R.string.coach_mark_post_list_body, R.string.okay_got_it, TYPE_NORMAL);
                    }
                }, COACH_MARK_DELAY);
                break;
            case TAB2:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (currentFragment instanceof DiscoverFragment && currentFragment.isAdded()) {
                                MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(
                                        activity, "discoverLanding");
//                                    sequence.setConfig(getShowcaseConfig(activity, TYPE_DISCOVER));
                                sequence.addSequenceItem(
                                        showCoachMark(
                                                activity,
                                                getTabChild(activity.bottomTabLayout, TAB2),
                                                "discover",
                                                R.string.discover_the_app,
                                                R.string.coach_mark_discover_body,
                                                R.string.next,
                                                TYPE_NORMAL)
                                ).addSequenceItem(
                                        showCoachMark(
                                                activity,
                                                ((DiscoverFragment) currentFragment).myInterestsViewAll,
                                                "myInterests",
                                                R.string.my_interests,
                                                R.string.coach_mark_my_interests_body,
                                                R.string.done,
                                                TYPE_DISCOVER));
                                sequence.start();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, COACH_MARK_DELAY);
                break;
            case TAB5:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (currentFragment instanceof FragmentNewProfile2 && currentFragment.isAdded())
                            getCoachMark(activity, currentFragment,
                                    getTabChild(activity.bottomTabLayout, TAB5), "profilePage", R.string.my_profile,
                                    R.string.coach_mark_profile_body, R.string.okay_got_it, TYPE_NORMAL);
                    }
                }, COACH_MARK_DELAY);
                break;
            default:
                break;
        }
    }
}