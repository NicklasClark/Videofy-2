package com.cncoding.teazer.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.local.database.TeazerDB;
import com.cncoding.teazer.data.remote.apicalls.post.PostsRepository;
import com.cncoding.teazer.data.remote.apicalls.post.PostsRepositoryImpl;
import com.cncoding.teazer.model.base.Medias;
import com.cncoding.teazer.model.base.TaggedUser;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostList;
import com.cncoding.teazer.model.post.PostReaction;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@SuppressWarnings("unchecked")
public class PostDetailsViewModel extends AndroidViewModel {

    private static final int DELETE = 0;
    private static final int INSERT = 1;
    private static final int LIKE = 2;
    private static final int DISLIKE = 4;
    private MediatorLiveData<PostList> livePostDetailsList;
    private PostsRepository apiRepository;
    private TeazerDB database;

    public PostDetailsViewModel(Application application, String token) {
        super(application);
        this.apiRepository = new PostsRepositoryImpl(token);
        database = TeazerDB.getInstance(this.getApplication());
        livePostDetailsList = new MediatorLiveData<>();
    }

    @Inject PostDetailsViewModel(Application application, MediatorLiveData<PostList> livePostDetailsList,
                         PostsRepository apiRepository, TeazerDB database) {
        super(application);
        this.livePostDetailsList = livePostDetailsList;
        this.apiRepository = apiRepository;
        this.database = database;
    }

    @NonNull public MediatorLiveData<PostList> getPostList() {
        return livePostDetailsList;
    }

    public void clearData() {
        if (livePostDetailsList.getValue() != null && livePostDetailsList.getValue().getPosts() != null) {
            livePostDetailsList.getValue().getPosts().clear();
        }
    }

    /**
     * API call to fetch post list.
     */
    public void loadPostList(final int page) {
        try {
            livePostDetailsList.addSource(
                    apiRepository.getHomePagePosts(page),
                    new Observer<PostList>() {
                        @Override
                        public void onChanged(@Nullable PostList postList) {
                            livePostDetailsList.setValue(postList);
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

    public boolean getAllPosts() {
        try {
            List<PostDetails> postDetailsList = database.dao().getAllPosts();
            boolean result = postDetailsList != null && !postDetailsList.isEmpty();
            if (result) livePostDetailsList.setValue(new PostList(postDetailsList));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public PostDetails getPost(int postId) {
        try {
            //noinspection ConstantConditions
            return database.dao().getPost(postId).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertAllPosts(List<PostDetails> postDetails) {
        new InsertAllTask(database).execute(postDetails);
    }

    public void updatePost(PostDetails postDetails) {
        database.dao().updatePost(postDetails);
    }

    public void likePost(int postId) {
        new PostUpdateTaskWithId(LIKE, database).execute(postId);
    }

    public void dislikePost(int postId) {
        new PostUpdateTaskWithId(DISLIKE, database).execute(postId);
    }

    public void deletePost(int postId) {
        new PostUpdateTaskWithId(DELETE, database).execute(postId);
    }

    public void deletePost(PostDetails postDetails) {
        new PostUpdateTaskWithObject(DELETE, database).execute(postDetails);
    }

    public void deleteAllStoredPosts() {
        database.dao().clearTable();
    }

    public void updateTitle(String title, int postId) {
        database.dao().updateTitle(title, postId);
    }

    public void updateTags(ArrayList<TaggedUser> taggedUsers, int postId) {
        database.dao().updateTotalTagsCount(taggedUsers != null ? taggedUsers.size() : 0, postId);
        database.dao().updateTaggedUsers(taggedUsers, postId);
    }

    public void updateReactions(ArrayList<PostReaction> reactions, boolean canReact, int postId) {
        database.dao().updateTotalReactionCount(reactions != null ? reactions.size() : 0, canReact, postId);
        database.dao().updateReactions(reactions, postId);
    }

    public void incrementViews(ArrayList<Medias> medias, int postId) {
        database.dao().incrementViews(medias, postId);
    }

    private static class PostUpdateTaskWithObject extends AsyncTask<PostDetails, Void, Void> {

        private int arg;
        private TeazerDB database;

        PostUpdateTaskWithObject(int arg, TeazerDB database) {
            this.arg = arg;
            this.database = database;
        }

        @Override
        protected Void doInBackground(PostDetails... postDetails) {
            switch (arg) {
                case DELETE:
                    database.dao().deletePost(postDetails[0]);
                    break;
                case INSERT:
                    database.dao().insertPost(postDetails[0]);
                    break;
                default:
                    break;
            }
            return null;
        }
    }

    private static class PostUpdateTaskWithId extends AsyncTask<Integer, Void, PostDetails> {

        private int arg;
        private TeazerDB database;

        PostUpdateTaskWithId(int arg, TeazerDB database) {
            this.arg = arg;
            this.database = database;
        }

        @Override
        protected PostDetails doInBackground(Integer... integers) {
            try {
                switch (arg) {
                    case LIKE:
                        database.dao().likePost(integers[0]);
                        return null;
                    case DISLIKE:
                        database.dao().dislikePost(integers[0]);
                        return null;
                    case DELETE:
                        database.dao().deletePost(integers[0]);
                        return null;
                    default:
                        return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private static class InsertAllTask extends AsyncTask<List<PostDetails>, Void, Void> {

        private TeazerDB database;

        InsertAllTask(TeazerDB database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(List<PostDetails>[] lists) {
            database.dao().insertAll(lists[0]);
            return null;
        }
    }
}