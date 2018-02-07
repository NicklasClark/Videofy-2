package com.cncoding.teazer.data.repository;

import com.cncoding.teazer.data.room.daos.PostDetailsDao;

import javax.inject.Singleton;

/**
 *
 * Created by Prem$ on 1/23/2018.
 */

@Singleton
public class PostListRepository {

    private PostDetailsDao postDetailsDao;

    public PostListRepository(PostDetailsDao postDetailsDao) {
        this.postDetailsDao = postDetailsDao;
    }

    public PostListRepository() {
    }
}
