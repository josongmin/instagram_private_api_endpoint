package com.studio1221.instagram_api_manager.endpoint.instagram_web;

import com.studio1221.instagram_api_manager.browser.InstaWebApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.api_call.InstaWebApiCall;
import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;

/**
 * Created by jo on 2017-11-21.
 */

public class WebLoginCall extends InstaWebApiCall{

    public WebLoginCall(InstaWebApiClient instaWebApiClient) {
        super(instaWebApiClient);
    }

    @Override
    public ApiResult work() throws Exception {
        String userName = ApiCall.getParam(this, "userName");
        String password = ApiCall.getParam(this, "password");

        ApiResult apiResult = getInstaWebApiClient().login(userName, password);
        ApiException.chkApiException(apiResult);
        return apiResult;
    }
}
