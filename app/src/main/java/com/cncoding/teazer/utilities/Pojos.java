package com.cncoding.teazer.utilities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 10/25/2017.
 */

@SuppressWarnings("WeakerAccess, unused")
public class Pojos {

    public static class Application {

        public static class ReportTypeList {
            private ArrayList<ReportType> reportTypeList;

            public ReportTypeList(ArrayList<ReportType> reportTypeList) {
                this.reportTypeList = reportTypeList;
            }

            public ArrayList<ReportType> getReportTypeList() {
                return reportTypeList;
            }

        }

        public static class CategoriesList {
            private ArrayList<Category> categoriesList;

            public CategoriesList(ArrayList<Category> categoriesList) {
                this.categoriesList = categoriesList;
            }

            public ArrayList<Category> getCategoriesList() {
                return categoriesList;
            }
        }

        public static class ReportType {
            private int report_type_id;
            private String title;
            private ArrayList<ReportType> sub_reports;

            public ReportType(int report_type_id, String title, ArrayList<ReportType> sub_reports) {
                this.report_type_id = report_type_id;
                this.title = title;
                this.sub_reports = sub_reports;
            }

            public int getReport_type_id() {
                return report_type_id;
            }

            public String getTitle() {
                return title;
            }

            public ArrayList<ReportType> getSub_reports() {
                return sub_reports;
            }
        }

    }

    public static class Authorize implements Parcelable {
        private String user_name;
        private String first_name;
        private String last_name;
        private String email;
        private String password;
        private String new_password;
        private long phone_number;
        private int country_code;
        private int otp;
        private String fcm_token;
        private String device_id;
        private int device_type;
        private String social_id;
        private int social_login_type;

        /**Proceed to second signup page after entering Name, Email and Phone number in the first page**/
        public Authorize(String first_name, String last_name, String email, int country_code, long phone_number) {
            this.first_name = first_name;
            this.last_name = last_name;
            this.email = email;
            this.country_code = country_code;
            this.phone_number = phone_number;
        }

        /**SignUp step 1**/
        public Authorize(String user_name, String first_name, String last_name, String email,
                         String password, long phone_number, int country_code) {
            this.user_name = user_name;
            this.first_name = first_name;
            this.last_name = last_name;
            this.email = email;
            this.password = password;
            this.phone_number = phone_number;
            this.country_code = country_code;
        }

        /**SignUp step 2**/
        public Authorize(String user_name, String first_name, String last_name, String email,
                         String password, long phone_number, int country_code, int otp,
                         String fcm_token, String device_id, int device_type) {
            this.fcm_token = fcm_token;
            this.device_id = device_id;
            this.device_type = device_type;
            this.user_name = user_name;
            this.first_name = first_name;
            this.last_name = last_name;
            this.email = email;
            this.password = password;
            this.phone_number = phone_number;
            this.country_code = country_code;
            this.otp = otp;
        }

        /**Login through Username/Email/Phone number and password**/
        public Authorize(String fcmToken, String deviceId, int deviceType, String username, String password) {
            this.fcm_token = fcmToken;
            this.device_id = deviceId;
            this.device_type = deviceType;
            this.user_name = username;
            this.password = password;
        }

        /**
         * Method user in first step of login through OTP,
         * OR, to reset the password by phone number, from forgot password.
         **/
        public Authorize(long phone_number, int country_code) {
            this.phone_number = phone_number;
            this.country_code = country_code;
        }

        /**
         * Second step of login through OTP.
         **/
        public Authorize(String fcmToken, String deviceId, int deviceType, long phone_number, int country_code, int otp) {
            this.fcm_token = fcmToken;
            this.device_id = deviceId;
            this.device_type = deviceType;
            this.phone_number = phone_number;
            this.country_code = country_code;
            this.otp = otp;
        }

        /**Forgot password constructor**/
        public Authorize(String user_name) {
            this.user_name = user_name;
        }

        /**
         * Reset the password by OTP received in email or phone number.
         * Send either
         * @param email or
         * @param phone_number and
         * @param country_code along with OTP and New password.
         **/
        public Authorize(String new_password, String email, long phone_number, int country_code, int otp) {
            this.new_password = new_password;
            this.email = email;
            this.phone_number = phone_number;
            this.country_code = country_code;
            this.otp = otp;
        }

        /**Social SignUp**/
        public Authorize(String fcm_token, String device_id, int device_type, String social_id,
                         int social_login_type, String email, String user_name, String first_name, String last_name) {
            this.fcm_token = fcm_token;
            this.device_id = device_id;
            this.device_type = device_type;
            this.social_id = social_id;
            this.social_login_type = social_login_type;
            this.email = email;
            this.user_name = user_name;
            this.first_name = first_name;
            this.last_name = last_name;
        }

        public String getFirstName() {
            return first_name;
        }

        public String getLastName() {
            return last_name;
        }

        public String getEmail() {
            return email;
        }

        public long getPhoneNumber() {
            return phone_number;
        }

        public int getCountryCode() {
            return country_code;
        }

        public String getUsername() {
            return user_name;
        }

        public String getPassword() {
            return password;
        }

        public int getOtp() {
            return otp;
        }

        public String getFcm_token() {
            return fcm_token;
        }

        public String getDevice_id() {
            return device_id;
        }

        public int getDevice_type() {
            return device_type;
        }

        public String getSocial_id() {
            return social_id;
        }

        public int getSocial_login_type() {
            return social_login_type;
        }

        public String getNew_password() {
            return new_password;
        }

        Authorize(Parcel in) {
            user_name = in.readString();
            first_name = in.readString();
            last_name = in.readString();
            email = in.readString();
            password = in.readString();
            phone_number = in.readLong();
            country_code = in.readInt();
            otp = in.readInt();
            fcm_token = in.readString();
            device_id = in.readString();
            device_type = in.readInt();
        }

