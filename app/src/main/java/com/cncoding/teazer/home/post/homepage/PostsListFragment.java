package com.cncoding.teazer.home.post.homepage;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.data.viewmodel.PostDetailsViewModel;
import com.cncoding.teazer.data.viewmodel.factory.PostDetailsViewModelFactory;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.post.AdFeedItem;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostList;
import com.cncoding.teazer.utilities.SharedPrefs;
import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiNative;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.toro.widget.Container;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_VOLUME_DOWN;
import static android.view.KeyEvent.KEYCODE_VOLUME_UP;

public class PostsListFragment extends BaseFragment implements View.OnKeyListener {

    private static final String TAG = "PostListFragment";
    //    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.post_list) Container recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.post_load_error) ProximaNovaRegularTextView postLoadErrorTextView;
    @BindView(R.id.post_load_error_layout) LinearLayout postLoadErrorLayout;

    public static boolean isRefreshing;
    public static PostDetails postDetails;
    public static int positionToUpdate = -1;
    private int currentPage;
    private PostDetailsViewModel postDetailsViewModel;
    private PostsListAdapter postListAdapter;

    private final List<InMobiNative> mNativeAds = new ArrayList<>();
    private InMobiNative.NativeAdListener nativeAdListener;
    private int[] mAdPositions = new int[]{3, 8, 15, 23};

    //All the InMobiNativeStrand instances created for this list feed will be held here
    private List<InMobiNative> mStrands = new ArrayList<>();
    private Map<Integer, PostDetails> mFeedMap = new HashMap<>();


    public PostsListFragment() {
    }
    
