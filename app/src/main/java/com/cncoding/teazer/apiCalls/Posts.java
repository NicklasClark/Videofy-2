package com.cncoding.teazer.apiCalls;

/**
 *
 * Created by Prem $ on 10/16/2017.
 */

public class Posts {
    private boolean next_page;
    private PostDetails posts;

    public Posts(boolean next_page, PostDetails posts) {
        this.next_page = next_page;
        this.posts = posts;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public PostDetails getPosts() {
        return posts;
    }

    public static class PostDetails {
        private int post_id;
        private int posted_by;
        private int likes;
        private int total_reactions;
        private boolean has_checkin;
        private String title;
        private boolean can_react;
        private boolean can_like;
        private boolean can_delete;
        private PostOwner post_owner;
        private String created_at;                  //use DateTime.Now.ToString("yyyy-MM-ddThh:mm:sszzz");
        private CheckIn check_in;
        private Medias medias;
        private Reactions reactions;

        public PostDetails(int post_id, int posted_by, int likes, int total_reactions, boolean has_checkin,
                           String title, boolean can_react, boolean can_like, boolean can_delete,
                           PostOwner post_owner, String created_at, CheckIn check_in, Medias medias, Reactions reactions) {
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
            this.reactions = reactions;
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

        public boolean isHasCheckin() {
            return has_checkin;
        }

        public String getTitle() {
            return title;
        }

        public boolean isCanReact() {
            return can_react;
        }

        public boolean isCanLike() {
            return can_like;
        }

        public boolean isCanDelete() {
            return can_delete;
        }

        public PostOwner getPostOwner() {
            return post_owner;
        }

        public String getCreatedAt() {
            return created_at;
        }

        public CheckIn getCheckIn() {
            return check_in;
        }

        public Medias getMedias() {
            return medias;
        }

        public Reactions getReactions() {
            return reactions;
        }

        private static class PostOwner {
            private int user_id;
            private String user_name;
            private String first_name;
            private String last_name;
            private boolean has_profile_media;
            private ProfileMedia profile_media;

            private PostOwner(int user_id, String user_name, String first_name, String last_name,
                              boolean has_profile_media, ProfileMedia profile_media) {
                this.user_id = user_id;
                this.user_name = user_name;
                this.first_name = first_name;
                this.last_name = last_name;
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

            public boolean isHas_profileMedia() {
                return has_profile_media;
            }

            public ProfileMedia getProfileMedia() {
                return profile_media;
            }


            static class ProfileMedia {
                private String media_url;
                private String thumb_url;
                private String duration;
                private String dimension;
                private boolean is_image;

                private ProfileMedia(String media_url, String thumb_url, String duration, String dimension, boolean is_image) {
                    this.media_url = media_url;
                    this.thumb_url = thumb_url;
                    this.duration = duration;
                    this.dimension = dimension;
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

                public String getDimension() {
                    return dimension;
                }

                public boolean isImage() {
                    return is_image;
                }
            }
        }

        private static class CheckIn {
            private int checkin_id;
            private int latitude;
            private int longitude;
            private String location;

            private CheckIn(int checkin_id, int latitude, int longitude, String location) {
                this.checkin_id = checkin_id;
                this.latitude = latitude;
                this.longitude = longitude;
                this.location = location;
            }

            public int getCheckinId() {
                return checkin_id;
            }

            public int getLatitude() {
                return latitude;
            }

            public int getLongitude() {
                return longitude;
            }

            public String getLocation() {
                return location;
            }
        }

        private static class Medias {
            private int media_id;
            private String media_url;
            private String thumb_url;
            private String duration;
            private String dimension;
            private boolean is_image;
            private int views;
            private String created_at;

            private Medias(int media_id, String media_url, String thumb_url, String duration,
                           String dimension, boolean is_image, int views, String created_at) {
                this.media_id = media_id;
                this.media_url = media_url;
                this.thumb_url = thumb_url;
                this.duration = duration;
                this.dimension = dimension;
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

            public String getDimension() {
                return dimension;
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
        }

        static class Reactions {
            private int react_id;
            private int post_id;
            private int post_owner_id;
            private int likes;
            private int views;
            private boolean can_like;
            private boolean can_delete;
            private MediaDetail media_detail;
            private ReactOwner react_owner;
            private String reacted_at;

            private Reactions(int react_id, int post_id, int post_owner_id, int likes, int views, boolean can_like,
                              boolean can_delete, MediaDetail media_detail, ReactOwner react_owner, String reacted_at) {
                this.react_id = react_id;
                this.post_id = post_id;
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

            public int getPostOwnerId() {
                return post_owner_id;
            }

            public int getLikes() {
                return likes;
            }

            public int getViews() {
                return views;
            }

            public boolean isCanLike() {
                return can_like;
            }

            public boolean isCanDelete() {
                return can_delete;
            }

            public MediaDetail getMediaDetail() {
                return media_detail;
            }

            public ReactOwner getReactOwner() {
                return react_owner;
            }

            public String getReactedAt() {
                return reacted_at;
            }

            static class MediaDetail {
                private int media_id;
                private String media_url;
                private String thumb_url;
                private String duration;
                private String dimension;
                private boolean is_image;

                MediaDetail(int media_id, String media_url, String thumb_url, String duration, String dimension, boolean is_image) {
                    this.media_id = media_id;
                    this.media_url = media_url;
                    this.thumb_url = thumb_url;
                    this.duration = duration;
                    this.dimension = dimension;
                    this.is_image = is_image;
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

                public String getDimension() {
                    return dimension;
                }

                public boolean isImage() {
                    return is_image;
                }
            }

            static class ReactOwner extends PostOwner {
                private ReactOwner(int user_id, String user_name, String first_name, String last_name,
                                   boolean has_profile_media, ProfileMedia profile_media) {
                    super(user_id, user_name, first_name, last_name, has_profile_media, profile_media);
                }
            }
        }

        public static class TaggedUser extends PostOwner {
            private boolean next_page;
            TaggedUser(boolean next_page, int user_id, String user_name, String first_name, String last_name,
                       boolean has_profile_media, ProfileMedia profile_media) {
                super(user_id, user_name, first_name, last_name, has_profile_media, profile_media);
                this.next_page = next_page;
            }

            public boolean isNextPage() {
                return next_page;
            }
        }
    }
}