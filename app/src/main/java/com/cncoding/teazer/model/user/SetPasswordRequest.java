package com.cncoding.teazer.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by amit on 23/11/17.
 */

public class SetPasswordRequest {

    @SerializedName("new_password")
    @Expose
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public SetPasswordRequest(String newPassword) {
        this.newPassword = newPassword;
    }
}
