package com.cncoding.teazer.data.viewmodel;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.local.database.DataAccessTasks;
import com.cncoding.teazer.data.local.database.TeazerDB;
import com.cncoding.teazer.data.model.application.ConfigBody;
import com.cncoding.teazer.data.model.application.ConfigDetails;
import com.cncoding.teazer.data.model.application.DeactivateTypes;
import com.cncoding.teazer.data.model.application.PushNotificationBody;
import com.cncoding.teazer.data.model.application.ReportTypes;
import com.cncoding.teazer.data.model.auth.InitiateLoginWithOtp;
import com.cncoding.teazer.data.model.auth.InitiateSignup;
import com.cncoding.teazer.data.model.auth.Login;
import com.cncoding.teazer.data.model.auth.ResetPasswordByOtp;
import com.cncoding.teazer.data.model.auth.ResetPasswordByPhoneNumber;
import com.cncoding.teazer.data.model.auth.SocialSignup;
import com.cncoding.teazer.data.model.auth.VerifyLoginWithOtp;
import com.cncoding.teazer.data.model.auth.VerifySignUp;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.base.Medias;
import com.cncoding.teazer.data.model.base.TaggedUser;
import com.cncoding.teazer.data.model.discover.LandingPostsV2;
import com.cncoding.teazer.data.model.discover.VideosList;
import com.cncoding.teazer.data.model.friends.CircleList;
import com.cncoding.teazer.data.model.friends.FollowersList;
import com.cncoding.teazer.data.model.friends.FollowingsList;
import com.cncoding.teazer.data.model.friends.ProfileInfo;
import com.cncoding.teazer.data.model.friends.UsersList;
import com.cncoding.teazer.data.model.post.LikedUserList;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.data.model.post.PostReaction;
import com.cncoding.teazer.data.model.post.PostUploadResult;
import com.cncoding.teazer.data.model.post.ReportPost;
import com.cncoding.teazer.data.model.post.TaggedUsersList;
import com.cncoding.teazer.data.model.post.UpdatePostRequest;
import com.cncoding.teazer.data.model.profile.DefaultCoverImageResponse;
import com.cncoding.teazer.data.model.react.GiphyReactionRequest;
import com.cncoding.teazer.data.model.react.HiddenReactionsList;
import com.cncoding.teazer.data.model.react.ReactionResponse;
import com.cncoding.teazer.data.model.react.ReactionsList;
import com.cncoding.teazer.data.model.react.ReportReaction;
import com.cncoding.teazer.data.model.updatemobilenumber.ChangeMobileNumber;
import com.cncoding.teazer.data.model.updatemobilenumber.UpdateMobileNumber;
import com.cncoding.teazer.data.model.user.BlockedUsersList;
import com.cncoding.teazer.data.model.user.DeactivateAccountRequest;
import com.cncoding.teazer.data.model.user.NotificationsList;
import com.cncoding.teazer.data.model.user.ProfileUpdateRequest;
import com.cncoding.teazer.data.model.user.ReportUser;
import com.cncoding.teazer.data.model.user.SetPasswordRequest;
import com.cncoding.teazer.data.model.user.UpdateCategories;
import com.cncoding.teazer.data.model.user.UpdatePasswordRequest;
import com.cncoding.teazer.data.model.user.UserProfile;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.data.remote.apicalls.application.ApplicationRepository;
import com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepository;
import com.cncoding.teazer.data.remote.apicalls.discover.DiscoverRepository;
import com.cncoding.teazer.data.remote.apicalls.friends.FriendsRepository;
import com.cncoding.teazer.data.remote.apicalls.post.PostsRepository;
import com.cncoding.teazer.data.remote.apicalls.react.ReactRepository;
import com.cncoding.teazer.data.remote.apicalls.user.UserRepository;
import com.cncoding.teazer.utilities.common.Annotations.AccountType;
import com.cncoding.teazer.utilities.common.Annotations.BlockUnblock;
import com.cncoding.teazer.utilities.common.Annotations.HideOrShow;
import com.cncoding.teazer.utilities.common.Annotations.LikeDislike;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MultipartBody;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

@SuppressWarnings("WeakerAccess")
public class BaseViewModel extends ViewModel {

    @Inject MediatorLiveData<ResultObject> resultObjectLiveData;
    @Inject MediatorLiveData<DefaultCoverImageResponse> defaultCoverImageLiveData;
    @Inject MediatorLiveData<ConfigDetails> configDetailsLiveData;
    @Inject MediatorLiveData<List<ReportTypes>> reportTypesListLiveData;
    @Inject MediatorLiveData<List<DeactivateTypes>> deactivateTypesListLiveData;
    @Inject MediatorLiveData<List<Category>> categoriesListLiveData;
    @Inject MediatorLiveData<LandingPostsV2> landingPostsLiveData;
    @Inject MediatorLiveData<PostList> postListLiveData;
    @Inject MediatorLiveData<PostList> mostPopularLiveData;
    @Inject MediatorLiveData<UsersList> usersListLiveData;
    @Inject MediatorLiveData<VideosList> videosLiveData;
    @Inject MediatorLiveData<CircleList> circleListLiveData;
    @Inject MediatorLiveData<FollowingsList> followingsListLiveData;
    @Inject MediatorLiveData<FollowersList> followersListLiveData;
    @Inject MediatorLiveData<ProfileInfo> profileInfoLiveData;
    @Inject MediatorLiveData<BlockedUsersList> blockedUsersListLiveData;
    @Inject MediatorLiveData<LikedUserList> likedUserListLiveData;
    @Inject MediatorLiveData<PostDetails> postDetailsLiveData;
    @Inject MediatorLiveData<PostUploadResult> postUploadResultLiveData;
    @Inject MediatorLiveData<TaggedUsersList> taggedUsersListLiveData;
    @Inject MediatorLiveData<ReactionResponse> reactionResponseLiveData;
    @Inject MediatorLiveData<ReactionsList> reactionsListLiveData;
    @Inject MediatorLiveData<HiddenReactionsList> hiddenReactionsLiveData;
    @Inject MediatorLiveData<UserProfile> userProfileLiveData;
    @Inject MediatorLiveData<NotificationsList> notificationsLiveData;

