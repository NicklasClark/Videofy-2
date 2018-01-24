package com.cncoding.teazer.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.res.Resources;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.api.ApiCalls;
import com.cncoding.teazer.data.api.ResultObject;
import com.cncoding.teazer.data.model.application.DeactivateTypes;
import com.cncoding.teazer.data.model.application.ReportPostTitlesResponse;
import com.cncoding.teazer.data.model.base.Authorize;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.discover.VideosList;
import com.cncoding.teazer.data.model.friends.CircleList;
import com.cncoding.teazer.data.model.friends.FollowersList;
import com.cncoding.teazer.data.model.friends.FollowingsList;
import com.cncoding.teazer.data.model.friends.ProfileInfo;
import com.cncoding.teazer.data.model.friends.UsersList;
import com.cncoding.teazer.data.model.post.LandingPosts;
import com.cncoding.teazer.data.model.post.LikedUserPost;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.data.model.post.PostReactionsList;
import com.cncoding.teazer.data.model.post.PostUploadResult;
import com.cncoding.teazer.data.model.post.ReportPost;
import com.cncoding.teazer.data.model.post.TaggedUsersList;
import com.cncoding.teazer.data.model.post.UpdatePostRequest;
import com.cncoding.teazer.data.model.react.ReactionResponse;
import com.cncoding.teazer.data.model.react.ReactionUploadResult;
import com.cncoding.teazer.data.model.react.ReactionsList;
import com.cncoding.teazer.data.model.react.ReportReaction;
import com.cncoding.teazer.data.model.updatemobilenumber.ChangeMobileNumber;
import com.cncoding.teazer.data.model.updatemobilenumber.UpdateMobileNumber;
import com.cncoding.teazer.data.model.user.BlockedUsersList;
import com.cncoding.teazer.data.model.user.DeactivateAccountRequest;
import com.cncoding.teazer.data.model.user.NotificationsList;
import com.cncoding.teazer.data.model.user.Profile;
import com.cncoding.teazer.data.model.user.ProfileUpdateRequest;
import com.cncoding.teazer.data.model.user.ReportUser;
import com.cncoding.teazer.data.model.user.SetPasswordRequest;
import com.cncoding.teazer.data.model.user.UpdateCategories;
import com.cncoding.teazer.data.model.user.UpdatePasswordRequest;
import com.cncoding.teazer.data.model.user.UserProfile;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by Prem $ on 10/3/2017.
 */

