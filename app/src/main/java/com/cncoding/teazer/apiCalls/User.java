package com.cncoding.teazer.apiCalls;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * Created by Prem $ on 10/4/2017.
 */

public class User {

    private int userId;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private long phoneNumber;
    private int countryCode;
    private boolean isActive;
    private int accountType;
    private String createdAt;
    private String updatedAt;
    private boolean hasProfilePicture;
    private ProfilePicture profilePicture;
    private long followers;
    private int followings;
    private int total_videos;

    private String password;
    private String authToken;
    private String deviceId;
    private int deviceType;

    /**
     * Setup complete user profile
     */
    public User(int userId, String userName, String firstName, String lastName, String email,
                long phoneNumber, int countryCode, boolean isActive, int accountType,
                String createdAt, String updatedAt, boolean hasProfilePicture, ProfilePicture profilePicture,
                long followers, int followings, int total_videos) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
        this.isActive = isActive;
        this.accountType = accountType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.hasProfilePicture = hasProfilePicture;
        this.profilePicture = profilePicture;
        this.followers = followers;
        this.followings = followings;
        this.total_videos = total_videos;
    }

    /**
     * Setup user profile for signup
     */
    public User(String firstName, String lastName, String userName, String email, String password,
                long phoneNumber, int countryCode, @Nullable String authToken, String deviceId, int deviceType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
        this.authToken = authToken;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getAccountType() {
        return accountType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public boolean isHasProfilePicture() {
        return hasProfilePicture;
    }

    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    public long getFollowers() {
        return followers;
    }

    public int getFollowings() {
        return followings;
    }

    public int getTotal_videos() {
        return total_videos;
    }

    public String getPassword() {
        return password;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public String getUserSignupDetailsInJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_name", getUserName());
            jsonObject.put("first_name", getFirstName());
            jsonObject.put("last_name", getLastName());
            jsonObject.put("email", getEmail());
            jsonObject.put("password", getPassword());
            jsonObject.put("phone_number", getPhoneNumber());
            jsonObject.put("country_code", getCountryCode());
            jsonObject.put("fcm_token", getAuthToken());
            jsonObject.put("device_id", getDeviceId());
            jsonObject.put("device_type", getDeviceType());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject.toString();
    }

    public String getUserSignInDetailsInJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_name", getUserName());
            jsonObject.put("password", getPassword());
            jsonObject.put("fcm_token", getAuthToken());
            jsonObject.put("device_id", getDeviceId());
            jsonObject.put("device_type", getDeviceType());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject.toString();
    }

    private class ProfilePicture {
        int pictureId;
        String profileImagePath;
        String thumbImagePath;

        public ProfilePicture(int pictureId, String profileImagePath, String thumbImagePath) {
            this.pictureId = pictureId;
            this.profileImagePath = profileImagePath;
            this.thumbImagePath = thumbImagePath;
        }

        public int getPictureId() {
            return pictureId;
        }

        public String getProfileImagePath() {
            return profileImagePath;
        }

        public String getThumbImagePath() {
            return thumbImagePath;
        }
    }
}