    @Inject ApplicationRepository applicationRepository;
    @Inject AuthenticationRepository authenticationRepository;
    @Inject DiscoverRepository discoverRepository;
    @Inject FriendsRepository friendsRepository;
    @Inject PostsRepository postsRepository;
    @Inject ReactRepository reactRepository;
    @Inject UserRepository userRepository;

    @Inject TeazerDB database;

    private Observer<ResultObject> resultObjectObserver = new Observer<ResultObject>() {
        @Override
        public void onChanged(@Nullable ResultObject resultObject) {
            resultObjectLiveData.setValue(resultObject);
        }
    };

    public BaseViewModel(MediatorLiveData<ResultObject> resultObjectLiveData, MediatorLiveData<DefaultCoverImageResponse> defaultCoverImageLiveData,
                         MediatorLiveData<ConfigDetails> configDetailsLiveData, MediatorLiveData<List<ReportTypes>> reportTypesListLiveData,
                         MediatorLiveData<List<DeactivateTypes>> deactivateTypesListLiveData,
                         MediatorLiveData<List<Category>> categoriesListLiveData, MediatorLiveData<LandingPostsV2> landingPostsLiveData,
                         MediatorLiveData<PostList> postListLiveData, MediatorLiveData<PostList> mostPopularLiveData,
                         MediatorLiveData<UsersList> usersListLiveData, MediatorLiveData<VideosList> videosLiveData,
                         MediatorLiveData<CircleList> circleListLiveData, MediatorLiveData<FollowingsList> followingsListLiveData,
                         MediatorLiveData<FollowersList> followersListLiveData, MediatorLiveData<ProfileInfo> profileInfoLiveData,
                         MediatorLiveData<BlockedUsersList> blockedUsersListLiveData, MediatorLiveData<LikedUserList> likedUserListLiveData,
                         MediatorLiveData<PostDetails> postDetailsLiveData, MediatorLiveData<PostUploadResult> postUploadResultLiveData,
                         MediatorLiveData<TaggedUsersList> taggedUsersListLiveData, MediatorLiveData<ReactionResponse> reactionResponseLiveData,
                         MediatorLiveData<ReactionsList> reactionsListLiveData, MediatorLiveData<HiddenReactionsList> hiddenReactionsLiveData,
                         MediatorLiveData<UserProfile> userProfileLiveData, MediatorLiveData<NotificationsList> notificationsLiveData,
                         ApplicationRepository applicationRepository, AuthenticationRepository authenticationRepository,
                         DiscoverRepository discoverRepository, FriendsRepository friendsRepository, PostsRepository postsRepository,
                         ReactRepository reactRepository, UserRepository userRepository, TeazerDB database) {
        this.resultObjectLiveData = resultObjectLiveData;
        this.defaultCoverImageLiveData = defaultCoverImageLiveData;
        this.configDetailsLiveData = configDetailsLiveData;
        this.reportTypesListLiveData = reportTypesListLiveData;
        this.deactivateTypesListLiveData = deactivateTypesListLiveData;
        this.categoriesListLiveData = categoriesListLiveData;
        this.landingPostsLiveData = landingPostsLiveData;
        this.postListLiveData = postListLiveData;
        this.mostPopularLiveData = mostPopularLiveData;
        this.usersListLiveData = usersListLiveData;
        this.videosLiveData = videosLiveData;
        this.circleListLiveData = circleListLiveData;
        this.followingsListLiveData = followingsListLiveData;
        this.followersListLiveData = followersListLiveData;
        this.profileInfoLiveData = profileInfoLiveData;
        this.blockedUsersListLiveData = blockedUsersListLiveData;
        this.likedUserListLiveData = likedUserListLiveData;
        this.postDetailsLiveData = postDetailsLiveData;
        this.postUploadResultLiveData = postUploadResultLiveData;
        this.taggedUsersListLiveData = taggedUsersListLiveData;
        this.reactionResponseLiveData = reactionResponseLiveData;
        this.reactionsListLiveData = reactionsListLiveData;
        this.hiddenReactionsLiveData = hiddenReactionsLiveData;
        this.userProfileLiveData = userProfileLiveData;
        this.notificationsLiveData = notificationsLiveData;
        this.applicationRepository = applicationRepository;
        this.authenticationRepository = authenticationRepository;
        this.discoverRepository = discoverRepository;
        this.friendsRepository = friendsRepository;
        this.postsRepository = postsRepository;
        this.reactRepository = reactRepository;
        this.userRepository = userRepository;
        this.database = database;
    }

    //region Application API calls
    public void sendPushNotificationToAllApiCall(PushNotificationBody pushNotificationBody){
        resultObjectLiveData.addSource(
                applicationRepository.sendPushNotificationToAll(pushNotificationBody),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        resultObjectLiveData.setValue(resultObject);
                    }
                }
        );
    }

    public void getDefaultCoverImagesApiCall(int page){
        defaultCoverImageLiveData.addSource(
                applicationRepository.getDefaultCoverImages(page),
                new Observer<DefaultCoverImageResponse>() {
                    @Override
                    public void onChanged(@Nullable DefaultCoverImageResponse defaultCoverImageResponse) {
                        defaultCoverImageLiveData.setValue(defaultCoverImageResponse);
                    }
                }
        );
    }

