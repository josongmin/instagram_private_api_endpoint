package com.studio1221.instagram_api_manager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josong on 2017-09-11.
 */

public class RelationAnalysis {
    public List<InstaUser> listMutual = new ArrayList<>();
    public List<InstaUser> listGhost = new ArrayList<>();
    public List<InstaUser> listUnfollower = new ArrayList<>();
    public List<InstaUser> listFollower = new ArrayList<>();
    public List<InstaUser> listFollowing = new ArrayList<>();

    public int followerCnt, followingCnt;
}
