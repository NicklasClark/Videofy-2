package com.cncoding.teazer.data.local.database;

import android.os.AsyncTask;

import com.cncoding.teazer.data.viewmodel.PostViewModel;
import com.cncoding.teazer.model.base.Medias;
import com.cncoding.teazer.model.base.TaggedUser;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostReaction;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Prem$ on 2/27/2018.
 */

public class DataAccessTasks {

    public static class UpdatePostTask extends AsyncTask<Void, Void, Void> {

        private TeazerDB database;
        private PostDetails postDetails;

        public UpdatePostTask(TeazerDB database, PostDetails postDetails) {
            this.database = database;
            this.postDetails = postDetails;
        }

        @Override protected Void doInBackground(Void... voids) {
            database.dao().updatePost(postDetails);
            return null;
        }
    }

    public static class UpdateTitleTask extends AsyncTask<Void, Void, Void> {

        private TeazerDB database;
        private String title;
        private int postId;

        public UpdateTitleTask(TeazerDB database, String title, int postId) {
            this.database = database;
            this.title = title;
            this.postId = postId;
        }

        @Override protected Void doInBackground(Void... voids) {
            database.dao().updateTitle(title, postId);
            return null;
        }
    }

    public static class UpdateTagsReactionsTask extends AsyncTask<Void, Void, Void> {

        private TeazerDB database;
        private ArrayList<TaggedUser> taggedUsers;
        private ArrayList<PostReaction> reactions;
        private boolean canReact;
        private int postId;
        private int type;

        public UpdateTagsReactionsTask(TeazerDB database, ArrayList<TaggedUser> taggedUsers,
                                        ArrayList<PostReaction> reactions, boolean canReact, int type, int postId) {
            this.database = database;
            this.taggedUsers = taggedUsers;
            this.reactions = reactions;
            this.type = type;
            this.canReact = canReact;
            this.postId = postId;
        }

        @Override protected Void doInBackground(Void... voids) {
            switch (type) {
                case PostViewModel.TAGS:
                    database.dao().updateTotalTagsCount(taggedUsers != null ? taggedUsers.size() : 0, postId);
                    database.dao().updateTaggedUsers(taggedUsers, postId);
                    break;
                case PostViewModel.REACTIONS:
                    database.dao().updateTotalReactionCount(reactions != null ? reactions.size() : 0, canReact, postId);
                    database.dao().updateReactions(reactions, postId);
                    break;
                default:
                    break;
            }
            return null;
        }
    }

    public static class PostUpdateTaskWithObject extends AsyncTask<PostDetails, Void, Void> {

        private int arg;
        private TeazerDB database;

        public PostUpdateTaskWithObject(int arg, TeazerDB database) {
            this.arg = arg;
            this.database = database;
        }

        @Override
        protected Void doInBackground(PostDetails... postDetails) {
            switch (arg) {
                case PostViewModel.DELETE:
                    database.dao().deletePost(postDetails[0]);
                    break;
                case PostViewModel.INSERT:
                    database.dao().insertPost(postDetails[0]);
                    break;
                default:
                    break;
            }
            return null;
        }
    }

    public static class PostUpdateTaskWithId extends AsyncTask<Integer, Void, PostDetails> {

        private int arg;
        private TeazerDB database;

        public PostUpdateTaskWithId(int arg, TeazerDB database) {
            this.arg = arg;
            this.database = database;
        }

        @Override
        protected PostDetails doInBackground(Integer... integers) {
            try {
                switch (arg) {
                    case PostViewModel.LIKE:
                        database.dao().likePost(integers[0]);
                        return null;
                    case PostViewModel.DISLIKE:
                        database.dao().dislikePost(integers[0]);
                        return null;
                    case PostViewModel.DELETE:
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

    public static class InsertAllTask extends AsyncTask<List<PostDetails>, Void, Void> {

        private TeazerDB database;

        public InsertAllTask(TeazerDB database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(List<PostDetails>[] lists) {
            database.dao().insertAll(lists[0]);
            return null;
        }
    }

    public static class IncrementViewsTask extends AsyncTask<ArrayList<Medias>, Void, Void> {

        private TeazerDB database;
        private int postId;

        public IncrementViewsTask(TeazerDB database, int postId) {
            this.database = database;
            this.postId = postId;
        }

        @Override
        protected Void doInBackground(ArrayList<Medias>[] lists) {
            database.dao().incrementViews(lists[0], postId);
            return null;
        }
    }

    public static class DeleteAllTask extends AsyncTask<ArrayList<Medias>, Void, Void> {

        private TeazerDB database;

        public DeleteAllTask(TeazerDB database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(ArrayList<Medias>[] lists) {
            database.dao().clearTable();
            return null;
        }
    }
}
