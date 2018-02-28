package com.cncoding.teazer.data.model.profile;

/**
 * Created by farazhabib on 30/01/18.
 */

public class UpdateFollowing {
    int userId;
    String status;

    public UpdateFollowing(int userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }
}
