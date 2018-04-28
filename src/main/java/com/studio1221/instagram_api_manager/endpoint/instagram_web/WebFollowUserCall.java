package com.studio1221.instagram_api_manager.endpoint.instagram_web;

import com.studio1221.instagram_api_manager.browser.InstaWebApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.api_call.InstaWebApiCall;
import com.studio1221.instagram_api_manager.browser.api_quick_caller.ApiQuickCaller;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;

/**
 * Created by jo on 2017-11-13.
 */

public class WebFollowUserCall extends InstaWebApiCall {

    public WebFollowUserCall(InstaWebApiClient client) {
        super(client);
    }

    @Override
    public ApiResult work() throws Exception{

        String targetUserId = ApiCall.getParam(this, "targetUserId");
        String targetUserName = ApiCall.getParam(this, "targetUserName");

        //열고
        ApiResult firstResult = ApiQuickCaller.get(getInstaWebApiClient().client,
                "https://www.instagram.com/" + targetUserName + "/?__a=1"
                , InstaApiUtil.makeStringMap(
                        "accept-language", getInstaWebApiClient().getAcceptLang()
                        , "user-agent", getInstaWebApiClient().getUserAgent()
                        , "accept", "*/*"
                        , "x-requested-with", "XMLHttpRequest"
                        , "referer", "https://www.instagram.com/" + targetUserName + "/"
                        , "x-requested-with", "XMLHttpRequest"
                        , "save-data", "on"
                )
                , InstaApiUtil.makeStringMap());

        //ex) https://www.instagram.com/p/Bbjep9gl9ZK/?explore=true
        //ex) https://www.instagram.com/p/Bbjep9gl9ZK/?tagged=%EB%A7%9E%ED%8C%94

        //referer https://www.instagram.com/p/BbkXOM-n1Pw/?tagged=%EB%A7%9E%ED%8C%94
        //팔ㄹ우
        ApiResult result = ApiQuickCaller.post(getInstaWebApiClient().client,
                "https://www.instagram.com/web/friendships/" + targetUserId + "/follow/"
                , InstaApiUtil.makeStringMap(
                        "accept-language", getInstaWebApiClient().getAcceptLang()
                        , "user-agent", getInstaWebApiClient().getUserAgent()
                        , "accept", "*/*"
                        , "origin", "https://www.instagram.com"
                        , "x-requested-with", "XMLHttpRequest"
                        , "referer", "https://www.instagram.com/" + targetUserName + "/"
                        , "x-csrftoken", getInstaWebApiClient().getCsrfToken()
                        , "x-requested-with", "XMLHttpRequest"
                        , "x-instagram-ajax", "1"
                        , "save-data", "on"
                )
                , InstaApiUtil.makeStringMap());

        return result;
    }
}