    @NonNull
    public static PostsListFragment newInstance() {
        return new PostsListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PostDetailsViewModelFactory factory = new PostDetailsViewModelFactory(SharedPrefs.getAuthToken(getContext()));
        postDetailsViewModel = ViewModelProviders.of(this, factory).get(PostDetailsViewModel.class);
        currentPage = 1;

        //inmobi ads
//        InMobiNative nativeAd = new InMobiNative(getActivity(), Long.parseLong("1519192553502"), nativeAdListener);
//        nativeAd.load();
//        mNativeAds.add(nativeAd);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_posts_list, container, false);
        previousTitle = getParentActivity().getToolbarTitle();
        getParentActivity().updateToolbarTitle(null);
        ButterKnife.bind(this, rootView);

        prepareRecyclerView();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPosts();
            }
        });

        // Handle changes emitted by LiveData
        postDetailsViewModel.getPostList().observe(this, new Observer<PostList>() {
            @Override
            public void onChanged(@Nullable PostList postList) {
                if (postList != null) {
                    if (postList.getError() != null) {
                        handleError(getString(R.string.something_went_wrong));
                    } else {
                        is_next_page = postList.isNextPage();
                        handleResponse(postList.getPosts());
                    }
                } else {
                    handleError(getString(R.string.something_went_wrong));
                }
            }
        });

        return rootView;
    }

    private void prepareRecyclerView() {
        postListAdapter = new PostsListAdapter(getContext());
        recyclerView.setAdapter(postListAdapter);
        bindRecyclerViewAdapter(postListAdapter);
        recyclerView.setSaveEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getParentActivity(), LinearLayoutManager.VERTICAL, false));
        scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void loadFirstPage() {
                currentPage = 1;
                getHomePagePosts(1);

                //for inmobi ads
                createStrands();
                loadAds();
            }

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page) {
                    currentPage = page;
                    getHomePagePosts(page);

                    //for inmobi ads
                    createStrands();
                    loadAds();
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void createStrands() {
        Long placement_id = 1519192553502L;
        for (int position : mAdPositions) {
            final InMobiNative nativeStrand = new InMobiNative(getActivity(),
                    placement_id, new StrandAdListener((currentPage-1)*30+position));
            mStrands.add(nativeStrand);
        }
    }

    @Override
    public void onDestroyView() {
        clearAds();
        super.onDestroyView();
    }

    private void loadAds() {
        for (InMobiNative strand : mStrands) {
            strand.load();
        }
    }

    private void refreshAds() {
        clearAds();
        createStrands();
        loadAds();
    }

    private void clearAds() {
        Iterator<PostDetails> feedItemIterator = postListAdapter.posts.iterator();
        while (feedItemIterator.hasNext()) {
            final PostDetails feedItem = feedItemIterator.next();
            if (feedItem instanceof AdFeedItem) {
                feedItemIterator.remove();
            }
        }
        postListAdapter.notifyDataSetChanged();
        for (InMobiNative strand : mStrands) {
            strand.destroy();
        }
        mStrands.clear();
        mFeedMap.clear();
    }

    public void refreshPosts() {
        swipeRefreshLayout.setRefreshing(true);
        postDetailsViewModel.clear();
        postListAdapter.clear();
        getHomePagePosts(1);

        //to refresh inmobi ads
        refreshAds();

        scrollListener.resetState();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    public void getHomePagePosts(int page) {
        postDetailsViewModel.loadPostList(page);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        try {
            if (keyEvent.getAction() == ACTION_DOWN) {
                switch (keyCode) {
                    case KEYCODE_VOLUME_DOWN:
                        recyclerView.smoothScrollBy(0, (int) recyclerView.getChildAt(recyclerView.getChildCount() - 2).getY(),
                                new DecelerateInterpolator());
                        return true;
                    case KEYCODE_VOLUME_UP:
                        int[] positions = new int[2];
                        if (((StaggeredGridLayoutManager) recyclerView.getLayoutManager())
                                .findFirstCompletelyVisibleItemPositions(positions)[0] == 0)
                            refreshPosts();
                        else
                            recyclerView.smoothScrollBy(0, -((int) recyclerView.getChildAt(recyclerView.getChildCount() - 1).getY()),
                                new DecelerateInterpolator());
                        return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefreshing) {
            isRefreshing = false;
            updateOrRemovePostDetailsAtThePosition();
            refreshAds();
        }
    }

    private void updateOrRemovePostDetailsAtThePosition() {
        if (postDetailsViewModel.getPostList().getValue() != null && postDetails == null) {
//            Item is deleted
            try {
                postDetailsViewModel.getPostList().getValue().getPosts().remove(positionToUpdate);
                postListAdapter.notifyItemRemoved(positionToUpdate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
//            Item is modified
            if (postListAdapter != null) {
                postListAdapter.notifyItemChanged(positionToUpdate, postDetails);
            }
        }
    }

    private void handleResponse(List<PostDetails> postDetailsList) {
        postListAdapter.addPosts(currentPage, postDetailsList);
    }

    @SuppressWarnings("ConstantConditions")
    private void handleError(String message) {
        try {
            if (currentPage > 1 || postDetailsViewModel.getPostList().getValue().getPosts().isEmpty()) {
                recyclerView.setVisibility(View.INVISIBLE);
                postLoadErrorLayout.setVisibility(View.VISIBLE);
                postLoadErrorTextView.setText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            recyclerView.setVisibility(View.INVISIBLE);
            postLoadErrorLayout.setVisibility(View.VISIBLE);
            postLoadErrorTextView.setText(message);
        }
    }

    public boolean isListAtTop() {
        return ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0;
    }

    public void scrollToTop() {
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getParentActivity().updateToolbarTitle(previousTitle);
        positionToUpdate = -1;
    }

    private final class StrandAdListener implements InMobiNative.NativeAdListener {

        private int mPosition;

        public StrandAdListener(int position) {
            mPosition = position;
        }

        @Override
        public void onAdLoadSucceeded(@NonNull InMobiNative inMobiNativeStrand) {
            Log.d(TAG, "Strand loaded at position " + mPosition);
            if (!postListAdapter.posts.isEmpty()) {
                PostDetails oldFeedItem = mFeedMap.get(mPosition);
                if (oldFeedItem != null) {
                    mFeedMap.remove(mPosition);
                    postListAdapter.posts.remove(oldFeedItem);
                }
                AdFeedItem adFeedItem = new AdFeedItem(inMobiNativeStrand);
                mFeedMap.put(mPosition, adFeedItem);
                postListAdapter.posts.add(mPosition, adFeedItem);
                postListAdapter.notifyItemChanged(mPosition);
            }
        }

        @Override
        public void onAdLoadFailed(@NonNull InMobiNative inMobiNativeStrand, @NonNull final InMobiAdRequestStatus inMobiAdRequestStatus) {
            Log.d(TAG, "Ad Load failed  for" + mPosition + "(" + inMobiAdRequestStatus.getMessage() + ")");
            if (!postListAdapter.posts.isEmpty()) {
                PostDetails oldFeedItem = mFeedMap.get(mPosition);
                if (oldFeedItem != null) {
                    mFeedMap.remove(mPosition);
                    postListAdapter.posts.remove(oldFeedItem);
                    postListAdapter.notifyItemRemoved(mPosition);
                    Log.d(TAG, "Ad removed for" + mPosition);
                }
            }
        }

        @Override
        public void onAdFullScreenDismissed(InMobiNative inMobiNative) {}

        @Override
        public void onAdFullScreenWillDisplay(InMobiNative inMobiNative) {}

        @Override
        public void onAdFullScreenDisplayed(InMobiNative inMobiNative) {}

        @Override
        public void onUserWillLeaveApplication(InMobiNative inMobiNative) {}

        @Override
        public void onAdImpressed(@NonNull InMobiNative inMobiNativeStrand) {
            Log.d(TAG, "Impression recorded for strand at position:" + mPosition);
        }

        @Override
        public void onAdClicked(@NonNull InMobiNative inMobiNativeStrand) {
            Log.d(TAG, "Click recorded for ad at position:" + mPosition);
        }

        @Override
        public void onMediaPlaybackComplete(@NonNull InMobiNative inMobiNative) {}

        @Override
        public void onAdStatusChanged(@NonNull InMobiNative inMobiNative) {}
    }
}