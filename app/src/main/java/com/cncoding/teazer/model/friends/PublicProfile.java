package com.cncoding.teazer.model.friends;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.base.Category;
import com.cncoding.teazer.model.base.Medias;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 *
 * Created by farazhabib on 13/11/17.
 */

public class PublicProfile implements Parcelable {
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone_number")
    @Expose
    private Long phoneNumber;
    @SerializedName("country_code")
    @Expose
    private Integer countryCode;
    @SerializedName("gender")
    @Expose
    private Integer gender;
    @SerializedName("is_active")
    @Expose
    private Boolean isActive;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("account_type")
    @Expose
    private Integer accountType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("has_profile_media")
    @Expose
    private Boolean hasProfileMedia;
    @SerializedName("profile_media")
    @Expose
    private Medias profileMedia;
    @SerializedName("categories")
    @Expose
    private ArrayList<Category> categories = null;

    protected PublicProfile(Parcel in) {
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        userName = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        if (in.readByte() == 0) {
            phoneNumber = null;
        } else {
            phoneNumber = in.readLong();
        }
        if (in.readByte() == 0) {
            countryCode = null;
        } else {
            countryCode = in.readInt();
        }
        if (in.readByte() == 0) {
            gender = null;
        } else {
            gender = in.readInt();
        }
        byte tmpIsActive = in.readByte();
        isActive = tmpIsActive == 0 ? null : tmpIsActive == 1;
        description = in.readString();
        if (in.readByte() == 0) {
            accountType = null;
        } else {
            accountType = in.readInt();
        }
        createdAt = in.readString();
        updatedAt = in.readString();
        byte tmpHasProfileMedia = in.readByte();
        hasProfileMedia = tmpHasProfileMedia == 0 ? null : tmpHasProfileMedia == 1;
    }

    public static final Creator<PublicProfile> CREATOR = new Creator<PublicProfile>() {
        @Override
        public PublicProfile createFromParcel(Parcel in) {
            return new PublicProfile(in);
        }

        @Override
        public PublicProfile[] newArray(int size) {
            return new PublicProfile[size];
        }
    };

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Integer countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getHasProfileMedia() {
        return hasProfileMedia;
    }

    public void setHasProfileMedia(Boolean hasProfileMedia) {
        this.hasProfileMedia = hasProfileMedia;
    }

    public Medias getProfileMedia() {
        return profileMedia;
    }

    public void setProfileMedia(Medias profileMedia) {
        this.profileMedia = profileMedia;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (userId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(userId);
        }
        parcel.writeString(userName);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(email);
        if (phoneNumber == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(phoneNumber);
        }
        if (countryCode == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(countryCode);
        }
        if (gender == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(gender);
        }
        parcel.writeByte((byte) (isActive == null ? 0 : isActive ? 1 : 2));
        parcel.writeString(description);
        if (accountType == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(accountType);
        }
        parcel.writeString(createdAt);
        parcel.writeString(updatedAt);
        parcel.writeByte((byte) (hasProfileMedia == null ? 0 : hasProfileMedia ? 1 : 2));
    }
}