        public static final Creator<Authorize> CREATOR = new Creator<Authorize>() {
            @Override
            public Authorize createFromParcel(Parcel in) {
                return new Authorize(in);
            }

            @Override
            public Authorize[] newArray(int size) {
                return new Authorize[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(user_name);
            parcel.writeString(first_name);
            parcel.writeString(last_name);
            parcel.writeString(email);
            parcel.writeString(password);
            parcel.writeLong(phone_number);
            parcel.writeInt(country_code);
            parcel.writeInt(otp);
            parcel.writeString(fcm_token);
            parcel.writeString(device_id);
            parcel.writeInt(device_type);
        }
    }

    public static class Friends {

        public static class CircleList {
            private boolean next_page;
            private ArrayList<MiniProfile> circles;

            public CircleList(boolean next_page, ArrayList<MiniProfile> circles) {
                this.next_page = next_page;
                this.circles = circles;
            }

            public boolean isNextPage() {
                return next_page;
            }

            public ArrayList<MiniProfile> getCircles() {
                return circles;
            }
        }

        public static class FollowingsList {
            private boolean next_page;
            private ArrayList<MiniProfile> followings;

            public FollowingsList(boolean next_page, ArrayList<MiniProfile> followings) {
                this.next_page = next_page;
                this.followings = followings;
            }

            public boolean isNextPage() {
                return next_page;
            }

            public ArrayList<MiniProfile> getFollowings() {
                return followings;
            }
        }

        public static class FollowersList {
            private boolean next_page;
            private ArrayList<MiniProfile> followers;

            public FollowersList(boolean next_page, ArrayList<MiniProfile> followers) {
                this.next_page = next_page;
                this.followers = followers;
            }

            public boolean isNextPage() {
                return next_page;
            }

            public ArrayList<MiniProfile> getFollowers() {
                return followers;
            }
        }

        public static class UsersList {
            boolean next_page;
            ArrayList<MiniProfile> users;

            public UsersList(boolean next_page, ArrayList<MiniProfile> users) {
                this.next_page = next_page;
                this.users = users;
            }

            public boolean isNextPage() {
                return next_page;
            }

            public ArrayList<MiniProfile> getUsers() {
                return users;
            }
        }
    }

    public static class React {

        public static class UserReactionsList {
            private boolean next_page;
            private ArrayList<UserReactions> reactions;

            public UserReactionsList(boolean next_page, ArrayList<UserReactions> reactions) {
                this.next_page = next_page;
                this.reactions = reactions;
            }

            public boolean isNextPage() {
                return next_page;
            }

            public ArrayList<UserReactions> getReactions() {
                return reactions;
            }
        }

        public static class HiddenReactionsList {
            private boolean next_page;
            private ArrayList<HiddenReactions> reactions;

            public HiddenReactionsList(boolean next_page, ArrayList<HiddenReactions> reactions) {
                this.next_page = next_page;
                this.reactions = reactions;
            }

            public boolean isNextPage() {
                return next_page;
            }

            public ArrayList<HiddenReactions> getReactions() {
                return reactions;
            }
        }

        public static class UserReactions implements Parcelable {
            private int post_id;
            private int react_id;
            private String title;
            private int reacted_by;
            private int likes;
            private int views;
            private boolean can_like;
            private ProfileMedia profile_media;
            private ReactionMediaDetail media_detail;
            private MiniProfile post_owner;
            private boolean can_delete;

            public UserReactions(int post_id, int react_id, String title, int reacted_by, int likes, int views, boolean can_like,
                                 ProfileMedia profile_media, ReactionMediaDetail media_detail, MiniProfile post_owner, boolean can_delete) {
                this.post_id = post_id;
                this.react_id = react_id;
                this.title = title;
                this.reacted_by = reacted_by;
                this.likes = likes;
                this.views = views;
                this.can_like = can_like;
                this.profile_media = profile_media;
                this.media_detail = media_detail;
                this.post_owner = post_owner;
                this.can_delete = can_delete;
            }

            public int getPostId() {
                return post_id;
            }

            public int getReactId() {
                return react_id;
            }

            public String getTitle() {
                return title;
            }

            public int getReactedBy() {
                return reacted_by;
            }

            public int getLikes() {
                return likes;
            }

            public int getViews() {
                return views;
            }

            public boolean canLike() {
                return can_like;
            }

            public ProfileMedia getProfileMedia() {
                return profile_media;
            }

            public ReactionMediaDetail getMediaDetail() {
                return media_detail;
            }

            public MiniProfile getPostOwner() {
                return post_owner;
            }

            public boolean isCanDelete() {
                return can_delete;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeInt(post_id);
                parcel.writeInt(react_id);
                parcel.writeInt(reacted_by);
                parcel.writeInt(likes);
                parcel.writeInt(views);
                parcel.writeByte((byte) (can_like ? 1 : 0));
                parcel.writeParcelable(profile_media, i);
                parcel.writeParcelable(media_detail, i);
                parcel.writeParcelable(post_owner, i);
                parcel.writeByte((byte) (can_delete ? 1 : 0));
            }

            protected UserReactions(Parcel in) {
                post_id = in.readInt();
                react_id = in.readInt();
                reacted_by = in.readInt();
                likes = in.readInt();
                views = in.readInt();
                can_like = in.readByte() != 0;
                profile_media = in.readParcelable(ProfileMedia.class.getClassLoader());
                media_detail = in.readParcelable(ReactionMediaDetail.class.getClassLoader());
                post_owner = in.readParcelable(MiniProfile.class.getClassLoader());
                can_delete = in.readByte() != 0;
            }

            public static final Creator<UserReactions> CREATOR = new Creator<UserReactions>() {
                @Override
                public UserReactions createFromParcel(Parcel in) {
                    return new UserReactions(in);
                }

                @Override
                public UserReactions[] newArray(int size) {
                    return new UserReactions[size];
                }
            };
        }

        public static class HiddenReactions implements Parcelable {
            private int react_id;
            private int post_id;
            private String title;
            private int post_owner_id;
            private int likes;
            private int views;
            private boolean can_like;
            private boolean can_delete;
            private ReactionMediaDetail media_detail;
            private MiniProfile react_owner;
            private String reacted_at;

            public HiddenReactions(int react_id, int post_id, String title, int post_owner_id, int likes, int views, boolean can_like,
                                   boolean can_delete, ReactionMediaDetail media_detail, MiniProfile react_owner, String reacted_at) {
                this.react_id = react_id;
                this.post_id = post_id;
                this.title = title;
                this.post_owner_id = post_owner_id;
                this.likes = likes;
                this.views = views;
                this.can_like = can_like;
                this.can_delete = can_delete;
                this.media_detail = media_detail;
                this.react_owner = react_owner;
                this.reacted_at = reacted_at;
            }

            public int getReact_id() {
                return react_id;
            }

            public int getPost_id() {
                return post_id;
            }

            public String getTitle() {
                return title;
            }

            public int getPostOwnerId() {
                return post_owner_id;
            }

            public int getLikes() {
                return likes;
            }

            public int getViews() {
                return views;
            }

            public boolean canLike() {
                return can_like;
            }

            public boolean canDelete() {
                return can_delete;
            }

            public ReactionMediaDetail getMediaDetail() {
                return media_detail;
            }

            public MiniProfile getReactOwner() {
                return react_owner;
            }

            public String getReactedAt() {
                return reacted_at;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeInt(react_id);
                parcel.writeInt(post_id);
                parcel.writeInt(post_owner_id);
                parcel.writeInt(likes);
                parcel.writeInt(views);
                parcel.writeByte((byte) (can_like ? 1 : 0));
                parcel.writeByte((byte) (can_delete ? 1 : 0));
                parcel.writeParcelable(media_detail, i);
                parcel.writeParcelable(react_owner, i);
                parcel.writeString(reacted_at);
            }

            protected HiddenReactions(Parcel in) {
                react_id = in.readInt();
                post_id = in.readInt();
                post_owner_id = in.readInt();
                likes = in.readInt();
                views = in.readInt();
                can_like = in.readByte() != 0;
                can_delete = in.readByte() != 0;
                media_detail = in.readParcelable(ReactionMediaDetail.class.getClassLoader());
                react_owner = in.readParcelable(MiniProfile.class.getClassLoader());
                reacted_at = in.readString();
            }

            public static final Creator<HiddenReactions> CREATOR = new Creator<HiddenReactions>() {
                @Override
                public HiddenReactions createFromParcel(Parcel in) {
                    return new HiddenReactions(in);
                }

                @Override
                public HiddenReactions[] newArray(int size) {
                    return new HiddenReactions[size];
                }
            };
        }

        public static class ReportReaction {
            private int react_id;
            private int report_type_id;

            public ReportReaction(int react_id, int report_type_id) {
                this.react_id = react_id;
                this.report_type_id = report_type_id;
            }
        }
    }

    public static class Post {

        public static class PostList {
            private boolean next_page;
            private ArrayList<PostDetails> posts;

            public PostList(boolean next_page, ArrayList<PostDetails> posts) {
                this.next_page = next_page;
                this.posts = posts;
            }

            public boolean isNextPage() {
                return next_page;
            }

            public ArrayList<PostDetails> getPosts() {
                return posts;
            }

            public void add(PostDetails postDetails) {
                posts.add(postDetails);
            }
        }

        public static class TaggedUsersList {
            private boolean next_page;
            private ArrayList<TaggedUser> tagged_users;

            public TaggedUsersList(boolean next_page, ArrayList<TaggedUser> tagged_users) {
                this.next_page = next_page;
                this.tagged_users = tagged_users;
            }

            public boolean isNextPage() {
                return next_page;
            }

            public ArrayList<TaggedUser> getTagged_users() {
                return tagged_users;
            }
        }

        public static class PostReactionsList {
            private boolean next_page;
            private ArrayList<PostReaction> reactions;

            public PostReactionsList(boolean next_page, ArrayList<PostReaction> reactions) {
                this.next_page = next_page;
                this.reactions = reactions;
            }

            public boolean isNextPage() {
                return next_page;
            }

            public ArrayList<PostReaction> getReactions() {
                return reactions;
            }
        }

        public static class PostDetails implements Parcelable{
            private int post_id;
            private int posted_by;
            private int likes;
            private int total_reactions;
            private boolean has_checkin;
            private String title;
            private boolean can_react;
            private boolean can_like;
            private boolean can_delete;
            private MiniProfile post_owner;
            private String created_at;                  //use DateTime.Now.ToString("yyyy-MM-ddThh:mm:sszzz");
            private CheckIn check_in;
            private ArrayList<Medias> medias;
            private ArrayList<Category> categories;

            public PostDetails(int post_id, int posted_by, int likes, int total_reactions, boolean has_checkin,
                               String title, boolean can_react, boolean can_like, boolean can_delete, MiniProfile post_owner,
                               String created_at, CheckIn check_in, ArrayList<Medias> medias, ArrayList<Category> categories) {
                this.post_id = post_id;
                this.posted_by = posted_by;
                this.likes = likes;
                this.total_reactions = total_reactions;
                this.has_checkin = has_checkin;
                this.title = title;
                this.can_react = can_react;
                this.can_like = can_like;
                this.can_delete = can_delete;
                this.post_owner = post_owner;
                this.created_at = created_at;
                this.check_in = check_in;
                this.medias = medias;
                this.categories = categories;
            }

            public int getPostId() {
                return post_id;
            }

            public int getPostedBy() {
                return posted_by;
            }

            public int getLikes() {
                return likes;
            }

            public int getTotalReactions() {
                return total_reactions;
            }

            public boolean hasCheckin() {
                return has_checkin;
            }

            public String getTitle() {
                return title;
            }

            public boolean canReact() {
                return can_react;
            }

            public boolean canLike() {
                return can_like;
            }

            public boolean canDelete() {
                return can_delete;
            }

            public MiniProfile getPostOwner() {
                return post_owner;
            }

            public String getCreatedAt() {
                return created_at;
            }

            public CheckIn getCheckIn() {
                return check_in;
            }

            public ArrayList<Medias> getMedias() {
                return medias;
            }

            public ArrayList<Category> getCategories() {
                return categories;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeInt(post_id);
                parcel.writeInt(posted_by);
                parcel.writeInt(likes);
                parcel.writeInt(total_reactions);
                parcel.writeByte((byte) (has_checkin ? 1 : 0));
                parcel.writeString(title);
                parcel.writeByte((byte) (can_react ? 1 : 0));
                parcel.writeByte((byte) (can_like ? 1 : 0));
                parcel.writeByte((byte) (can_delete ? 1 : 0));
                parcel.writeParcelable(post_owner, i);
                parcel.writeString(created_at);
                parcel.writeParcelable(check_in, i);
                parcel.writeTypedList(medias);
                parcel.writeTypedList(categories);
            }

            protected PostDetails(Parcel in) {
                post_id = in.readInt();
                posted_by = in.readInt();
                likes = in.readInt();
                total_reactions = in.readInt();
                has_checkin = in.readByte() != 0;
                title = in.readString();
                can_react = in.readByte() != 0;
                can_like = in.readByte() != 0;
                can_delete = in.readByte() != 0;
                post_owner = in.readParcelable(MiniProfile.class.getClassLoader());
                created_at = in.readString();
                check_in = in.readParcelable(CheckIn.class.getClassLoader());
                medias = in.createTypedArrayList(Medias.CREATOR);
                categories = in.createTypedArrayList(Category.CREATOR);
            }

            public static final Creator<PostDetails> CREATOR = new Creator<PostDetails>() {
                @Override
                public PostDetails createFromParcel(Parcel in) {
                    return new PostDetails(in);
                }

                @Override
                public PostDetails[] newArray(int size) {
                    return new PostDetails[size];
                }
            };
        }

        public static class PostReaction implements Parcelable {
            private int react_id;
            private int post_id;
            private String react_title;
            private int post_owner_id;
            private int likes;
            private int views;
            private boolean can_like;
            private boolean can_delete;
            private ReactionMediaDetail media_detail;
            private MiniProfile react_owner;
            private String reacted_at;

            public PostReaction(int react_id, int post_id, String react_title, int post_owner_id, int likes, int views, boolean can_like,
                                boolean can_delete, ReactionMediaDetail media_detail, MiniProfile react_owner, String reacted_at) {
                this.react_id = react_id;
                this.post_id = post_id;
                this.react_title = react_title;
                this.post_owner_id = post_owner_id;
                this.likes = likes;
                this.views = views;
                this.can_like = can_like;
                this.can_delete = can_delete;
                this.media_detail = media_detail;
                this.react_owner = react_owner;
                this.reacted_at = reacted_at;
            }

            public int getReactId() {
                return react_id;
            }

            public int getPostId() {
                return post_id;
            }

            public String getReact_title() {
                return react_title;
            }

            public int getPostOwnerId() {
                return post_owner_id;
            }

            public int getLikes() {
                return likes;
            }

            public int getViews() {
                return views;
            }

            public boolean canLike() {
                return can_like;
            }

            public boolean canDelete() {
                return can_delete;
            }

            public ReactionMediaDetail getMediaDetail() {
                return media_detail;
            }

            public MiniProfile getReactOwner() {
                return react_owner;
            }

            public String getReactedAt() {
                return reacted_at;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeInt(react_id);
                parcel.writeInt(post_id);
                parcel.writeInt(post_owner_id);
                parcel.writeInt(likes);
                parcel.writeInt(views);
                parcel.writeByte((byte) (can_like ? 1 : 0));
                parcel.writeByte((byte) (can_delete ? 1 : 0));
                parcel.writeParcelable(media_detail, i);
                parcel.writeParcelable(react_owner, i);
                parcel.writeString(reacted_at);
            }

            protected PostReaction(Parcel in) {
                react_id = in.readInt();
                post_id = in.readInt();
                post_owner_id = in.readInt();
                likes = in.readInt();
                views = in.readInt();
                can_like = in.readByte() != 0;
                can_delete = in.readByte() != 0;
                media_detail = in.readParcelable(ReactionMediaDetail.class.getClassLoader());
                react_owner = in.readParcelable(MiniProfile.class.getClassLoader());
                reacted_at = in.readString();
            }

            public static final Creator<PostReaction> CREATOR = new Creator<PostReaction>() {
                @Override
                public PostReaction createFromParcel(Parcel in) {
                    return new PostReaction(in);
                }

                @Override
                public PostReaction[] newArray(int size) {
                    return new PostReaction[size];
                }
            };
        }

        public static class ReportPost {
            private int post_id;
            private int report_type_id;

            public ReportPost(int post_id, int report_type_id) {
                this.post_id = post_id;
                this.report_type_id = report_type_id;
            }
        }
    }



    public static class User {

        public static class Profile implements Parcelable {
            private UserProfile user_profile;
            private int followers;
            private int followings;
            private int total_videos;

            public Profile(UserProfile user_profile, int followers, int followings, int total_videos) {
                this.user_profile = user_profile;
                this.followers = followers;
                this.followings = followings;
                this.total_videos = total_videos;
            }

            public UserProfile getUserProfile() {
                return user_profile;
            }

            public int getFollowers() {
                return followers;
            }

            public int getFollowings() {
                return followings;
            }

            public int getTotalVideos() {
                return total_videos;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeParcelable(user_profile, i);
                parcel.writeInt(followers);
                parcel.writeInt(followings);
                parcel.writeInt(total_videos);
            }

            protected Profile(Parcel in) {
                user_profile = in.readParcelable(UserProfile.class.getClassLoader());
                followers = in.readInt();
                followings = in.readInt();
                total_videos = in.readInt();
            }

            public static final Creator<Profile> CREATOR = new Creator<Profile>() {
                @Override
                public Profile createFromParcel(Parcel in) {
                    return new Profile(in);
                }

                @Override
                public Profile[] newArray(int size) {
                    return new Profile[size];
                }
            };
        }

        public static class UserProfile implements Parcelable {
            private String user_id;
            private String user_name;
            private String first_name;
            private String last_name;
            private String email;
            private long phone_number;
            private int country_code;
            private boolean is_active;
            private int account_type;
            private String created_at;
            private String updated_at;
            private boolean has_profile_media;
            private ProfileMedia profile_media;
            private ArrayList<Category> categories;
            private String password;

            public UserProfile(String user_id, String user_name, String first_name, String last_name, String email, long phone_number, int country_code,
                               String password, boolean is_active, int account_type, String created_at, String updated_at, boolean has_profile_media,
                               ProfileMedia profile_media, ArrayList<Category> categories, int followers, int followings, int total_videos) {
                this.user_id = user_id;
                this.user_name = user_name;
                this.first_name = first_name;
                this.last_name = last_name;
                this.email = email;
                this.phone_number = phone_number;
                this.country_code = country_code;
                this.is_active = is_active;
                this.account_type = account_type;
                this.created_at = created_at;
                this.updated_at = updated_at;
                this.has_profile_media = has_profile_media;
                this.profile_media = profile_media;
                this.categories = categories;
                this.password = password;
            }

            public UserProfile(String user_name, String first_name, String last_name, String email, long phone_number, int country_code) {
                this.user_name = user_name;
                this.first_name = first_name;
                this.last_name = last_name;
                this.email = email;
                this.phone_number = phone_number;
                this.country_code = country_code;
            }

            public UserProfile(String email) {
                this.email = email;
            }

            public String getUserId() {
                return user_id;
            }

            public String getUsername() {
                return user_name;
            }

            public String getFirstName() {
                return first_name;
            }

            public String getLastName() {
                return last_name;
            }

            public String getEmail() {
                return email;
            }

            public long getPhoneNumber() {
                return phone_number;
            }

            public int getCountryCode() {
                return country_code;
            }

            public boolean isActive() {
                return is_active;
            }

            public int getAccountType() {
                return account_type;
            }

            public String getCreatedAt() {
                return created_at;
            }

            public String getUpdatedAt() {
                return updated_at;
            }

            public boolean hasProfileMedia() {
                return has_profile_media;
            }

            public ProfileMedia getProfileMedia() {
                return profile_media;
            }

            public ArrayList<Category> getCategories() {
                return categories;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeString(user_id);
                parcel.writeString(user_name);
                parcel.writeString(first_name);
                parcel.writeString(last_name);
                parcel.writeString(email);
                parcel.writeLong(phone_number);
                parcel.writeInt(country_code);
                parcel.writeByte((byte) (is_active ? 1 : 0));
                parcel.writeInt(account_type);
                parcel.writeString(created_at);
                parcel.writeString(updated_at);
                parcel.writeByte((byte) (has_profile_media ? 1 : 0));
                parcel.writeParcelable(profile_media, i);
                parcel.writeTypedList(categories);
            }

            protected UserProfile(Parcel in) {
                user_id = in.readString();
                user_name = in.readString();
                first_name = in.readString();
                last_name = in.readString();
                email = in.readString();
                phone_number = in.readLong();
                country_code = in.readInt();
                is_active = in.readByte() != 0;
                account_type = in.readInt();
                created_at = in.readString();
                updated_at = in.readString();
                has_profile_media = in.readByte() != 0;
                profile_media = in.readParcelable(ProfileMedia.class.getClassLoader());
                categories = in.createTypedArrayList(Category.CREATOR);
            }

            public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
                @Override
                public UserProfile createFromParcel(Parcel in) {
                    return new UserProfile(in);
                }

                @Override
                public UserProfile[] newArray(int size) {
                    return new UserProfile[size];
                }
            };
        }

        public static class UpdateProfile implements Parcelable {
            private String first_name;
            private String last_name;
            private String email;
            private long phone_number;
            private int country_code;

            public UpdateProfile(String first_name, String last_name, String email, long phone_number, int country_code) {
                this.first_name = first_name;
                this.last_name = last_name;
                this.email = email;
                this.phone_number = phone_number;
                this.country_code = country_code;
            }

            public String getFirst_name() {
                return first_name;
            }

            public String getLast_name() {
                return last_name;
            }

            public String getEmail() {
                return email;
            }

            public long getPhone_number() {
                return phone_number;
            }

            public int getCountry_code() {
                return country_code;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(first_name);
                dest.writeString(last_name);
                dest.writeString(email);
                dest.writeLong(phone_number);
                dest.writeInt(country_code);
            }

            protected UpdateProfile(Parcel in) {
                first_name = in.readString();
                last_name = in.readString();
                email = in.readString();
                phone_number = in.readLong();
                country_code = in.readInt();
            }

            public static final Creator<UpdateProfile> CREATOR = new Creator<UpdateProfile>() {
                @Override
                public UpdateProfile createFromParcel(Parcel in) {
                    return new UpdateProfile(in);
                }

                @Override
                public UpdateProfile[] newArray(int size) {
                    return new UpdateProfile[size];
                }
            };
        }

        public static class UpdatePassword implements Parcelable {
            private String old_password;
            private String new_password;

            public UpdatePassword(String old_password, String new_password) {
                this.old_password = old_password;
                this.new_password = new_password;
            }

            public String getOld_password() {
                return old_password;
            }

            public String getNew_password() {
                return new_password;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeString(old_password);
                parcel.writeString(new_password);
            }

            protected UpdatePassword(Parcel in) {
                old_password = in.readString();
                new_password = in.readString();
            }

            public static final Creator<UpdatePassword> CREATOR = new Creator<UpdatePassword>() {
                @Override
                public UpdatePassword createFromParcel(Parcel in) {
                    return new UpdatePassword(in);
                }

                @Override
                public UpdatePassword[] newArray(int size) {
                    return new UpdatePassword[size];
                }
            };
        }

        public static class UpdateCategories implements Parcelable {
            private String categories;

            public UpdateCategories(String categories) {
                this.categories = categories;
            }

            public String getCategories() {
                return categories;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeString(categories);
            }

            protected UpdateCategories(Parcel in) {
                categories = in.readString();
            }

            public static final Creator<UpdateCategories> CREATOR = new Creator<UpdateCategories>() {
                @Override
                public UpdateCategories createFromParcel(Parcel in) {
                    return new UpdateCategories(in);
                }

                @Override
                public UpdateCategories[] newArray(int size) {
                    return new UpdateCategories[size];
                }
            };
        }

        public static class NotificationsList {
            private ArrayList<Notifications> notifications;
            private boolean next_page;

            public NotificationsList(ArrayList<Notifications> notifications, boolean next_page) {
                this.notifications = notifications;
                this.next_page = next_page;
            }

            public ArrayList<Notifications> getNotifications() {
                return notifications;
            }

            public boolean isNextPage() {
                return next_page;
            }
        }

        public static class Notifications implements Parcelable {
            private int notification_id;
            private int notification_type;
            private int source_id;
            private String meta_data;
            private String title;
            private String message;
            private String created_at;
            private boolean has_profile_media;
            private ProfileMedia profile_media;

            public Notifications(int notification_id, int notification_type, int source_id, String meta_data, String title,
                                 String message, String created_at, boolean has_profile_media, ProfileMedia profile_media) {
                this.notification_id = notification_id;
                this.notification_type = notification_type;
                this.source_id = source_id;
                this.meta_data = meta_data;
                this.title = title;
                this.message = message;
                this.created_at = created_at;
                this.has_profile_media = has_profile_media;
                this.profile_media = profile_media;
            }

            public int getNotificationId() {
                return notification_id;
            }

            public int getNotificationType() {
                return notification_type;
            }

            public int getSourceId() {
                return source_id;
            }

            public String getMetaData() {
                return meta_data;
            }

            public String getTitle() {
                return title;
            }

            public String getMessage() {
                return message;
            }

            public String getCreatedAt() {
                return created_at;
            }

            public boolean hasProfileMedia() {
                return has_profile_media;
            }

            public ProfileMedia getProfileMedia() {
                return profile_media;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeInt(notification_id);
                parcel.writeInt(notification_type);
                parcel.writeInt(source_id);
                parcel.writeString(meta_data);
                parcel.writeString(title);
                parcel.writeString(message);
                parcel.writeString(created_at);
                parcel.writeByte((byte) (has_profile_media ? 1 : 0));
                parcel.writeParcelable(profile_media, i);
            }

            protected Notifications(Parcel in) {
                notification_id = in.readInt();
                notification_type = in.readInt();
                source_id = in.readInt();
                meta_data = in.readString();
                title = in.readString();
                message = in.readString();
                created_at = in.readString();
                has_profile_media = in.readByte() != 0;
                profile_media = in.readParcelable(ProfileMedia.class.getClassLoader());
            }

            public static final Creator<Notifications> CREATOR = new Creator<Notifications>() {
                @Override
                public Notifications createFromParcel(Parcel in) {
                    return new Notifications(in);
                }

                @Override
                public Notifications[] newArray(int size) {
                    return new Notifications[size];
                }
            };
        }

        public static class ReportUser implements Parcelable {
            private int user_id;
            private int report_type_id;

            public ReportUser(int user_id, int report_type_id) {
                this.user_id = user_id;
                this.report_type_id = report_type_id;
            }

            public int getUserId() {
                return user_id;
            }

            public int getReportTypeId() {
                return report_type_id;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeInt(user_id);
                parcel.writeInt(report_type_id);
            }

            protected ReportUser(Parcel in) {
                user_id = in.readInt();
                report_type_id = in.readInt();
            }

            public static final Creator<ReportUser> CREATOR = new Creator<ReportUser>() {
                @Override
                public ReportUser createFromParcel(Parcel in) {
                    return new ReportUser(in);
                }

                @Override
                public ReportUser[] newArray(int size) {
                    return new ReportUser[size];
                }
            };
        }
    }

