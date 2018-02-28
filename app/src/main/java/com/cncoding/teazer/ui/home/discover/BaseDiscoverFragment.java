package com.cncoding.teazer.ui.home.discover;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.discover.LandingPostsV2;
import com.cncoding.teazer.data.model.discover.VideosList;
import com.cncoding.teazer.data.model.friends.UsersList;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.data.viewmodel.DiscoverViewModel;
import com.cncoding.teazer.data.viewmodel.factory.AuthTokenViewModelFactory;
import com.cncoding.teazer.ui.base.BaseFragment;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cncoding.teazer.utilities.common.Annotations.NO_CALL;
import static com.cncoding.teazer.utilities.common.SharedPrefs.getAuthToken;

/**
 *
 * Created by Prem$ on 2/20/2018.
 */

public abstract class BaseDiscoverFragment extends BaseFragment {

    protected static DiscoverViewModel viewModel;
    protected int currentPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (viewModel == null) {
            viewModel = ViewModelProviders
                    .of(this, new AuthTokenViewModelFactory(getAuthToken(getContext())))
                    .get(DiscoverViewModel.class);
        }
        currentPage = 1;
    }

    /**
     * Setting observers.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getLandingPosts().observe(this, new Observer<LandingPostsV2>() {
            @Override
            public void onChanged(@Nullable LandingPostsV2 landingPostsV2) {
                handleLiveDataChange(landingPostsV2);
            }
        });

        viewModel.getPostListLiveData().observe(this, new Observer<PostList>() {
            @Override
            public void onChanged(@Nullable PostList postList) {
                handleLiveDataChange(postList);
            }
        });

        viewModel.getMostPopularPosts().observe(this, new Observer<PostList>() {
            @Override
            public void onChanged(@Nullable PostList postList) {
                handleLiveDataChange(postList);
            }
        });

        viewModel.getUsersList().observe(this, new Observer<UsersList>() {
            @Override
            public void onChanged(@Nullable UsersList usersList) {
                handleLiveDataChange(usersList);
            }
        });

        viewModel.getVideosList().observe(this, new Observer<VideosList>() {
            @Override
            public void onChanged(@Nullable VideosList videosList) {
                handleLiveDataChange(videosList);
            }
        });
    }

    @Contract(pure = true) public DiscoverViewModel getViewModel() {
        return viewModel;
    }

    private void handleLiveDataChange(BaseModel baseModel) {
        if (baseModel != null) {
            if (baseModel.getError() != null) handleError(baseModel);
            else handleResponse(baseModel);
        }
        else handleError(
                new BaseModel()
                        .loadError(new Throwable(getString(R.string.something_went_wrong)))
                        .loadCallType(NO_CALL));
    }

    private void notifyNoInternetConnection() {
        Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
    }

    protected ArrayList<PostDetails> getFeaturedPosts() {
        try {
            //noinspection ConstantConditions
            return viewModel.getLandingPosts().getValue().getFeaturedVideos();
        } catch (Exception e) {
            return null;
        }
    }

    protected List<PostDetails> getMostPopularPosts() {
        try {
            //noinspection ConstantConditions
            return viewModel.getMostPopularPosts().getValue().getPosts();
        } catch (Exception e) {
            return null;
        }
    }

    protected ArrayList<Category> getUserInterests() {
        try {
            //noinspection ConstantConditions
            return viewModel.getLandingPosts().getValue().getUserInterests();
        } catch (Exception e) {
            return null;
        }
    }

    protected ArrayList<Category> getTrendingCategories() {
        try {
            //noinspection ConstantConditions
            return viewModel.getLandingPosts().getValue().getTrendingCategories();
        } catch (Exception e) {
            return null;
        }
    }

    protected Map<String, ArrayList<PostDetails>> getMyInterests() {
        try {
            //noinspection ConstantConditions
            return viewModel.getLandingPosts().getValue().getMyInterests();
        } catch (Exception e) {
            return null;
        }
    }

    public void loadLandingPosts() {
        viewModel.loadLandingPosts();
    }

    public void loadMostPopularPosts(int page) {
        viewModel.loadMostPopularPosts(page);
    }

    public void loadFeaturedPosts(int page) {
        viewModel.loadFeaturedPosts(page);
    }

    public void loadAllInterestedCategoriesPosts(int page, int categoryId) {
        viewModel.loadAllInterestedCategoriesPosts(page, categoryId);
    }

    public void loadTrendingPostsByCategory(int page, int categoryId) {
        viewModel.loadTrendingPostsByCategory(page, categoryId);
    }

    public void loadTrendingPosts(int page) {
        viewModel.loadTrendingVideos(page);
    }

    public void loadUsersList(int page) {
        viewModel.loadUsersList(page);
    }

    public void loadUsersListWithSearchTerm(int page, String searchTerm) {
        viewModel.loadUsersListWithSearchTerm(page, searchTerm);
    }

    public void loadVideosWithSearchTerm(int page, String searchTerm) {
        viewModel.loadVideosWithSearchTerm(page, searchTerm);
    }

    public void loadTrendingVideos(int page) {
        viewModel.loadTrendingVideos(page);
    }

    protected abstract void handleResponse(BaseModel resultObject);

    protected abstract void handleError(BaseModel baseModel);
}