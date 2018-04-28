package com.studio1221.instagram_api_manager.endpoint.instagram_app;

import com.studio1221.instagram_api_manager.browser.InstaAppApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.api_call.InstaAppApiCall;
import com.studio1221.instagram_api_manager.browser.api_quick_caller.ApiQuickCaller;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;

/**
 * Created by jo on 2017-11-13.
 */

public class AppUnFollowCall extends InstaAppApiCall {

    public AppUnFollowCall(InstaAppApiClient client) {
        super(client);
    }

    @Override
    public ApiResult work() throws Exception{

        String targetUserId = ApiCall.getParam(this, "targetUserId");
        final String url = "https://i.instagram.com/api/v1/friendships/destroy/" + targetUserId + "/";

        final String guid = getInstaAppApiClient().guid;
        final String userId = getInstaAppApiClient().userId;

        ApiResult resultModel = ApiQuickCaller.postForAppInstagram(getInstaAppApiClient().client, url
                , getCommonHeader()
                , InstaApiUtil.makeStringMap(
                        "_uuid", guid
                        ,  "_uid", userId
                        ,  "_csrftoken", getInstaAppApiClient().getCsrfToken()
                        ,  "user_id", targetUserId)
        );
        resultModel.object = resultModel.result;

        return resultModel;
    }
}
