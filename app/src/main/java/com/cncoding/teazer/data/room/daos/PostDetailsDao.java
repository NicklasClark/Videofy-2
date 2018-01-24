package com.cncoding.teazer.data.room.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.cncoding.teazer.data.model.base.CheckIn;
import com.cncoding.teazer.data.model.base.MiniProfile;
import com.cncoding.teazer.data.model.post.PostDetails;

import java.util.ArrayList;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 *
 * Created by Prem $ on 1/18/2018.
 */

@Dao
public interface PostDetailsDao {

    //region Getter queries
    @Query("SELECT * FROM PostDetails")
    ArrayList<PostDetails> getAllPosts();

    @Query("SELECT * FROM PostDetails WHERE post_id LIKE :postId")
    PostDetails getPost(int postId);

    @Query("SELECT post_owner FROM PostDetails WHERE post_id = :postId LIMIT 1")
    MiniProfile getPostOwner(int postId);

    @Query("SELECT check_in FROM PostDetails WHERE post_id = :postId LIMIT 1")
    CheckIn getPostCheckIn(int postId);
    //endregion

    //region Update queries
    @Query("UPDATE PostDetails SET likes = :likeCount")
    void updateLikes(int likeCount);

    @Query("UPDATE PostDetails SET views = :viewCount")
    void updateViews(int viewCount);
    //endregion

    //region Deletion queries
    @Query("DELETE FROM PostDetails WHERE post_id = :postId")
    void deletePost(int postId);

    @Delete
    void deletePost(PostDetails postDetails);
    //endregion

    //region Insertion queries
    @Insert
    ArrayList<Long> insertAll(ArrayList<PostDetails> postDetails);

    @Insert(onConflict = REPLACE)
    long insertPost(PostDetails postDetails);
    //endregion
}