    public static class MiniProfile implements Parcelable {
        int user_id;
        String user_name;
        String first_name;
        String last_name;
        boolean following;
        boolean follower;
        boolean has_profile_media;
        ProfileMedia profile_media;

        public MiniProfile(int user_id, String user_name, String first_name, String last_name,
                           boolean following, boolean follower, boolean has_profile_media, ProfileMedia profile_media) {
            this.user_id = user_id;
            this.user_name = user_name;
            this.first_name = first_name;
            this.last_name = last_name;
            this.following = following;
            this.follower = follower;
            this.has_profile_media = has_profile_media;
            this.profile_media = profile_media;
        }

        public int getUserId() {
            return user_id;
        }

        public String getUserName() {
            return user_name;
        }

        public String getFirstName() {
            return first_name;
        }

        public String getLastName() {
            return last_name;
        }

        public boolean isFollowing() {
            return following;
        }

        public boolean isFollower() {
            return follower;
        }

        public boolean hasProfileMedia() {
            return has_profile_media;
        }

        public ProfileMedia getProfileMedia() {
            return profile_media;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(user_id);
            parcel.writeString(user_name);
            parcel.writeString(first_name);
            parcel.writeString(last_name);
            parcel.writeByte((byte) (following ? 1 : 0));
            parcel.writeByte((byte) (follower ? 1 : 0));
            parcel.writeByte((byte) (has_profile_media ? 1 : 0));
            parcel.writeParcelable(profile_media, i);
        }

