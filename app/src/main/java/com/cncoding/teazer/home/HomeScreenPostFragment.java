package com.cncoding.teazer.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.Pojos.Post.PostList;
import com.cncoding.teazer.utilities.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link HomeScreenPostInteractionListener}
 * interface.
 */
public class HomeScreenPostFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    @BindView(R.id.list) RecyclerView recyclerView;

    private int columnCount = 1;
    private PostList postList;
    private HomeScreenPostInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeScreenPostFragment() {
    }

    public static HomeScreenPostFragment newInstance(int columnCount) {
        HomeScreenPostFragment fragment = new HomeScreenPostFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postList = new PostList(false, new ArrayList<PostDetails>());
        if (getArguments() != null) {
            columnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_screen_post, container, false);
        ButterKnife.bind(this, rootView);
        getHomePagePosts(1);
        return rootView;
    }

    private void getHomePagePosts(final int pageNumber) {
        ApiCallingService.Posts.getHomePagePosts(pageNumber)
                .enqueue(new Callback<PostList>() {
                    @Override
                    public void onResponse(Call<PostList> call, Response<PostList> response) {
                        if (response.code() == 200) {
//                            Posts came! now fetching the posts and checking if next page is true (More posts are available) or not.
                            for (int i = 0; i < response.body().getPosts().size(); i++) {
                                postList.add(response.body().getPosts().get(i));
                            }
                            if (response.body().isNextPage()) {
//                                More posts are available, so incrementing the page number and re-calling this method.
                                getHomePagePosts(pageNumber + 1);
                            }
                            else {
//                                No more posts available. So populating the posts in the recyclerView.
                                if (postList.getPosts().size() > 0)
                                    populateRecyclerView(postList.getPosts());
                                else populateDummyLists();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PostList> call, Throwable t) {
                        ViewUtils.makeSnackbarWithBottomMargin(getActivity(), recyclerView, t.getMessage());
                    }
                });
    }

    private void populateRecyclerView(List<PostDetails> posts) {
        StaggeredGridLayoutManager manager;
        manager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new HomeScreenPostAdapter(posts, mListener, getContext()));
    }

    private void populateDummyLists() {
        StaggeredGridLayoutManager manager;
        if (columnCount <= 1) {
            manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        } else {
            manager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        }
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        ArrayList<Pojos.Medias> medias1 = new ArrayList<>();
        medias1.add(new Pojos.Medias(0, "",
                "https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg",
                "", "", false, 0, ""));
        ArrayList<Pojos.Medias> medias2 = new ArrayList<>();
        medias2.add(new Pojos.Medias(0, "",
                "https://i.pinimg.com/736x/84/e1/57/84e15741767278781febecef51b8f6e7--portrait-photography-tips-people-photography.jpg",
                "", "", false, 0, ""));
        ArrayList<Pojos.Medias> medias3 = new ArrayList<>();
        medias3.add(new Pojos.Medias(0, "",
                "http://is5.mzstatic.com/image/thumb/Purple71/v4/90/ae/1b/90ae1b97-762d-82fa-1ff9-bcc76435ea88/source/1200x630bb.jpg",
                "", "", false, 0, ""));
        ArrayList<Pojos.Category> categories = new ArrayList<>();
        categories.add(new Pojos.Category(1, "Fun"));
        PostDetails postDetails1 = new PostDetails(12, 324, 456, 12, false, "Watch this!", false, false, false,
                new Pojos.MiniProfile(324, "prem", "Prem", "Suman", true,
                        new Pojos.ProfileMedia(
                                "",
                                "https://timesofindia.indiatimes.com/thumb/msid-59564820,width-400,resizemode-4/59564820.jpg",
                                "00:35",
                                "20",
                                false
                        )),
                "", new Pojos.CheckIn(1, 12, 17, "Bangalore"),
                medias1,
                categories
        );
        PostDetails postDetails2 = new PostDetails(12, 324, 456, 12, false, "Weekend at Bahamas", false, false, false,
                new Pojos.MiniProfile(324, "prem", "Prem", "Suman", true,
                        new Pojos.ProfileMedia(
                                "",
                                "https://timesofindia.indiatimes.com/thumb/msid-59564820,width-400,resizemode-4/59564820.jpg",
                                "00:35",
                                "20",
                                false
                        )),
                "", new Pojos.CheckIn(1, 12, 17, "Bangalore"),
                medias2,
                categories
        );
        PostDetails postDetails3 = new PostDetails(12, 324, 456, 12, false, "See this!", false, false, false,
                new Pojos.MiniProfile(324, "prem", "Prem", "Suman", true,
                        new Pojos.ProfileMedia(
                                "",
                                "https://timesofindia.indiatimes.com/thumb/msid-59564820,width-400,resizemode-4/59564820.jpg",
                                "00:35",
                                "20",
                                false
                        )),
                "", new Pojos.CheckIn(1, 12, 17, "Bangalore"),
                medias3,
                categories
        );
        List<PostDetails> postDetailsList = new ArrayList<>();
        for (int i = 0; i < 20; i ++) {
            postDetailsList.add(postDetails1);
            postDetailsList.add(postDetails2);
            postDetailsList.add(postDetails3);
        }

        HomeScreenPostAdapter adapter =
                new HomeScreenPostAdapter(postDetailsList, mListener, getContext());

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeScreenPostInteractionListener) {
            mListener = (HomeScreenPostInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement HomeScreenPostInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    interface HomeScreenPostInteractionListener {
        void onHomeScreenPostInteraction(int action, PostDetails postDetails);
    }
}
