package com.cncoding.teazer.data.viewmodel;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.remote.apicalls.discover.DiscoverRepository;
import com.cncoding.teazer.data.remote.apicalls.discover.DiscoverRepositoryImpl;
import com.cncoding.teazer.model.discover.LandingPostsV2;
import com.cncoding.teazer.model.discover.VideosList;
import com.cncoding.teazer.model.friends.UsersList;
import com.cncoding.teazer.model.post.PostList;

import javax.inject.Inject;

/**
 *
 * Created by Prem$ on 2/20/2018.
 */

public class DiscoverViewModel extends ViewModel {

    private MediatorLiveData<LandingPostsV2> landingPostsLiveData;
    private MediatorLiveData<PostList> postListLiveData;
    private MediatorLiveData<PostList> mostPopularLiveData;
//    private MediatorLiveData<PostList> featuredPostsLiveData;
//    private MediatorLiveData<PostList> interestedCategoriesPostsLiveData;
//    private MediatorLiveData<PostList> trendingPostsLiveData;
    private MediatorLiveData<UsersList> usersLiveData;
    private MediatorLiveData<VideosList> videosLiveData;
    private DiscoverRepository apiRepository;

//    @Inject public DiscoverViewModel(@NonNull Application application, MediatorLiveData<LandingPostsV2> landingPostsLiveData,
//                             MediatorLiveData<PostList> mostPopularLiveData, MediatorLiveData<PostList> featuredPostsLiveData,
//                             MediatorLiveData<PostList> interestedCategoriesPostsLiveData, MediatorLiveData<PostList> trendingPostsLiveData,
//                             MediatorLiveData<UsersList> usersLiveData, MediatorLiveData<VideosList> videosLiveData,
//                             DiscoverRepository apiRepository) {
//        super(application);
//        this.landingPostsLiveData = landingPostsLiveData;
//        this.mostPopularLiveData = mostPopularLiveData;
//        this.featuredPostsLiveData = featuredPostsLiveData;
//        this.interestedCategoriesPostsLiveData = interestedCategoriesPostsLiveData;
//        this.trendingPostsLiveData = trendingPostsLiveData;
//        this.usersLiveData = usersLiveData;
//        this.videosLiveData = videosLiveData;
//        this.apiRepository = apiRepository;
//    }

    @Inject DiscoverViewModel(MediatorLiveData<LandingPostsV2> landingPostsLiveData, MediatorLiveData<PostList> postListLiveData,
                      MediatorLiveData<PostList> mostPopularLiveData, MediatorLiveData<UsersList> usersLiveData,
                      MediatorLiveData<VideosList> videosLiveData, DiscoverRepository apiRepository) {
        this.landingPostsLiveData = landingPostsLiveData;
        this.postListLiveData = postListLiveData;
        this.mostPopularLiveData = mostPopularLiveData;
        this.usersLiveData = usersLiveData;
        this.videosLiveData = videosLiveData;
        this.apiRepository = apiRepository;
    }

    public DiscoverViewModel(String token) {
        landingPostsLiveData = new MediatorLiveData<>();
        postListLiveData = new MediatorLiveData<>();
        mostPopularLiveData = new MediatorLiveData<>();
        usersLiveData = new MediatorLiveData<>();
        videosLiveData = new MediatorLiveData<>();
        apiRepository = new DiscoverRepositoryImpl(token);
    }

    //region Getters
    public MediatorLiveData<LandingPostsV2> getLandingPosts() {
        return landingPostsLiveData;
    }

    public MediatorLiveData<PostList> getPostListLiveData() {
        return postListLiveData;
    }

    public MediatorLiveData<PostList> getMostPopularPosts() {
        return mostPopularLiveData;
    }

//    public MediatorLiveData<PostList> getFeaturedPosts() {
//        return featuredPostsLiveData;
//    }
//
//    public MediatorLiveData<PostList> getInterestedCategoriesPosts() {
//        return interestedCategoriesPostsLiveData;
//    }
//
//    public MediatorLiveData<PostList> getTrendingPosts() {
//        return trendingPostsLiveData;
//    }

    public MediatorLiveData<UsersList> getUsersList() {
        return usersLiveData;
    }

    public MediatorLiveData<VideosList> getVideosList() {
        return videosLiveData;
    }
    //endregion

    //region API Calls
    public void loadLandingPosts() {
        try {
            landingPostsLiveData.addSource(
                    apiRepository.getNewDiscoverLandingPosts(),
                    new Observer<LandingPostsV2>() {
                        @Override
                        public void onChanged(@Nullable LandingPostsV2 landingPostsV2) {
                            landingPostsLiveData.setValue(landingPostsV2);
                            loadMostPopularPosts(1);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMostPopularPosts(int page) {
        try {
            mostPopularLiveData.addSource(
                    apiRepository.getAllMostPopularVideos(page),
                    new Observer<PostList>() {
                        @Override
                        public void onChanged(@Nullable PostList postList) {
                            mostPopularLiveData.setValue(postList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFeaturedPosts(int page) {
        try {
            postListLiveData.addSource(
                    apiRepository.getFeaturedPosts(page),
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

    public void loadAllInterestedCategoriesPosts(int page, int categoryId) {
        try {
            postListLiveData.addSource(
                    apiRepository.getAllInterestedCategoriesVideos(page, categoryId),
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

    public void loadTrendingPosts(int page, int categoryId) {
        try {
            postListLiveData.addSource(
                    apiRepository.getTrendingVideos(page, categoryId),
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

    public void loadUsersList(int page) {
        try {
            usersLiveData.addSource(
                    apiRepository.getUsersListToFollow(page),
                    new Observer<UsersList>() {
                        @Override
                        public void onChanged(@Nullable UsersList usersList) {
                            usersLiveData.setValue(usersList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadUsersListWithSearchTerm(int page, String searchTerm) {
        try {
            usersLiveData.addSource(
                    apiRepository.getUsersListToFollowWithSearchTerm(page, searchTerm),
                    new Observer<UsersList>() {
                        @Override
                        public void onChanged(@Nullable UsersList usersList) {
                            usersLiveData.setValue(usersList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadVideosWithSearchTerm(int page, String searchTerm) {
        try {
            videosLiveData.addSource(
                    apiRepository.getVideosWithSearchTerm(page, searchTerm),
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
}