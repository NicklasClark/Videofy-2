package com.cncoding.teazer.ui.home.post.homepage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.post.AdFeedItem;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.data.viewmodel.PostViewModel;
import com.cncoding.teazer.ui.customviews.common.CustomLinearLayoutManager;
import com.cncoding.teazer.ui.customviews.common.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.ui.customviews.exoplayer.Container;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.ui.home.post.BasePostFragment;
import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiNative;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_VOLUME_DOWN;
import static android.view.KeyEvent.KEYCODE_VOLUME_UP;
import static com.cncoding.teazer.utilities.common.AuthUtils.isConnected;
import static com.cncoding.teazer.utilities.common.AuthUtils.isConnectedToWifi;
import static com.cncoding.teazer.utilities.common.SharedPrefs.getCanSaveMediaOnlyOnWiFi;

public class PostsListFragment extends BasePostFragment implements View.OnKeyListener {

    private static final String TAG = "PostListFragment";

    @BindView(R.id.post_list) Container recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.post_load_error) ProximaNovaRegularTextView postLoadErrorTextView;
    @BindView(R.id.post_load_error_layout) LinearLayout postLoadErrorLayout;

    public boolean manualRefreshTriggered;
    private PostsListAdapter postListAdapter;

//    private final List<InMobiNative> mNativeAds = new ArrayList<>();
//    private InMobiNative.NativeAdListener nativeAdListener;
    private int[] mAdPositions = new int[]{2, 8, 15, 23};

    //All the InMobiNativeStrand instances created for this list feed will be held here
    private List<InMobiNative> mStrands = new ArrayList<>();
    private SparseArray<PostDetails> mFeedMap = new SparseArray<>();


    public PostsListFragment() {}

    @NonNull
    public static PostsListFragment newInstance() {
        return new PostsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_posts_list, container, false);
        ButterKnife.bind(this, rootView);

        prepareRecyclerView();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                manualRefreshTriggered = true;
                refreshPosts(true, true);
                refreshAds();
            }
        });
        return rootView;
    }

    private void prepareRecyclerView() {
        postListAdapter = new PostsListAdapter(this);
        recyclerView.setAdapter(postListAdapter);
        bindRecyclerViewAdapter(postListAdapter);
        recyclerView.setSaveEnabled(true);
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(getParentActivity(), LinearLayoutManager.VERTICAL, false));
        if (scrollListener == null)
            scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (is_next_page) {
                        currentPage = page;
                        loadHomePagePosts(page);

                        //for inmobi ads
                        createStrands();
                        loadAds();
                    }
                }
            };
        recyclerView.addOnScrollListener(scrollListener);
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

    public void refreshPosts(boolean scrollToTop, boolean isRefreshing) {
        try {
            if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing() && isRefreshing) swipeRefreshLayout.setRefreshing(true);
            if (!isListAtTop() && scrollToTop) recyclerView.scrollToPosition(0);
            if (!scrollToTop) toggleRecyclerViewScrolling(false);
            if (scrollListener != null && scrollToTop) scrollListener.resetState();
            if (isConnected(context)) {
                loadHomePagePosts(1);
                //to refresh inmobi ads
                refreshAds();
            }
            else {
                Toast.makeText(context, R.string.no_internet_message, Toast.LENGTH_LONG).show();
                if (!scrollToTop) toggleRecyclerViewScrolling(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        refreshAds();
        new InitialRetrieveTask(PostsListFragment.this).execute();
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
                            refreshPosts(true, true);
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

    public boolean isListAtTop() {
        return ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0;
    }

    private void toggleRecyclerViewScrolling(boolean enabled) {
        ((CustomLinearLayoutManager) recyclerView.getLayoutManager()).setScrollEnabled(enabled);
    }

    public PostViewModel getViewModel() {
        return viewModel;
    }

    private void preDownloadVideos(List<PostDetails> postDetailsList) {
        if (isConnected(context)) {
            if (!getCanSaveMediaOnlyOnWiFi(context))
                recyclerView.preDownload(postDetailsList, context);
            else if (isConnectedToWifi(context))
                recyclerView.preDownload(postDetailsList, context);
        }
    }

    @Override
    protected void handleResponse(BaseModel resultObject) {
        if (resultObject instanceof PostList) {
            PostList postList = (PostList) resultObject;
            is_next_page = postList.isNextPage();
            preDownloadVideos(postList.getPosts());
            toggleRecyclerViewScrolling(true);
            if (manualRefreshTriggered) {
                manualRefreshTriggered = false;
                postListAdapter.updateNewPosts(postList.getPosts());
            } else {
                postListAdapter.addPosts(currentPage, postList.getPosts());
            }
            if (currentPage == 1) viewModel.insertAllPostsToDB(postList.getPosts());
            if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override @SuppressWarnings("ConstantConditions")
    protected void handleError(BaseModel baseModel) {
        try {
            if (currentPage > 1 ||
                    (viewModel.getPostList().getValue().getPosts() != null && viewModel.getPostList().getValue().getPosts().isEmpty())) {
                recyclerView.setVisibility(View.INVISIBLE);
                postLoadErrorLayout.setVisibility(View.VISIBLE);
                postLoadErrorTextView.setText(baseModel.getError().getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            recyclerView.setVisibility(View.INVISIBLE);
            postLoadErrorLayout.setVisibility(View.VISIBLE);
            postLoadErrorTextView.setText(baseModel.getError().getMessage());
        } finally {
            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        }
    }

    public static class InitialRetrieveTask extends AsyncTask<Void, Void, ArrayList<PostDetails>> {

        private PostsListFragment fragment;

        InitialRetrieveTask(PostsListFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        protected ArrayList<PostDetails> doInBackground(Void... voids) {
            return fragment.viewModel != null && fragment.viewModel.getPostList().getValue() == null ?
                    new ArrayList<>(fragment.getViewModel().getDatabase().dao().getAllPosts()) :
                    null;
        }

        @Override
        protected void onPostExecute(ArrayList<PostDetails> postDetailsList) {
            if (postDetailsList != null && !postDetailsList.isEmpty()) {
                fragment.getViewModel().getPostList().setValue(new PostList(postDetailsList));
//                Posts available in Room DB, just update new Posts.
                fragment.manualRefreshTriggered = true;
                fragment.refreshPosts(true, false);
            } else {
//                Posts are not available in Room DB, or coming back from some other fragment, fetch them from server.
                fragment.refreshPosts(false, false);
            }
        }
    }

    private final class StrandAdListener implements InMobiNative.NativeAdListener {

        private int mPosition;

        StrandAdListener(int position) {
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