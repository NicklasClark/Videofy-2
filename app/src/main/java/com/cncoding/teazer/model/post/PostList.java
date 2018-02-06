package com.cncoding.teazer.model.post;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class PostList {
    private boolean next_page;
    private ArrayList<PostDetails> posts;

    public PostList(boolean next_page, ArrayList<PostDetails> posts) {
        this.next_page = next_page;
        this.posts = posts;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public ArrayList<PostDetails> getPosts() {
        return posts;
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
}