        protected MiniProfile(Parcel in) {
            user_id = in.readInt();
            user_name = in.readString();
            first_name = in.readString();
            last_name = in.readString();
            following = in.readByte() != 0;
            follower = in.readByte() != 0;
            has_profile_media = in.readByte() != 0;
            profile_media = in.readParcelable(ProfileMedia.class.getClassLoader());
        }

        public static final Creator<MiniProfile> CREATOR = new Creator<MiniProfile>() {
            @Override
            public MiniProfile createFromParcel(Parcel in) {
                return new MiniProfile(in);
            }

            @Override
            public MiniProfile[] newArray(int size) {
                return new MiniProfile[size];
            }
        };
    }

    public static class TaggedUser extends MiniProfile implements Parcelable {
        private int tag_id;

        public TaggedUser(int tag_id, int user_id, String user_name, String first_name, String last_name, boolean following,
                          boolean follower, boolean has_profile_media, ProfileMedia profile_media) {
            super(user_id, user_name, first_name, last_name, following, follower, has_profile_media, profile_media);
            this.tag_id = tag_id;
        }

        protected TaggedUser(Parcel in) {
            super(in);
        }

        public int getTagId() {
            return tag_id;
        }
    }

    public static class ReactionMediaDetail implements Parcelable{
        private int media_id;
        private String react_media_url;
        private String react_thumb_url;
        private String react_duration;
        private Dimension media_dimension;
        private boolean react_is_image;

