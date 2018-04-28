package com.studio1221.instagram_api_manager.transaction_service;

import com.studio1221.instagram_api_manager.browser.InstaWebApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;

/**
 * Created by jo on 2017-11-21.
 */

public class WebInstaLoginService extends ApiCall {

    InstaWebApiClient instaWebApiClient = null;

    public WebInstaLoginService(InstaWebApiClient instaWebApiClient) {
        this.instaWebApiClient = instaWebApiClient;
    }

    @Override
    public ApiResult work() throws Exception {

        return new ApiResult<>(200, "success");
    }
}
