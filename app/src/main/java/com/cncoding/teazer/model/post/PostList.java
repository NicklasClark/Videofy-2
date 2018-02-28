package com.cncoding.teazer.model.post;

import com.cncoding.teazer.model.BaseModel;
import com.cncoding.teazer.utilities.Annotations.CallType;

import java.util.List;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class PostList extends BaseModel {

    private boolean next_page;
    private int page;
    private List<PostDetails> posts;

    public PostList(List<PostDetails> posts) {
        this.posts = posts;
    }

    public PostList(Throwable error) {
        this.error = error;
    }

    public PostList setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public List<PostDetails> getPosts() {
        return posts;
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

    public void clear() {
        posts.clear();
    }
}