        public ReactionMediaDetail(int media_id, String react_media_url, String react_thumb_url, String react_duration, Dimension media_dimension, boolean react_is_image) {
            this.media_id = media_id;
            this.react_media_url = react_media_url;
            this.react_thumb_url = react_thumb_url;
            this.react_duration = react_duration;
            this.media_dimension = media_dimension;
            this.react_is_image = react_is_image;
        }

        public int getMediaId() {
            return media_id;
        }

        public String getMediaUrl() {
            return react_media_url;
        }

        public String getThumbUrl() {
            return react_thumb_url;
        }

        public String getReactDuration() {
            return react_duration;
        }

        public Dimension getReactDimension() {
            return media_dimension;
        }

        public boolean hasImage() {
            return react_is_image;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(media_id);
            parcel.writeString(react_media_url);
            parcel.writeString(react_thumb_url);
            parcel.writeString(react_duration);
            parcel.writeParcelable(media_dimension, i);
            parcel.writeByte((byte) (react_is_image ? 1 : 0));
        }

        protected ReactionMediaDetail(Parcel in) {
            media_id = in.readInt();
            react_media_url = in.readString();
            react_thumb_url = in.readString();
            react_duration = in.readString();
            media_dimension = in.readParcelable(Dimension.class.getClassLoader());
            react_is_image = in.readByte() != 0;
        }

