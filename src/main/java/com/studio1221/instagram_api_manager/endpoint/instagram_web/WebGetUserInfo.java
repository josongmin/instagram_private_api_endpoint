package com.studio1221.instagram_api_manager.endpoint.instagram_web;

import com.studio1221.instagram_api_manager.browser.InstaWebApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.InstaWebApiCall;
import com.studio1221.instagram_api_manager.browser.api_quick_caller.ApiQuickCaller;
import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.model.InstaUserWebSummary;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;

import java.util.Map;

/**
 * Created by jo on 2017-11-13.
 */

public class WebGetUserInfo extends InstaWebApiCall {

    public WebGetUserInfo(InstaWebApiClient client) {
        super(client);
    }


    @Override
    public ApiResult<InstaUserWebSummary> syncWork() {
        return super.syncWork();
    }


    @Override
    public ApiResult<InstaUserWebSummary> work() throws Exception{

        final String userName = getParam(this, "userName");

        ApiResult result = ApiQuickCaller.get(getInstaWebApiClient().client,
                "https://www.instagram.com/#{userName}/?__a=1".replace("#{userName}", userName)
                , InstaApiUtil.makeStringMap(
                        "accept-language", getInstaWebApiClient().getAcceptLang()
                        , "upgrade-insecure-requests", "1"
                        , "user-agent", getInstaWebApiClient().getUserAgent()
                        , "accept", getInstaWebApiClient().getAccept()
                        , "cache-control", "max-age=0"
                        , "authority", "www.instagram.com"
                )
                , InstaApiUtil.makeStringMap());


        ApiException.chkApiException(result);
        Map<String, Object> mapResult = (Map<String, Object>)getResultMap(result.result).get("user");
        Map<String, Object> mapFollowedBy = (Map<String, Object>)mapResult.get("followed_by");
        Map<String, Object> mapFollow = (Map<String, Object>)mapResult.get("follows");


        InstaUserWebSummary instaUserWebSummary = new InstaUserWebSummary();

        instaUserWebSummary.name = InstaApiUtil.getDataFromMap(mapResult, "username");
        instaUserWebSummary.fullname = InstaApiUtil.getDataFromMap(mapResult, "full_name");
        instaUserWebSummary.id = InstaApiUtil.getDataFromMap(mapResult, "id")+"";
        instaUserWebSummary.isPrivate = InstaApiUtil.getDataFromMap(mapResult, "is_private");
        instaUserWebSummary.isVerified = InstaApiUtil.getDataFromMap(mapResult, "is_verified");
        instaUserWebSummary.photoUrl = InstaApiUtil.getDataFromMap(mapResult, "profile_pic_url");
        instaUserWebSummary.follow = InstaApiUtil.getDataFromMap(mapFollow, "count");
        instaUserWebSummary.followedBy = InstaApiUtil.getDataFromMap(mapFollowedBy, "count");

        ApiResult<InstaUserWebSummary> resultModel = new ApiResult(result.resultCode, result.result, instaUserWebSummary);

//        biography: null,
//                blocked_by_viewer: false,
//                country_block: false,
//                external_url: null,
//                external_url_linkshimmed: null,
//                followed_by: {
//            count: 138
//        },
//        followed_by_viewer: false,
//                follows: {
//            count: 5
//        },
//        follows_viewer: false,
//                full_name: "전후성",
//                has_blocked_viewer: false,
//                has_requested_viewer: false,
//                id: "6060822445",
//                is_private: false,
//                is_verified: false,
//                profile_pic_url: "https://scontent-sit4-1.cdninstagram.com/t51.2885-19/s150x150/21879553_301402603668928_8809465742186512384_n.jpg",
//                profile_pic_url_hd: "https://scontent-sit4-1.cdninstagram.com/t51.2885-19/s320x320/21879553_301402603668928_8809465742186512384_n.jpg",
//                requested_by_viewer: false,
//                username: "jsgso1003",
//                connected_fb_page: null,


        return resultModel;
    }
}
