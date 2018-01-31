package com.cncoding.teazer.data.repository.local.post;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.room.daos.PostDetailsDao;

import java.util.List;

import javax.inject.Inject;

/**
 *
 * Created by Prem$ on 1/31/2018.
 */

public class PostDetailsLocalRepo {

    private PostDetailsDao postDetailsDao;

    @Inject public PostDetailsLocalRepo(PostDetailsDao postDetailsDao) {
        this.postDetailsDao = postDetailsDao;
    }

    public LiveData<List<PostDetails>> getAllPosts() {
        return postDetailsDao.getAllPosts();
    }

    public LiveData<List<PostDetails>> getPost(int postId) {
        return postDetailsDao.getPost(postId);
    }

//    public LiveData<List<MiniProfile>> getPostOwner(int postId) {
//        return postDetailsDao.getPostOwner(postId);
//    }

//    public LiveData<List<ArrayList<Medias>>> getPostMedia(int postId) {
//        return postDetailsDao.getPostMedia(postId);
//    }

    public void likePost() {
        postDetailsDao.likePost();
    }

    public void dislikePost() {
        postDetailsDao.dislikePost();
    }

//    public void incrementViews() {
//        postDetailsDao.incrementViews();
//    }

    public void deletePost(int postId) {
        postDetailsDao.deletePost(postId);
    }

    public void deletePost(PostDetails postDetails) {
        postDetailsDao.deletePost(postDetails);
    }

    public List<Long> insertAll(List<PostDetails> postDetails) {
        return postDetailsDao.insertAll(postDetails);
    }

    public long insertPost(PostDetails postDetails) {
        return postDetailsDao.insertPost(postDetails);
    }
}
