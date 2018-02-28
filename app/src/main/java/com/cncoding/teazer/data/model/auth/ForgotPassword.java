package com.cncoding.teazer.data.model.auth;

/**
 *
 * Created by Prem$ on 2/9/2018.
 */

public class ForgotPassword extends BaseAuth {

    private String userName;

    public ForgotPassword(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
