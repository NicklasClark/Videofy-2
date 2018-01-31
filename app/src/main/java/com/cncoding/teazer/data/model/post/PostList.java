package com.cncoding.teazer.data.model.post;

import java.util.List;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class PostList {
    private boolean next_page;
    private int page;
    private List<PostDetails> posts;
    private Throwable error;

    public PostList(List<PostDetails> posts) {
        this.posts = posts;
    }

    public PostList(Throwable error) {
        this.error = error;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public List<PostDetails> getPosts() {
        return posts;
    }

    public Throwable getError() {
        return error;
    }

    public int getPage() {
        return page;
    }

    public void add(PostDetails postDetails) {
        posts.add(postDetails);
    }

    public void setNextPage(boolean next_page) {
        this.next_page = next_page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPosts(List<PostDetails> posts) {
        this.posts = posts;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}