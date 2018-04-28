package com.studio1221.instagram_api_manager.model;

/**
 * Created by jo on 2017-11-16.
 */

public class InstaUser {
    public String id, userName, fullName, userPhotoUrl, userPhotoThumbUrl;
    public int no = 0;
    public long _id = -1;
    public boolean followedByMe; /**나한테 팔로우 되고잇냐*/
    public boolean requestedByMe; /**나한테 신청받았냐 */
    public int friendShip;

    public long getId() {
        if(_id == -1){
            _id = Long.parseLong(id);
        }
        return _id;
    }
}
