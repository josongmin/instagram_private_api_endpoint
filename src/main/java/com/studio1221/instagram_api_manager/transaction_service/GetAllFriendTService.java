package com.studio1221.instagram_api_manager.transaction_service;

import com.studio1221.instagram_api_manager.browser.InstaWebApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.endpoint.instagram_web.WebLoadFollowerCall;
import com.studio1221.instagram_api_manager.endpoint.instagram_web.WebLoadFollowingCall;
import com.studio1221.instagram_api_manager.model.Friendship;
import com.studio1221.instagram_api_manager.model.InstaUser;
import com.studio1221.instagram_api_manager.model.InstaUsers;
import com.studio1221.instagram_api_manager.model.RelationAnalysis;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by jo on 2017-11-21.
 */

public class GetAllFriendTService extends ApiCall<RelationAnalysis> {

    InstaWebApiClient instaWebApiClient = null;
    String targetUserName, targetUserId;
    InstaUsers instaFollowings = null, instaFollowers = null;


    public GetAllFriendTService(InstaWebApiClient instaWebApiClient, String targetUserName, String targetUserId) {
        this.instaWebApiClient = instaWebApiClient;
        this.targetUserId = targetUserId;
        this.targetUserName = targetUserName;
    }

    public GetAllFriendTService(InstaWebApiClient instaWebApiClient) {
        this.instaWebApiClient = instaWebApiClient;
        this.targetUserId = instaWebApiClient.id;
        this.targetUserName = instaWebApiClient.name;
    }

    @Override
    public ApiResult<RelationAnalysis> work() throws Exception {


        Thread thFollower = new Thread(){
            @Override
            public void run() {
                super.run();
                instaFollowers = loadAllFollower();
            }
        };

        Thread thFollowing = new Thread(){
            @Override
            public void run() {
                super.run();
                instaFollowings = loadAllFollowing();
            }
        };

        thFollower.start();
        thFollowing.start();

        thFollower.join();
        thFollowing.join();

        if(instaFollowers == null){
            throw new ApiException(-200, "insta followers null");
        }

        if(instaFollowings == null){
            throw new ApiException(-300, "insta followings null");
        }

        RelationAnalysis relationAnalysis = getAnalysisFriendship(instaFollowers, instaFollowings);

        return new ApiResult<>(200, "success", relationAnalysis);
    }





    //아날리시스 프랜드쉽 가져오기
    private RelationAnalysis getAnalysisFriendship(InstaUsers followers, InstaUsers followings){

        RelationAnalysis relationAnalysis = new RelationAnalysis();
        relationAnalysis.listFollower = followers.listUsers;
        relationAnalysis.listFollowing = followings.listUsers;
        relationAnalysis.followerCnt = followers.countForDisplay;
        relationAnalysis.followingCnt = followings.countForDisplay;

        Iterator entries = followings.mapUsers.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, InstaUser> entry = (Map.Entry) entries.next();
            String key = entry.getKey();
            InstaUser user = entry.getValue();

            //팔로잉아이템이 팔로워에도 있으면 뮤츄얼
            if (followers.mapUsers.containsKey(key)) {
                relationAnalysis.listMutual.add(user);
                user.friendShip = Friendship.FRIENDSHIP_FRIEND;
            } else {
                relationAnalysis.listUnfollower.add(user);
                user.friendShip = Friendship.FRIENDSHIP_FOLLOW;
            }
        }

        entries = followers.mapUsers.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, InstaUser> entry = (Map.Entry) entries.next();
            String key = entry.getKey();
            InstaUser user = entry.getValue();

            //고스트
            if (!followings.mapUsers.containsKey(key)) {
                relationAnalysis.listGhost.add(user);
                user.friendShip = Friendship.FRIENDSHIP_FOLLOW_ME;
            }
        }

        return relationAnalysis;
    }

    //팔로잉 로드
    private InstaUsers loadAllFollowing(){
        try{
            return loadAll(true);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //팔로워 로드
    private InstaUsers loadAllFollower(){
        try{
            return loadAll(false);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //팔로워나 팔로잉 불러오기
    private InstaUsers loadAll(final boolean loadFollowing) throws Exception{
        String lastItemKey = null;
        InstaUsers usersResult = new InstaUsers();
        boolean hasNextPage = false;
        do {

            InstaUsers users = null;
            if(loadFollowing){
                ApiResult<InstaUsers> followingResult = new WebLoadFollowingCall(instaWebApiClient)
                        .setParams("targetUserName", targetUserName
                                , "targetUserId", targetUserId
                                , "loadSize", 3000
                                , "lastItemKey", lastItemKey)
                        .syncWork();

                ApiException.chkApiException(followingResult);

                users = followingResult.object;
            }else{
                ApiResult<InstaUsers> followerResult = new WebLoadFollowerCall(instaWebApiClient)
                        .setParams("targetUserName", targetUserName
                                , "targetUserId", targetUserId
                                , "loadSize", 3000
                                , "lastItemKey", lastItemKey)
                        .syncWork();

                ApiException.chkApiException(followerResult);
                users = followerResult.object;
            }

            usersResult.countForDisplay = users.countForDisplay;
            lastItemKey = users.endCursor;
            hasNextPage = users.hasNextPage;

            usersResult.mapUsers.putAll(users.mapUsers);
            usersResult.listUsers.addAll(users.listUsers);

        }while(hasNextPage);

        return usersResult;
    }
}
