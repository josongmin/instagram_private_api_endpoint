package com.studio1221.instagram_api_manager.browser.api_call;

import com.studio1221.instagram_api_manager.browser.CommonApiClient;

/**
 * Created by jo on 2017-11-21.
 */

public abstract class CommonApiCall extends ApiCall{

    CommonApiClient commonApiClient;
    public CommonApiCall(CommonApiClient commonApiClient) {
        this.commonApiClient = commonApiClient;
    }

    public CommonApiClient getCommonApiClient() {
        return commonApiClient;
    }
}