        public static final Creator<ReactionMediaDetail> CREATOR = new Creator<ReactionMediaDetail>() {
            @Override
            public ReactionMediaDetail createFromParcel(Parcel in) {
                return new ReactionMediaDetail(in);
            }

            @Override
            public ReactionMediaDetail[] newArray(int size) {
                return new ReactionMediaDetail[size];
            }
        };
    }

    public static class ProfileMedia implements Parcelable {
        private String media_url;
        private String thumb_url;
        private String duration;
        private Dimension media_dimension;
        private boolean is_image;

        public ProfileMedia(String media_url, String thumb_url, String duration, Dimension dimension, boolean is_image) {
            this.media_url = media_url;
            this.thumb_url = thumb_url;
            this.duration = duration;
            this.media_dimension = dimension;
            this.is_image = is_image;
        }

        public String getMediaUrl() {
            return media_url;
        }

        public String getThumbUrl() {
            return thumb_url;
        }

        public String getDuration() {
            return duration;
        }

        public Dimension getDimension() {
            return media_dimension;
        }

        public boolean isImage() {
            return is_image;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(media_url);
            parcel.writeString(thumb_url);
            parcel.writeString(duration);
            parcel.writeParcelable(media_dimension, i);
            parcel.writeByte((byte) (is_image ? 1 : 0));
        }

