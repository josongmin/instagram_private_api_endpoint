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

public class AppAccountVisiblityChangeCall extends InstaAppApiCall {

    public AppAccountVisiblityChangeCall(InstaAppApiClient client) {
        super(client);
    }

    @Override
    public ApiResult work() throws Exception{

        final boolean isPublic = ApiCall.getParam(this, "isPublic");
        final String url = "https://i.instagram.com/api/v1/accounts/" + (isPublic ? "set_public" : "set_private") + "/";

        final String guid = InstaApiUtil.getRandomUUID(true);
        final String userId = getInstaAppApiClient().userId;

        ApiResult resultModel = ApiQuickCaller.postForAppInstagram(getInstaAppApiClient().client, url
                , getCommonHeader()
                , InstaApiUtil.makeStringMap(
                        "_uuid", guid
                        ,  "_uid", userId
                        ,  "_csrftoken", getInstaAppApiClient().getCsrfToken())
        );
        resultModel.object = resultModel.result;

        return resultModel;
    }
}
