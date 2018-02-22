package com.cncoding.teazer.model.discover;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.cncoding.teazer.model.BaseModel;
import com.cncoding.teazer.model.base.Medias;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class Videos extends BaseModel implements Parcelable, Comparable<Videos> {
    private int post_id;
    private int posted_by;
    private int likes;
    private int views;
    private int total_reactions;
    private String title;
    private String created_at;
    private ArrayList<Medias> medias;

    public Videos(int post_id, int posted_by, int likes, int views, int total_reactions,
                  String title, String created_at, ArrayList<Medias> post_video_info) {
        this.post_id = post_id;
        this.posted_by = posted_by;
        this.likes = likes;
        this.views = views;
        this.total_reactions = total_reactions;
        this.title = title;
        this.created_at = created_at;
        this.medias = post_video_info;
    }

    protected Videos(Parcel in) {
        post_id = in.readInt();
        posted_by = in.readInt();
        likes = in.readInt();
        views = in.readInt();
        total_reactions = in.readInt();
        title = in.readString();
        created_at = in.readString();
        medias = in.createTypedArrayList(Medias.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(post_id);
        dest.writeInt(posted_by);
        dest.writeInt(likes);
        dest.writeInt(views);
        dest.writeInt(total_reactions);
        dest.writeString(title);
        dest.writeString(created_at);
        dest.writeTypedList(medias);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Videos> CREATOR = new Creator<Videos>() {
        @Override
        public Videos createFromParcel(Parcel in) {
            return new Videos(in);
        }

        @Override
        public Videos[] newArray(int size) {
            return new Videos[size];
        }
    };

    public int getPostId() {
        return post_id;
    }

    public int getPostedBy() {
        return posted_by;
    }

    public int getLikes() {
        return likes;
    }

    public int getViews() {
        return views;
    }

    public int getTotalReactions() {
        return total_reactions;
    }

    public String getTitle() {
        return title;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public ArrayList<Medias> getPostVideoInfo() {
        return medias;
    }

    @Override
    public int compareTo(@NonNull Videos videos) {
        return Integer.compare(this.post_id, videos.post_id);
    }
}