package com.studio1221.instagram_api_manager.browser.api_call;

import com.studio1221.instagram_api_manager.browser.InstaWebApiClient;

/**
 * Created by jo on 2017-11-21.
 */

public abstract class InstaWebApiCall extends ApiCall{

    InstaWebApiClient instaWebApiClient;
    public InstaWebApiCall(InstaWebApiClient instaWebApiClient) {
        this.instaWebApiClient = instaWebApiClient;
    }

    public InstaWebApiClient getInstaWebApiClient() {
        return instaWebApiClient;
    }
}