        ProfileMedia(Parcel in) {
            media_url = in.readString();
            thumb_url = in.readString();
            duration = in.readString();
            media_dimension = in.readParcelable(Dimension.class.getClassLoader());
            is_image = in.readByte() != 0;
        }

        public static final Creator<ProfileMedia> CREATOR = new Creator<ProfileMedia>() {
            @Override
            public ProfileMedia createFromParcel(Parcel in) {
                return new ProfileMedia(in);
            }

            @Override
            public ProfileMedia[] newArray(int size) {
                return new ProfileMedia[size];
            }
        };
    }


    public static class Medias implements Parcelable {
        private int media_id;
        private String media_url;
        private String thumb_url;
        private String duration;
        private Dimension media_dimension;
        private boolean is_image;
        private int views;
        private String created_at;
        public Medias(int media_id, String media_url, String thumb_url, String duration,
                      Dimension dimension, boolean is_image, int views, String created_at) {
            this.media_id = media_id;
            this.media_url = media_url;
            this.thumb_url = thumb_url;
            this.duration = duration;
            this.media_dimension = dimension;
            this.is_image = is_image;
            this.views = views;
            this.created_at = created_at;
        }

        public int getMediaId() {
            return media_id;
        }

        public String getMediaUrl() {
            return media_url;
        }