//    public void addDefaultCoverMediaApiCall(DefaultCoverMedia defaultCoverMedia){}

    public void getUpdateAndConfigDetailsApiCall(ConfigBody configBody){
        configDetailsLiveData.addSource(
                applicationRepository.getUpdateAndConfigDetails(configBody),
                new Observer<ConfigDetails>() {
                    @Override
                    public void onChanged(@Nullable ConfigDetails configDetails) {
                        configDetailsLiveData.setValue(configDetails);
                    }
                }
        );
    }

    public void getPostReportTypesApiCall(){
        reportTypesListLiveData.addSource(
                applicationRepository.getPostReportTypes(),
                new Observer<List<ReportTypes>>() {
                    @Override
                    public void onChanged(@Nullable List<ReportTypes> reportTypes) {
                        reportTypesListLiveData.setValue(reportTypes);
                    }
                }
        );
    }

    public void getReactionReportTypesApiCall(){
        reportTypesListLiveData.addSource(
                applicationRepository.getReactionReportTypes(),
                new Observer<List<ReportTypes>>() {
                    @Override
                    public void onChanged(@Nullable List<ReportTypes> reportTypes) {
                        reportTypesListLiveData.setValue(reportTypes);
                    }
                }
        );
    }

    public void getProfileReportTypesApiCall(){
        reportTypesListLiveData.addSource(
                applicationRepository.getReactionReportTypes(),
                new Observer<List<ReportTypes>>() {
                    @Override
                    public void onChanged(@Nullable List<ReportTypes> reportTypes) {
                        reportTypesListLiveData.setValue(reportTypes);
                    }
                }
        );
    }

    public void getDeactivationTypesListApiCall(){
        deactivateTypesListLiveData.addSource(
                applicationRepository.getDeactivationTypesList(),
                new Observer<List<DeactivateTypes>>() {
                    @Override
                    public void onChanged(@Nullable List<DeactivateTypes> deactivateTypes) {
                        deactivateTypesListLiveData.setValue(deactivateTypes);
                    }
                }
        );
    }

    public void getCategoriesApiCall(){
        categoriesListLiveData.addSource(
                applicationRepository.getCategories(),
                new Observer<List<Category>>() {
                    @Override
                    public void onChanged(@Nullable List<Category> categories) {
                        categoriesListLiveData.setValue(categories);
                    }
                }
        );
    }
    //endregion

    //region Authentication API calls
    private void clearLiveDataResponse() {
        if (resultObjectLiveData.getValue() != null) {
            resultObjectLiveData.getValue().clearData();
        }
    }

    public void signUpApiCall(InitiateSignup initiateSignup) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.signUp(initiateSignup), resultObjectObserver);
    }

    public void verifySignUpApiCall(VerifySignUp verifySignUp) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.verifySignUp(verifySignUp), resultObjectObserver);
    }

    public void socialSignUpApiCall(SocialSignup socialSignup) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.socialSignUp(socialSignup), resultObjectObserver);
    }

    public void loginWithPasswordApiCall(Login login) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.loginWithPassword(login), resultObjectObserver);
    }

    public void loginWithOtpApiCall(InitiateLoginWithOtp initiateLoginWithOtp) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.loginWithOtp(initiateLoginWithOtp), resultObjectObserver);
    }

    public void verifyLoginWithOtpApiCall(VerifyLoginWithOtp verifyLoginWithOtp) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.verifyLoginWithOtp(verifyLoginWithOtp), resultObjectObserver);
    }

    public void checkUsernameAvailabilityApiCall(String username) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.checkUsernameAvailability(username), resultObjectObserver);
    }

    public void checkEmailAvailabilityApiCall(String email) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.checkEmailAvailability(email), resultObjectObserver);
    }

    public void checkPhoneNumberAvailabilityApiCall(int countryCode, long phoneNumber) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.checkPhoneNumberAvailability(countryCode, phoneNumber), resultObjectObserver);
    }

    public void verifyForgotPasswordOtpApiCall(int otp) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.verifyForgotPasswordOtp(otp), resultObjectObserver);
    }

    public void requestResetPasswordByEmailApiCall(String email) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.requestResetPasswordByEmail(email), resultObjectObserver);
    }

    public void requestResetPasswordByPhoneApiCall(ResetPasswordByPhoneNumber resetPasswordByPhoneNumber) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.requestResetPasswordByPhone(resetPasswordByPhoneNumber), resultObjectObserver);
    }

    public void resetPasswordByOtpApiCall(ResetPasswordByOtp resetPasswordByOtp) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.resetPasswordByOtp(resetPasswordByOtp), resultObjectObserver);
    }
    //endregion

    //region Discover API Calls
    public void loadLandingPostsApiCall() {
        try {
            landingPostsLiveData.addSource(
                    discoverRepository.getNewDiscoverLandingPosts(),
                    new Observer<LandingPostsV2>() {
                        @Override
                        public void onChanged(@Nullable LandingPostsV2 landingPostsV2) {
                            landingPostsLiveData.setValue(landingPostsV2);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMostPopularPostsApiCall(final int page) {
        try {
            mostPopularLiveData.addSource(
                    discoverRepository.getAllMostPopularVideos(page),
                    new Observer<PostList>() {
                        @Override
                        public void onChanged(@Nullable PostList postList) {
                            if (page > 1 && postList != null && mostPopularLiveData.getValue() != null) {
                                List<PostDetails> postDetailsList = mostPopularLiveData.getValue().getPosts();
                                postDetailsList.addAll(postList.getPosts());
                                postList.setPosts(postDetailsList);
                            }
                            mostPopularLiveData.setValue(postList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFeaturedPostsApiCall(int page) {
        try {
            postListLiveData.addSource(
                    discoverRepository.getFeaturedPosts(page),
                    new Observer<PostList>() {
                        @Override
                        public void onChanged(@Nullable PostList postList) {
                            postListLiveData.setValue(postList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadAllInterestedCategoriesPostsApiCall(int page, int categoryId) {
        try {
            postListLiveData.addSource(
                    discoverRepository.getAllInterestedCategoriesVideos(page, categoryId),
                    new Observer<PostList>() {
                        @Override
                        public void onChanged(@Nullable PostList postList) {
                            postListLiveData.setValue(postList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTrendingPostsByCategoryApiCall(int page, int categoryId) {
        try {
            postListLiveData.addSource(
                    discoverRepository.getTrendingVideosByCategory(page, categoryId),
                    new Observer<PostList>() {
                        @Override
                        public void onChanged(@Nullable PostList postList) {
                            postListLiveData.setValue(postList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadUsersListApiCall(int page) {
        try {
            usersListLiveData.addSource(
                    discoverRepository.getUsersListToFollow(page),
                    new Observer<UsersList>() {
                        @Override
                        public void onChanged(@Nullable UsersList usersList) {
                            usersListLiveData.setValue(usersList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadUsersListWithSearchTermApiCall(int page, String searchTerm) {
        try {
            usersListLiveData.addSource(
                    discoverRepository.getUsersListToFollowWithSearchTerm(page, searchTerm),
                    new Observer<UsersList>() {
                        @Override
                        public void onChanged(@Nullable UsersList usersList) {
                            usersListLiveData.setValue(usersList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTrendingVideosApiCall(int page) {
        try {
            videosLiveData.addSource(
                    discoverRepository.getTrendingVideos(page),
                    new Observer<VideosList>() {
                        @Override
                        public void onChanged(@Nullable VideosList videosList) {
                            videosLiveData.setValue(videosList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadVideosWithSearchTermApiCall(int page, String searchTerm) {
        try {
            videosLiveData.addSource(
                    discoverRepository.getVideosWithSearchTerm(page, searchTerm),
                    new Observer<VideosList>() {
                        @Override
                        public void onChanged(@Nullable VideosList videosList) {
                            videosLiveData.setValue(videosList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Friends API Calls
    public void sendJoinRequestByUserIdApiCall(int userId, final int position) {
        resultObjectLiveData.addSource(
                friendsRepository.sendJoinRequestByUserId(userId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        if (resultObject != null) {
                            resultObjectLiveData.setValue(position  > -1 ? resultObject.setAdapterPosition(position) : resultObject);
                        }
                    }
                }
        );
    }

    public void sendJoinRequestByUserIdApiCall(int userId) {
        resultObjectLiveData.addSource(
                friendsRepository.sendJoinRequestByUserId(userId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        if (resultObject != null) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
                }
        );
    }

    public void sendJoinRequestByUsernameApiCall(String username) {
        resultObjectLiveData.addSource(
                friendsRepository.sendJoinRequestByUsername(username),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        resultObjectLiveData.setValue(resultObject);
                    }
                }
        );
    }

    public void acceptJoinRequestApiCall(int notificationId, final int position) {
        resultObjectLiveData.addSource(
                friendsRepository.acceptJoinRequest(notificationId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        if (resultObject != null) {
                            resultObjectLiveData.setValue(position  > -1 ? resultObject.setAdapterPosition(position) : resultObject);
                        }
                    }
                }
        );
    }

    public void acceptJoinRequestApiCall(int notificationId) {
        resultObjectLiveData.addSource(
                friendsRepository.acceptJoinRequest(notificationId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        resultObjectLiveData.setValue(resultObject);
                    }
                }
        );
    }

    public void deleteJoinRequestApiCall(int notificationId) {
        resultObjectLiveData.addSource(
                friendsRepository.deleteJoinRequest(notificationId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        resultObjectLiveData.setValue(resultObject);
                    }
                }
        );
    }

    public void getMyCircleApiCall(int page) {
        if (circleListLiveData != null) {
            circleListLiveData.addSource(
                    friendsRepository.getMyCircle(page),
                    new Observer<CircleList>() {
                        @Override
                        public void onChanged(@Nullable CircleList circleList) {
                            circleListLiveData.setValue(circleList);
                        }
                    }
            );
        }
    }

    public void getMyCircleWithSearchTermApiCall(int page, String searchTerm) {
        if (circleListLiveData != null) {
            circleListLiveData.addSource(
                    friendsRepository.getMyCircleWithSearchTerm(page, searchTerm),
                    new Observer<CircleList>() {
                        @Override
                        public void onChanged(@Nullable CircleList circleList) {
                            circleListLiveData.setValue(circleList);
                        }
                    }
            );
        }
    }

    public void getMyFollowingApiCall(int page) {
        if (followingsListLiveData != null) {
            followingsListLiveData.addSource(
                    friendsRepository.getMyFollowing(page),
                    new Observer<FollowingsList>() {
                        @Override
                        public void onChanged(@Nullable FollowingsList followingsList) {
                            followingsListLiveData.setValue(followingsList);
                        }
                    }
            );
        }
    }

    public void getMyFollowingsWithSearchTermApiCall(int page, String searchTerm) {
        if (followingsListLiveData != null) {
            followingsListLiveData.addSource(
                    friendsRepository.getMyFollowingsWithSearchTerm(page, searchTerm),
                    new Observer<FollowingsList>() {
                        @Override
                        public void onChanged(@Nullable FollowingsList followingsList) {
                            followingsListLiveData.setValue(followingsList);
                        }
                    }
            );
        }
    }

    public void getFriendsFollowingsApiCall(int page, int userId) {
        if (followingsListLiveData != null) {
            followingsListLiveData.addSource(
                    friendsRepository.getFriendsFollowings(page, userId),
                    new Observer<FollowingsList>() {
                        @Override
                        public void onChanged(@Nullable FollowingsList followingsList) {
                            followingsListLiveData.setValue(followingsList);
                        }
                    }
            );
        }
    }

    public void getFriendsFollowingsWithSearchTermApiCall(int userId, int page, String searchTerm) {
        if (followingsListLiveData != null) {
            followingsListLiveData.addSource(
                    friendsRepository.getFriendsFollowingsWithSearchTerm(page, userId, searchTerm),
                    new Observer<FollowingsList>() {
                        @Override
                        public void onChanged(@Nullable FollowingsList followingsList) {
                            followingsListLiveData.setValue(followingsList);
                        }
                    }
            );
        }
    }

    public void getMyFollowersApiCall(int page) {
        if (followersListLiveData != null) {
            followersListLiveData.addSource(
                    friendsRepository.getMyFollowers(page),
                    new Observer<FollowersList>() {
                        @Override
                        public void onChanged(@Nullable FollowersList followersList) {
                            followersListLiveData.setValue(followersList);
                        }
                    }
            );
        }
    }

    public void getMyFollowersWithSearchTermApiCall(int page, String searchTerm) {
        if (followersListLiveData != null) {
            followersListLiveData.addSource(
                    friendsRepository.getMyFollowersWithSearchTerm(page, searchTerm),
                    new Observer<FollowersList>() {
                        @Override
                        public void onChanged(@Nullable FollowersList followersList) {
                            followersListLiveData.setValue(followersList);
                        }
                    }
            );
        }
    }

    public void getFriendsFollowersApiCall(int page, int userId) {
        if (followersListLiveData != null) {
            followersListLiveData.addSource(
                    friendsRepository.getFriendsFollowers(page, userId),
                    new Observer<FollowersList>() {
                        @Override
                        public void onChanged(@Nullable FollowersList followersList) {
                            followersListLiveData.setValue(followersList);
                        }
                    }
            );
        }
    }

    public void getFriendsFollowersWithSearchTermApiCall(int userId, int page, String searchTerm) {
        if (followersListLiveData != null) {
            followersListLiveData.addSource(
                    friendsRepository.getFriendsFollowersWithSearchTerm(page, userId, searchTerm),
                    new Observer<FollowersList>() {
                        @Override
                        public void onChanged(@Nullable FollowersList followersList) {
                            followersListLiveData.setValue(followersList);
                        }
                    }
            );
        }
    }

    public void unfollowUserApiCall(int userId, final int position) {
        resultObjectLiveData.addSource(
                friendsRepository.unfollowUser(userId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        if (resultObject != null) {
                            resultObjectLiveData.setValue(position  > -1 ? resultObject.setAdapterPosition(position) : resultObject);
                        }
                    }
                }
        );
    }

    public void unfollowUserApiCall(int userId) {
        resultObjectLiveData.addSource(
                friendsRepository.unfollowUser(userId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        resultObjectLiveData.setValue(resultObject);
                    }
                }
        );
    }

    public void cancelRequestApiCall(int userId, final int position) {
        resultObjectLiveData.addSource(
                friendsRepository.cancelRequest(userId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        if (resultObject != null) {
                            resultObjectLiveData.setValue(position  > -1 ? resultObject.setAdapterPosition(position) : resultObject);
                        }
                    }
                }
        );
    }

    public void cancelRequestApiCall(int userId) {
        resultObjectLiveData.addSource(
                friendsRepository.cancelRequest(userId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        resultObjectLiveData.setValue(resultObject);
                    }
                }
        );
    }

    public void followUserApiCall(int userId) {
        resultObjectLiveData.addSource(
                friendsRepository.followUser(userId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        resultObjectLiveData.setValue(resultObject);
                    }
                }
        );
    }

    public void getOthersProfileInfoApiCall(int userId) {
        if (profileInfoLiveData != null) {
            profileInfoLiveData.addSource(
                    friendsRepository.getOthersProfileInfo(userId),
                    new Observer<ProfileInfo>() {
                        @Override
                        public void onChanged(@Nullable ProfileInfo profileInfo) {
                            profileInfoLiveData.setValue(profileInfo);
                        }
                    }
            );
        }
    }

    public void blockUnblockUserApiCall(int userId, @BlockUnblock int status, final int position) {
        resultObjectLiveData.addSource(
                friendsRepository.blockUnblockUser(userId, status),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        if (resultObject != null) {
                            resultObjectLiveData.setValue(position  > -1 ? resultObject.setAdapterPosition(position) : resultObject);
                        }
                    }
                }
        );
    }

    public void blockUnblockUserApiCall(int userId, @BlockUnblock int status) {
        resultObjectLiveData.addSource(
                friendsRepository.blockUnblockUser(userId, status),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        resultObjectLiveData.setValue(resultObject);
                    }
                }
        );
    }

    public void getBlockedUsersApiCall(int page) {
        if (blockedUsersListLiveData != null) {
            blockedUsersListLiveData.addSource(
                    friendsRepository.getBlockedUsers(page),
                    new Observer<BlockedUsersList>() {
                        @Override
                        public void onChanged(@Nullable BlockedUsersList blockedUsersList) {
                            blockedUsersListLiveData.setValue(blockedUsersList);
                        }
                    }
            );
        }
    }

    public void getUsersListToFollowApiCall(int page) {
        if (usersListLiveData != null) {
            usersListLiveData.addSource(
                    friendsRepository.getUsersListToFollow(page),
                    new Observer<UsersList>() {
                        @Override
                        public void onChanged(@Nullable UsersList usersList) {
                            usersListLiveData.setValue(usersList);
                        }
                    }
            );
        }
    }

    public void getUsersListToFollowWithSearchTermApiCall(int page, String searchTerm) {
        if (usersListLiveData != null) {
            usersListLiveData.addSource(
                    friendsRepository.getUsersListToFollowWithSearchTerm(page, searchTerm),
                    new Observer<UsersList>() {
                        @Override
                        public void onChanged(@Nullable UsersList usersList) {
                            usersListLiveData.setValue(usersList);
                        }
                    }
            );
        }
    }

    public void getLikedUsersApiCall(int postId, int page) {
        if (likedUserListLiveData != null) {
            likedUserListLiveData.addSource(
                    friendsRepository.getLikedUsers(postId, page),
                    new Observer<LikedUserList>() {
                        @Override
                        public void onChanged(@Nullable LikedUserList likedUserList) {
                            likedUserListLiveData.setValue(likedUserList);
                        }
                    }
            );
        }
    }
    //endregion

    //region PostList related stuff (Local and Remote).
    /**
     * API call to fetch post list.
     */
    public void loadPostListApiCall(final int page) {
        try {
            postListLiveData.addSource(
                    postsRepository.getHomePagePosts(page),
                    new Observer<PostList>() {
                        @Override
                        public void onChanged(@Nullable PostList postList) {
                            postListLiveData.setValue(postList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TeazerDB getDatabase() {
        return database;
    }

    public boolean getAllLocalPosts() {
        try {
            List<PostDetails> postDetailsList = database.dao().getAllPosts();
            boolean result = postDetailsList != null && !postDetailsList.isEmpty();
            if (result) postListLiveData.setValue(new PostList(postDetailsList));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public PostDetails getLocalPost(int postId) {
        try {
            return database.dao().getPost(postId).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertAllPostsToDB(List<PostDetails> postDetails) {
        new DataAccessTasks.InsertAllTask(database, postDetails).execute();
    }

    public void updateLocalPost(PostDetails postDetails) {
        new DataAccessTasks.UpdatePostTask(database, postDetails).execute();
    }

    public void likeLocalPost(int postId) {
        new DataAccessTasks.PostUpdateTaskWithId(TeazerDB.LIKE, database).execute(postId);
    }

    public void dislikeLocalPost(int postId) {
        new DataAccessTasks.PostUpdateTaskWithId(TeazerDB.DISLIKE, database).execute(postId);
    }

    public void deleteLocalPost(int postId) {
        new DataAccessTasks.PostUpdateTaskWithId(TeazerDB.DELETE, database).execute(postId);
    }

    public void deleteLocalPost(PostDetails postDetails) {
        new DataAccessTasks.PostUpdateTaskWithObject(TeazerDB.DELETE, database).execute(postDetails);
    }

    public void deleteAllLocalPosts() {
        new DataAccessTasks.DeleteAllTask(database).execute();
    }

    public void updateLocalTitle(String title, int postId) {
        new DataAccessTasks.UpdateTitleTask(database, title, postId).execute();
    }

    public void updateLocalTags(ArrayList<TaggedUser> taggedUsers, int postId) {
        new DataAccessTasks.UpdateTagsReactionsTask(database, taggedUsers, null, false, TeazerDB.TAGS, postId).execute();
    }

    public void updateLocalReactions(ArrayList<PostReaction> reactions, boolean canReact, int postId) {
        new DataAccessTasks.UpdateTagsReactionsTask(database, null, reactions, true, TeazerDB.TAGS, postId).execute();
    }

    public void incrementLocalViews(ArrayList<Medias> medias, int postId) {
        new DataAccessTasks.IncrementViewsTask(database, postId).execute(medias);
    }
    //endregion

    //region Other "Post" API calls
    public void uploadVideoApiCall(MultipartBody.Part video, String title, String location,
                            double latitude, double longitude, String tags, String categories) {
        try {
            postUploadResultLiveData.addSource(
                    postsRepository.uploadVideo(video, title, location, latitude, longitude, tags, categories),
                    new Observer<PostUploadResult>() {
                        @Override
                        public void onChanged(@Nullable PostUploadResult postUploadResult) {
                            postUploadResultLiveData.setValue(postUploadResult);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createReactionByGiphyApiCall(GiphyReactionRequest giphyReactionRequest) {
        try {
            reactionResponseLiveData.addSource(
                    reactRepository.createReactionByGiphy(giphyReactionRequest),
                    new Observer<ReactionResponse>() {
                        @Override
                        public void onChanged(@Nullable ReactionResponse reactionResponse) {
                            reactionResponseLiveData.setValue(reactionResponse);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePostApiCall(UpdatePostRequest updatePostRequest) {
        try {
            postUploadResultLiveData.addSource(
                    postsRepository.updatePost(updatePostRequest),
                    new Observer<PostUploadResult>() {
                        @Override
                        public void onChanged(@Nullable PostUploadResult postUploadResult) {
                            postUploadResultLiveData.setValue(postUploadResult);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void likeDislikePostApiCall(int postId, @LikeDislike int status) {
        try {
            resultObjectLiveData.addSource(
                    postsRepository.likeDislikePost(postId, status),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void incrementViewCountApiCall(int mediaId) {
        try {
            resultObjectLiveData.addSource(
                    postsRepository.incrementViewCount(mediaId),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletePostApiCall(int postId) {
        try {
            resultObjectLiveData.addSource(
                    postsRepository.deletePost(postId),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reportPostApiCall(ReportPost reportPostDetails) {
        try {
            resultObjectLiveData.addSource(
                    postsRepository.reportPost(reportPostDetails),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideOrShowPostApiCall(int postId, @HideOrShow int status) {
        try {
            resultObjectLiveData.addSource(
                    postsRepository.hideOrShowPost(postId, status),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPostDetailsApiCall(int postId) {
        try {
            postDetailsLiveData.addSource(
                    postsRepository.getPostDetails(postId),
                    new Observer<PostDetails>() {
                        @Override
                        public void onChanged(@Nullable PostDetails postDetails) {
                            postDetailsLiveData.setValue(postDetails);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTaggedUsersApiCall(int postId, int page) {
        try {
            taggedUsersListLiveData.addSource(
                    postsRepository.getTaggedUsers(postId, page),
                    new Observer<TaggedUsersList>() {
                        @Override
                        public void onChanged(@Nullable TaggedUsersList taggedUsersList) {
                            taggedUsersListLiveData.setValue(taggedUsersList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadHiddenPostsApiCall(int page) {
        try {
            postListLiveData.addSource(
                    postsRepository.getHiddenPosts(page),
                    new Observer<PostList>() {
                        @Override
                        public void onChanged(@Nullable PostList postList) {
                            postListLiveData.setValue(postList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMyPostedVideosApiCall(int page) {
        try {
            postListLiveData.addSource(
                    postsRepository.getMyPostedVideos(page),
                    new Observer<PostList>() {
                        @Override
                        public void onChanged(@Nullable PostList postList) {
                            postListLiveData.setValue(postList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadVideosPostedByFriendApiCall(int page, int friendId) {
        try {
            postListLiveData.addSource(
                    postsRepository.getVideosPostedByFriend(page, friendId),
                    new Observer<PostList>() {
                        @Override
                        public void onChanged(@Nullable PostList postList) {
                            postListLiveData.setValue(postList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadReactionsOfPostApiCall(int postId, int page) {
        try {
            reactionsListLiveData.addSource(
                    postsRepository.getReactionsOfPost(postId, page),
                    new Observer<ReactionsList>() {
                        @Override
                        public void onChanged(@Nullable ReactionsList reactionsList) {
                            reactionsListLiveData.setValue(reactionsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadHiddenVideosListApiCall(int page) {
        try {
            postListLiveData.addSource(
                    postsRepository.getHiddenVideosList(page),
                    new Observer<PostList>() {
                        @Override
                        public void onChanged(@Nullable PostList postList) {
                            postListLiveData.setValue(postList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadAllHiddenVideosListApiCall(int postId) {
        try {
            resultObjectLiveData.addSource(
                    postsRepository.getAllHiddenVideosList(postId),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region React API Calls
    public void uploadReactionApiCall(MultipartBody.Part video, int postId, String title){
        try {
            reactionResponseLiveData.addSource(
                    reactRepository.uploadReaction(video, postId, title),
                    new Observer<ReactionResponse>() {
                        @Override
                        public void onChanged(@Nullable ReactionResponse reactionResponse) {
                            reactionResponseLiveData.setValue(reactionResponse);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getReactionDetailApiCall(int reactId){
        try {
            reactionResponseLiveData.addSource(
                    reactRepository.getReactionDetail(reactId),
                    new Observer<ReactionResponse>() {
                        @Override
                        public void onChanged(@Nullable ReactionResponse reactionResponse) {
                            reactionResponseLiveData.setValue(reactionResponse);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void likeDislikeReactionApiCall(int reactId, @LikeDislike int status){
        try {
            resultObjectLiveData.addSource(
                    reactRepository.likeDislikeReaction(reactId, status),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void incrementReactionViewCountApiCall(int mediaId){
        try {
            resultObjectLiveData.addSource(
                    reactRepository.incrementReactionViewCount(mediaId),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteReactionApiCall(int reactId){
        try {
            resultObjectLiveData.addSource(
                    reactRepository.deleteReaction(reactId),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reportReactionApiCall(ReportReaction reportReaction){
        try {
            resultObjectLiveData.addSource(
                    reactRepository.reportReaction(reportReaction),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideOrShowReactionApiCall(int reactId, @HideOrShow int status){
        try {
            resultObjectLiveData.addSource(
                    reactRepository.hideOrShowReaction(reactId, status),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMyReactionsApiCall(int page){
        try {
            reactionsListLiveData.addSource(
                    reactRepository.getMyReactions(page),
                    new Observer<ReactionsList>() {
                        @Override
                        public void onChanged(@Nullable ReactionsList reactionsList) {
                            reactionsListLiveData.setValue(reactionsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFriendsReactionsApiCall(int page, int friend_id){
        try {
            reactionsListLiveData.addSource(
                    reactRepository.getFriendsReactions(page, friend_id),
                    new Observer<ReactionsList>() {
                        @Override
                        public void onChanged(@Nullable ReactionsList reactionsList) {
                            reactionsListLiveData.setValue(reactionsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getHiddenReactionsApiCall(int page){
        try {
            hiddenReactionsLiveData.addSource(
                    reactRepository.getHiddenReactions(page),
                    new Observer<HiddenReactionsList>() {
                        @Override
                        public void onChanged(@Nullable HiddenReactionsList hiddenReactionsList) {
                            hiddenReactionsLiveData.setValue(hiddenReactionsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated @SuppressWarnings("deprecation") public void getOldLikedUsersOfReactionApiCall(int reactId, int page){
        try {
            likedUserListLiveData.addSource(
                    reactRepository.getOldLikedUsersOfReaction(reactId, page),
                    new Observer<LikedUserList>() {
                        @Override
                        public void onChanged(@Nullable LikedUserList hiddenReactionsList) {
                            likedUserListLiveData.setValue(hiddenReactionsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated @SuppressWarnings("deprecation")
    public void getOldLikedUsersOfReactionWithSearchTermApiCall(int reactId, int page, String searchTerm){
        try {
            likedUserListLiveData.addSource(
                    reactRepository.getOldLikedUsersOfReactionWithSearchTerm(reactId, page, searchTerm),
                    new Observer<LikedUserList>() {
                        @Override
                        public void onChanged(@Nullable LikedUserList hiddenReactionsList) {
                            likedUserListLiveData.setValue(hiddenReactionsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLikedUsersOfReactionApiCall(int reactId, int page){
        try {
            likedUserListLiveData.addSource(
                    reactRepository.getLikedUsersOfReaction(reactId, page),
                    new Observer<LikedUserList>() {
                        @Override
                        public void onChanged(@Nullable LikedUserList hiddenReactionsList) {
                            likedUserListLiveData.setValue(hiddenReactionsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLikedUsersOfReactionWithSearchTermApiCall(int reactId, int page, String searchTerm){
        try {
            likedUserListLiveData.addSource(
                    reactRepository.getLikedUsersOfReactionWithSearchTerm(reactId, page, searchTerm),
                    new Observer<LikedUserList>() {
                        @Override
                        public void onChanged(@Nullable LikedUserList hiddenReactionsList) {
                            likedUserListLiveData.setValue(hiddenReactionsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region User API Calls
    public void updateUserProfileMediaApiCall(MultipartBody.Part media){
        try {
            resultObjectLiveData.addSource(
                    userRepository.updateUserProfileMedia(media),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetFcmTokenApiCall(String header, String token){
        try {
            resultObjectLiveData.addSource(
                    userRepository.resetFcmToken(header, token),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAccountVisibilityApiCall(@AccountType int accountType){
        try {
            resultObjectLiveData.addSource(
                    userRepository.setAccountVisibility(accountType),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getUserProfileApiCall(){
        try {
            userProfileLiveData.addSource(
                    userRepository.getUserProfile(),
                    new Observer<UserProfile>() {
                        @Override
                        public void onChanged(@Nullable UserProfile userProfile) {
                            userProfileLiveData.setValue(userProfile);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeMobileNumberApiCall(ChangeMobileNumber changeMobileNumber){
        try {
            resultObjectLiveData.addSource(
                    userRepository.changeMobileNumber(changeMobileNumber),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateMobileNumberApiCall(UpdateMobileNumber updateMobileNumber){
        try {
            resultObjectLiveData.addSource(
                    userRepository.updateMobileNumber(updateMobileNumber),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUserProfileApiCall(ProfileUpdateRequest updateProfileDetails){
        try {
            resultObjectLiveData.addSource(
                    userRepository.updateUserProfile(updateProfileDetails),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePasswordApiCall(UpdatePasswordRequest updatePasswordDetails){
        try {
            resultObjectLiveData.addSource(
                    userRepository.updatePassword(updatePasswordDetails),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPasswordApiCall(SetPasswordRequest setPasswordDetails){
        try {
            resultObjectLiveData.addSource(
                    userRepository.setPassword(setPasswordDetails),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFollowingNotificationsApiCall(int page){
        try {
            notificationsLiveData.addSource(
                    userRepository.getFollowingNotifications(page),
                    new Observer<NotificationsList>() {
                        @Override
                        public void onChanged(@Nullable NotificationsList notificationsList) {
                            notificationsLiveData.setValue(notificationsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getRequestNotificationsApiCall(int page){
        try {
            notificationsLiveData.addSource(
                    userRepository.getRequestNotifications(page),
                    new Observer<NotificationsList>() {
                        @Override
                        public void onChanged(@Nullable NotificationsList notificationsList) {
                            notificationsLiveData.setValue(notificationsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCategoriesApiCall(UpdateCategories categories){
        try {
            resultObjectLiveData.addSource(
                    userRepository.updateCategories(categories),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logoutApiCall(String header){
        try {
            resultObjectLiveData.addSource(
                    userRepository.logout(header),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeProfilePicApiCall(){
        try {
            resultObjectLiveData.addSource(
                    userRepository.removeProfilePic(),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reportUserApiCall(ReportUser reportuser){
        try {
            resultObjectLiveData.addSource(
                    userRepository.reportUser(reportuser),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deactivateAccountApiCall(DeactivateAccountRequest deactivateAccountRequest){
        try {
            resultObjectLiveData.addSource(
                    userRepository.deactivateAccount(deactivateAccountRequest),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetUnreadNotificationApiCall(int notificationType){
        try {
            resultObjectLiveData.addSource(
                    userRepository.resetUnreadNotification(notificationType),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void likeDislikeProfileApiCall(int userId, @LikeDislike int status){
        try {
            resultObjectLiveData.addSource(
                    userRepository.likeDislikeProfile(userId, status),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLikedUsersListApiCall(int userId){
        try {
            likedUserListLiveData.addSource(
                    userRepository.getLikedUsersList(userId),
                    new Observer<LikedUserList>() {
                        @Override
                        public void onChanged(@Nullable LikedUserList likedUserList) {
                            likedUserListLiveData.setValue(likedUserList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion
}