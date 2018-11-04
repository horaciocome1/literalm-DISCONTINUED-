package com.tumblr.b1moz.literalm.helpers;

public class Constants {
    
    private Constants(){}
    
    public final static String LOGOUT_EXTRA_NAME = "logout";
    public final static int LOGOUT_EXTRA_DATA = 500;
    
    public final static String POST_KEY_EXTRA_NAME = "key";
    
    public static class RequestCode {
        
        public final static int START_ACCOUNT_ACTIVITY = 210;
        public final static int START_SIGN_IN_ACTIVITY = 230;
        public final static int PICK_IMAGE_FROM_GALLERY = 250;
        
    }
    
    public static class FirebaseAuthentication {
    
        public static final int RC_SIGN_IN = 123;
    
    }
    
    public static class RealtimeDatabase {
        
        public final static String POST_NODE_TITLE_ATRIBUTE = "title";
        public final static String POST_DETAILS_NODE_IMAGEURL_ATRIBUTE = "imageUrl";
        public final static String POST_USERS_RATING_NODE_TOTAL_USERS_ATRIBUTE =
                "post_users_rating";
        
        public final static String POST_CONTENT_NODE = "post_content";
        public final static String USERS_NODE = "users";
        public final static String POSTS_NODE = "posts";
        public final static String USER_POSTS_NODE = "user_posts";
        public final static String POST_USERS_RATING_NODE = "post_users_rating";
        public final static String APP_VERSION_NODE = "appversion";
        
    }
    
    public static class FirebaseStorage {
    
        public static final String POST_COVERS_NODE = "post_covers";
        
    }
    
}