public abstract class ApiRepository implements ApplicationRepository, AuthenticationRepository, DiscoverRepository,
        FriendsRepository, PostsRepository, ReactRepository, UserRepository{

    @Override
    public LiveData<ResultObject> signUp(Authorize signUpBody) {
        return null;
    }

    @Override
    public LiveData<ResultObject> verifySignUp(Authorize verifySignUp) {
        return null;
    }

    @Override
    public LiveData<ResultObject> socialSignUp(Authorize socialSignUpDetails) {
        return null;
    }

    @Override
    public LiveData<PostList> getFeaturedPosts(int page) {
        return null;
    }

    @Override
    public LiveData<List<ReportPostTitlesResponse>> getPostReportTypes() {
        return null;
    }

    @Override
    public LiveData<PostList> getAllInterestedCategoriesVideos(int page, int categoryId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> loginWithPassword(Authorize loginWithPassword) {
        return null;
    }

    @Override
    public LiveData<List<ReportPostTitlesResponse>> getProfileReportTypes() {
        return null;
    }

    @Override
    public LiveData<ReactionUploadResult> uploadReaction(MultipartBody.Part video, int postId, String title) {
        return null;
    }

    @Override
    public LiveData<PostList> getAllMostPopularVideos(int page) {
        return null;
    }

    @Override
    public LiveData<ResultObject> loginWithOtp(Authorize phoneNumberDetails) {
        return null;
    }

    @Override
    public LiveData<List<DeactivateTypes>> getDeactivationTypesList() {
        return null;
    }

    @Override
    public LiveData<PostList> getTrendingVideos(int page, int categoryId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> likeDislikeReaction(int reactId, int status) {
        return null;
    }

    @Override
    public LiveData<ResultObject> verifyLoginWithOtp(Authorize verifyLoginWithOtp) {
        return null;
    }

    @Override
    public LiveData<ArrayList<Category>> getCategories() {
        return null;
    }

    @Override
    public LiveData<LandingPosts> getDiscoverPagePosts() {
        return null;
    }

    @Override
    public LiveData<ResultObject> getMyCircleWithSearchTerm(int page, String searchTerm) {
        return null;
    }

    @Override
    public LiveData<ResultObject> incrementReactionViewCount(int mediaId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> checkUsernameAvailability(String username) {
        return null;
    }

    @Override
    public LiveData<PostUploadResult> uploadVideo(MultipartBody.Part video, String title, String location, double latitude, double longitude, String tags, String categories) {
        return null;
    }

    @Override
    public LiveData<UsersList> getUsersListToFollow(int page) {
        return null;
    }

    @Override
    public LiveData<ResultObject> deleteReaction(int reactId) {
        return null;
    }

    @Override
    public LiveData<FollowingsList> getMyFollowing(int page) {
        return null;
    }

    @Override
    public LiveData<ResultObject> checkEmailAvailability(String email) {
        return null;
    }

    @Override
    public LiveData<UsersList> getUsersListToFollowWithSearchTerm(int page, String searchTerm) {
        return null;
    }

    @Override
    public LiveData<ResultObject> reportReaction(ReportReaction reportReaction) {
        return null;
    }

    @Override
    public LiveData<ResultObject> sendJoinRequestByUserId(int userId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> checkPhoneNumberAvailability(int countryCode, long phoneNumber) {
        return null;
    }

    @Override
    public LiveData<VideosList> getVideosWithSearchTerm(int page, String searchTerm) {
        return null;
    }

    @Override
    public LiveData<PostUploadResult> updatePost(UpdatePostRequest updatePostRequest) {
        return null;
    }

    @Override
    public LiveData<ResultObject> sendJoinRequestByUsername(String username) {
        return null;
    }

    @Override
    public LiveData<ResultObject> hideOrShowReaction(int reactId, int status) {
        return null;
    }

    @Override
    public LiveData<ResultObject> verifyForgotPasswordOtp(int otp) {
        return null;
    }

    @Override
    public LiveData<ResultObject> likeDislikePost(int postId, int status) {
        return null;
    }

    @Override
    public LiveData<ResultObject> acceptJoinRequest(int notificationId) {
        return null;
    }

    @Override
    public LiveData<ReactionsList> getMyReactions(int page) {
        return null;
    }

    @Override
    public LiveData<ResultObject> requestResetPasswordByEmail(String email) {
        return null;
    }

    @Override
    public LiveData<ResultObject> incrementViewCount(int mediaId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> getHiddenReactions(int page) {
        return null;
    }

    @Override
    public LiveData<ResultObject> deleteJoinRequest(int notificationId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> updateUserProfileMedia(MultipartBody.Part media) {
        return null;
    }

    @Override
    public LiveData<ResultObject> requestResetPasswordByPhone(Authorize phoneNumberDetails) {
        return null;
    }

    @Override
    public LiveData<ResultObject> deletePost(int postId) {
        return null;
    }

    @Override
    public LiveData<ReactionResponse> getReactionDetail(int reactId) {
        return null;
    }

    @Override
    public LiveData<CircleList> getMyCircle(int page) {
        return null;
    }

    @Override
    public LiveData<ResultObject> resetFcmToken(String header, String token) {
        return null;
    }

    @Override
    public LiveData<ResultObject> deletePostVideo(int postId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> getMyFollowingsWithSearchTerm(int page, String searchTerm) {
        return null;
    }

    @Override
    public LiveData<ResultObject> resetPasswordByOtp(Authorize resetPasswordDetails) {
        return null;
    }

    @Override
    public LiveData<ResultObject> setAccountVisibility(int accountType) {
        return null;
    }

    @Override
    public LiveData<ResultObject> reportPost(ReportPost reportPostDetails) {
        return null;
    }

    @Override
    public LiveData<FollowingsList> getFriendsFollowings(int page, int userId) {
        return null;
    }

    @Override
    public LiveData<UserProfile> getUserProfile() {
        return null;
    }

    @Override
    public LiveData<ResultObject> hideOrShowPost(int postId, int status) {
        return null;
    }

    @Override
    public LiveData<ResultObject> changeMobileNumber(ChangeMobileNumber changeMobileNumber) {
        return null;
    }

    @Override
    public LiveData<ResultObject> getFriendsFollowingsWithSearchTerm(int userId, int page, String searchTerm) {
        return null;
    }

    @Override
    public LiveData<PostDetails> getPostDetails(int postId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> updateMobileNumber(UpdateMobileNumber updateMobileNumber) {
        return null;
    }

    @Override
    public LiveData<TaggedUsersList> getTaggedUsers(int postId, int page) {
        return null;
    }

    @Override
    public LiveData<FollowersList> getMyFollowers(int page) {
        return null;
    }

    @Override
    public LiveData<PostList> getHiddenPosts(int page) {
        return null;
    }

    @Override
    public LiveData<Profile> getUserProfileDetail() {
        return null;
    }

    @Override
    public LiveData<ResultObject> getMyFollowersWithSearchTerm(int page, String searchTerm) {
        return null;
    }

    @Override
    public LiveData<PostList> getHomePagePosts(final int page, String authToken) {
        try {
            final MutableLiveData<PostList> liveData = new MutableLiveData<>();
            ApiCalls.Posts.getHomePagePosts(page, authToken).enqueue(new Callback<PostList>() {
                @Override
                public void onResponse(Call<PostList> call, Response<PostList> response) {
                    try {
                        switch (response.code()) {
                            case 200:
                                PostList tempPostList = response.body();
                                if (tempPostList.getPosts() != null && tempPostList.getPosts().size() > 0) {
                                    liveData.setValue(response.body());
                                } else {
                                    if (page == 1 && tempPostList.getPosts().isEmpty())
                                        liveData.setValue(new PostList(
                                                new Throwable(Resources.getSystem().getString(R.string.no_posts_available))));
                                }
                                break;
                            default:
                                liveData.setValue(new PostList(new Throwable("Couldn't load posts")));
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PostList> call, Throwable t) {
                    liveData.setValue(new PostList(t));
                }
            });
            return liveData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LiveData<ResultObject> updateUserProfile(ProfileUpdateRequest updateProfileDetails) {
        return null;
    }

    @Override
    public LiveData<FollowersList> getFriendsFollowers(int page, int userId) {
        return null;
    }

    @Override
    public LiveData<PostList> getPostedVideos(int page) {
        return null;
    }

    @Override
    public LiveData<ResultObject> updatePassword(UpdatePasswordRequest updatePasswordDetails) {
        return null;
    }

    @Override
    public LiveData<PostList> getVideosPostedByFriends(int page, int friendId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> getFriendsFollowersWithSearchTerm(int userId, int page, String searchTerm) {
        return null;
    }

    @Override
    public LiveData<PostList> getVideosPostedByFriend(int page, int friendId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> setPassword(SetPasswordRequest setPasswordDetails) {
        return null;
    }

    @Override
    public LiveData<ResultObject> unfollowUser(int userId) {
        return null;
    }

    @Override
    public LiveData<PostReactionsList> getReactionsOfPost(int postId, int page) {
        return null;
    }

    @Override
    public LiveData<NotificationsList> getFollowingNotifications(int page) {
        return null;
    }

    @Override
    public LiveData<ResultObject> cancelRequest(int userId) {
        return null;
    }

    @Override
    public LiveData<PostList> getHiddenVideosList(int page) {
        return null;
    }

    @Override
    public LiveData<ResultObject> followUser(int userId) {
        return null;
    }

    @Override
    public LiveData<NotificationsList> getRequestNotifications(int page) {
        return null;
    }

    @Override
    public LiveData<ProfileInfo> getOthersProfileInfo(int userId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> getAllHiddenVideosList(int postId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> updateCategories(UpdateCategories categories) {
        return null;
    }

    @Override
    public LiveData<ResultObject> blockUnblockUser(int userId, int status) {
        return null;
    }

    @Override
    public LiveData<ResultObject> logout(String header) {
        return null;
    }

    @Override
    public LiveData<BlockedUsersList> getBlockedUsers(int page) {
        return null;
    }

    @Override
    public LiveData<ResultObject> removeProfilePic() {
        return null;
    }

    @Override
    public LiveData<ResultObject> reportUser(ReportUser reportuser) {
        return null;
    }

    @Override
    public LiveData<ResultObject> deactivateAccount(DeactivateAccountRequest deactivateAccountRequest) {
        return null;
    }

    @Override
    public LiveData<LikedUserPost> getLikedUsers(int postId, int page) {
        return null;
    }

    @Override
    public LiveData<ResultObject> resetUnreadNotification(int notificationType) {
        return null;
    }
}