package com.cncoding.teazer.data.model.post;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class PostList extends ViewModel {
    private boolean next_page;
    private ArrayList<PostDetails> posts;
    private Throwable error;

    public PostList(ArrayList<PostDetails> posts) {
        this.posts = posts;
    }

    public PostList(Throwable error) {
        this.error = error;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public ArrayList<PostDetails> getPosts() {
        return posts;
    }

    public Throwable getError() {
        return error;
    }

    public void add(PostDetails postDetails) {
        posts.add(postDetails);
    }

    public void setNextPage(boolean next_page) {
        this.next_page = next_page;
    }

    public void setPosts(ArrayList<PostDetails> posts) {
        this.posts = posts;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}