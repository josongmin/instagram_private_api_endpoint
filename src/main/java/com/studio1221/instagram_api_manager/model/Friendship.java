package com.studio1221.instagram_api_manager.model;

/**
 * Created by josong on 2017-09-08.
 */

public class Friendship {
    public final static int FRIENDSHIP_REQUEST = 1;
    public final static int FRIENDSHIP_REQUEST_ME = 2;
    public final static int FRIENDSHIP_REQUEST_NONE = 3;

    public final static int FRIENDSHIP_FOLLOW = 3;
    public final static int FRIENDSHIP_FOLLOW_ME = 4;
    public final static int FRIENDSHIP_FRIEND = 5;
    public final static int FRIENDSHIP_NONE = 6;

    public String myId, targetUserId;
    public int request, follow;
}
