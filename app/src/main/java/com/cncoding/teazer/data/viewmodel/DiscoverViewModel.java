package com.cncoding.teazer.data.viewmodel;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.model.discover.LandingPostsV2;
import com.cncoding.teazer.data.model.discover.VideosList;
import com.cncoding.teazer.data.model.friends.UsersList;
import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.data.remote.apicalls.discover.DiscoverRepository;
import com.cncoding.teazer.data.remote.apicalls.discover.DiscoverRepositoryImpl;

import javax.inject.Inject;

/**
 *
 * Created by Prem$ on 2/20/2018.
 */

public class DiscoverViewModel extends ViewModel {

    private MediatorLiveData<LandingPostsV2> landingPostsLiveData;
    private MediatorLiveData<PostList> postListLiveData;
    private MediatorLiveData<PostList> mostPopularLiveData;
    private MediatorLiveData<UsersList> usersLiveData;
    private MediatorLiveData<VideosList> videosLiveData;
    private DiscoverRepository discoverRepository;

    @Inject DiscoverViewModel(MediatorLiveData<LandingPostsV2> landingPostsLiveData, MediatorLiveData<PostList> postListLiveData,
                              MediatorLiveData<PostList> mostPopularLiveData, MediatorLiveData<UsersList> usersLiveData,
                              MediatorLiveData<VideosList> videosLiveData, DiscoverRepository discoverRepository) {
        this.landingPostsLiveData = landingPostsLiveData;
        this.postListLiveData = postListLiveData;
        this.mostPopularLiveData = mostPopularLiveData;
        this.usersLiveData = usersLiveData;
        this.videosLiveData = videosLiveData;
        this.discoverRepository = discoverRepository;
    }

    public DiscoverViewModel(String token) {
        landingPostsLiveData = new MediatorLiveData<>();
        postListLiveData = new MediatorLiveData<>();
        mostPopularLiveData = new MediatorLiveData<>();
        usersLiveData = new MediatorLiveData<>();
        videosLiveData = new MediatorLiveData<>();
        discoverRepository = new DiscoverRepositoryImpl(token);
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
                    discoverRepository.getNewDiscoverLandingPosts(),
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
                    discoverRepository.getAllMostPopularVideos(page),
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

    public void loadAllInterestedCategoriesPosts(int page, int categoryId) {
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

    public void loadTrendingPostsByCategory(int page, int categoryId) {
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

    public void loadUsersList(int page) {
        try {
            usersLiveData.addSource(
                    discoverRepository.getUsersListToFollow(page),
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
                    discoverRepository.getUsersListToFollowWithSearchTerm(page, searchTerm),
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

    public void loadTrendingVideos(int page) {
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

    public void loadVideosWithSearchTerm(int page, String searchTerm) {
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
}