package com.cncoding.teazer.data.room.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.cncoding.teazer.model.post.PostDetails;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 *
 * Created by Prem $ on 1/18/2018.
 */

@Dao
public interface PostDetailsDao {

    //region Getter queries
    @Query("SELECT * FROM PostDetails")
    LiveData<List<PostDetails>> getAllPosts();

    @Query("SELECT * FROM PostDetails WHERE postId LIKE :postId")
    LiveData<List<PostDetails>> getPost(int postId);

//    @Query("SELECT postOwner FROM PostDetails WHERE postId = :postId LIMIT 1")
//    LiveData<List<MiniProfile>> getPostOwner(int postId);

//    @Query("SELECT medias FROM PostDetails WHERE postId = :postId LIMIT 1")
//    LiveData<List<ArrayList<Medias>>> getPostMedia(int postId);

//    @Query("SELECT checkIn FROM PostDetails WHERE postId = :postId LIMIT 1")
//    List<CheckIn> getPostCheckIn(int postId);
    //endregion

    //region Update queries
    @Query("UPDATE PostDetails SET likes = likes + 1")
    void likePost();

    @Query("UPDATE PostDetails SET likes = likes - 1")
    void dislikePost();

//    void incrementViews();
    //endregion

    //region Deletion queries
    @Query("DELETE FROM PostDetails WHERE postId = :postId")
    void deletePost(int postId);

    @Delete
    void deletePost(PostDetails postDetails);
    //endregion

    //region Insertion queries
    @Insert(onConflict = REPLACE)
    List<Long> insertAll(List<PostDetails> postDetails);

    @Insert(onConflict = REPLACE)
    long insertPost(PostDetails postDetails);
    //endregion
}
