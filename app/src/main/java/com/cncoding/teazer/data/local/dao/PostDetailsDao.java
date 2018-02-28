package com.cncoding.teazer.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.cncoding.teazer.data.model.base.Medias;
import com.cncoding.teazer.data.model.base.TaggedUser;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostReaction;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 *
 * Created by Prem $ on 1/18/2018.
 */

@Dao
public interface PostDetailsDao {

    //region Getter queries
    @Query("SELECT * FROM PostDetails ORDER BY createdAt DESC")
    List<PostDetails> getAllPosts();

    @Query("SELECT * FROM PostDetails WHERE postId LIKE :postId LIMIT 1")
    List<PostDetails> getPost(int postId);
    //endregion

    //region Update queries
    @Query("UPDATE PostDetails SET likes = likes + 1, canLike = 0 WHERE postId LIKE :postId")
    void likePost(int postId);

    @Query("UPDATE PostDetails SET likes = likes - 1, canLike = 1 WHERE postId LIKE :postId")
    void dislikePost(int postId);

    @Query("UPDATE PostDetails SET title = :title WHERE postId LIKE :postId")
    void updateTitle(String title, int postId);

    @Query("UPDATE PostDetails SET totalTags = :totalTags WHERE postId LIKE :postId")
    void updateTotalTagsCount(int totalTags, int postId);

    @Query("UPDATE PostDetails SET taggedUsers = :taggedUsers WHERE postId LIKE :postId")
    void updateTaggedUsers(ArrayList<TaggedUser> taggedUsers, int postId);

    @Query("UPDATE PostDetails SET totalReactions = :totalReactions, canReact = :canReact WHERE postId LIKE :postId")
    void updateTotalReactionCount(int totalReactions, boolean canReact, int postId);

    @Query("UPDATE PostDetails SET reactions = :reactions WHERE postId LIKE :postId")
    void updateReactions(ArrayList<PostReaction> reactions, int postId);

    @Update(onConflict = REPLACE)
    void updatePost(PostDetails postDetails);

    @Query("UPDATE PostDetails SET medias = :medias WHERE postId LIKE :postId")
    void incrementViews(ArrayList<Medias> medias, int postId);
    //endregion

    //region Deletion queries
    @Query("DELETE FROM PostDetails WHERE postId = :postId")
    void deletePost(int postId);

    @Delete
    void deletePost(PostDetails postDetails);

    @Query("DELETE FROM PostDetails")
    void clearTable();
    //endregion

    //region Insertion queries
    @Insert(onConflict = REPLACE)
    void insertAll(List<PostDetails> postDetails);

    @Insert(onConflict = REPLACE)
    void insertPost(PostDetails postDetails);
    //endregion
}