        public String getThumbUrl() {
            return thumb_url;
        }

        public String getDuration() {
            return duration;
        }

        public Dimension getDimension() {
            return media_dimension;
        }

        public boolean isImage() {
            return is_image;
        }

        public int getViews() {
            return views;
        }

        public String getCreatedAt() {
            return created_at;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(media_id);
            parcel.writeString(media_url);
            parcel.writeString(thumb_url);
            parcel.writeString(duration);
            parcel.writeParcelable(media_dimension, i);
            parcel.writeByte((byte) (is_image ? 1 : 0));
            parcel.writeInt(views);
            parcel.writeString(created_at);
        }

        protected Medias(Parcel in) {
            media_id = in.readInt();
            media_url = in.readString();
            thumb_url = in.readString();
            duration = in.readString();
            media_dimension = in.readParcelable(Dimension.class.getClassLoader());
            is_image = in.readByte() != 0;
            views = in.readInt();
            created_at = in.readString();
        }

        public static final Creator<Medias> CREATOR = new Creator<Medias>() {
            @Override
            public Medias createFromParcel(Parcel in) {
                return new Medias(in);
            }

            @Override
            public Medias[] newArray(int size) {
                return new Medias[size];
            }
        };
    }

    public static class Dimension implements Parcelable {
        private int height;
        private int width;

        public Dimension(int height, int width) {
            this.height = height;
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(height);
            parcel.writeInt(width);
        }

        protected Dimension(Parcel in) {
            height = in.readInt();
            width = in.readInt();
        }

        public static final Creator<Dimension> CREATOR = new Creator<Dimension>() {
            @Override
            public Dimension createFromParcel(Parcel in) {
                return new Dimension(in);
            }

            @Override
            public Dimension[] newArray(int size) {
                return new Dimension[size];
            }
        };
    }

    public static class CheckIn implements Parcelable{
        private int checkin_id;
        private float latitude;
        private float longitude;
        private String location;

        public CheckIn(int checkin_id, int latitude, int longitude, String location) {
            this.checkin_id = checkin_id;
            this.latitude = latitude;
            this.longitude = longitude;
            this.location = location;
        }

        public int getCheckinId() {
            return checkin_id;
        }

        public float getLatitude() {
            return latitude;
        }

        public float getLongitude() {
            return longitude;
        }

        public String getLocation() {
            return location;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(checkin_id);
            parcel.writeFloat(latitude);
            parcel.writeFloat(longitude);
            parcel.writeString(location);
        }

        protected CheckIn(Parcel in) {
            checkin_id = in.readInt();
            latitude = in.readFloat();
            longitude = in.readFloat();
            location = in.readString();
        }

        public static final Creator<CheckIn> CREATOR = new Creator<CheckIn>() {
            @Override
            public CheckIn createFromParcel(Parcel in) {
                return new CheckIn(in);
            }

            @Override
            public CheckIn[] newArray(int size) {
                return new CheckIn[size];
            }
        };
    }

    public static class Category implements Parcelable {
        int category_id;
        String category_name;

        public Category(int category_id, String category_name) {
            this.category_id = category_id;
            this.category_name = category_name;
        }

        Category(JSONObject jsonObject) {
            try {
                category_id = jsonObject.getInt("category_id");
                category_name = jsonObject.getString("category_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public int getCategoryId() {
            return category_id;
        }

        public String getCategoryName() {
            return category_name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(category_id);
            parcel.writeString(category_name);
        }

        Category(Parcel in) {
            category_id = in.readInt();
            category_name = in.readString();
        }

        public static final Creator<Category> CREATOR = new Creator<Category>() {
            @Override
            public Category createFromParcel(Parcel in) {
                return new Category(in);
            }

            @Override
            public Category[] newArray(int size) {
                return new Category[size];
            }
        };
    }

    public static class UploadParams implements Parcelable {
        private String videoPath;
        private boolean isReaction;
        private String title;
        private String location;
        private double latitude;
        private double longitude;
        private String tags;
        private String categories;

        private int postId;

        public UploadParams(String videoPath, boolean isReaction, String title, String location,
                            double latitude, double longitude, String tags, String categories) {
            this.videoPath = videoPath;
            this.isReaction = isReaction;
            this.title = title;
            this.location = location;
            this.latitude = latitude;
            this.longitude = longitude;
            this.tags = tags;
            this.categories = categories;
        }

        public UploadParams(String videoPath, String title, int postId) {
            this.videoPath = videoPath;
            this.title = title;
            this.postId = postId;
        }

        public UploadParams(String videoPath) {
            this.videoPath = videoPath;
        }

        protected UploadParams(Parcel in) {
            videoPath = in.readString();
            isReaction = in.readByte() != 0;
            title = in.readString();
            location = in.readString();
            latitude = in.readDouble();
            longitude = in.readDouble();
            tags = in.readString();
            categories = in.readString();
            postId = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(videoPath);
            dest.writeByte((byte) (isReaction ? 1 : 0));
            dest.writeString(title);
            dest.writeString(location);
            dest.writeDouble(latitude);
            dest.writeDouble(longitude);
            dest.writeString(tags);
            dest.writeString(categories);
            dest.writeInt(postId);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<UploadParams> CREATOR = new Creator<UploadParams>() {
            @Override
            public UploadParams createFromParcel(Parcel in) {
                return new UploadParams(in);
            }

            @Override
            public UploadParams[] newArray(int size) {
                return new UploadParams[size];
            }
        };

        public String getVideoPath() {
            return videoPath;
        }

        public boolean isReaction() {
            return isReaction;
        }

        public String getTitle() {
            return title;
        }

        public String getLocation() {
            return location;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getTags() {
            return tags;
        }

        public String getCategories() {
            return categories;
        }

        public int getPostId() {
            return postId;
        }
    }
}