package com.studio1221.instagram_api_manager.endpoint.instagram_web;

import com.studio1221.instagram_api_manager.browser.InstaWebApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.InstaWebApiCall;
import com.studio1221.instagram_api_manager.browser.api_quick_caller.ApiQuickCaller;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;

/**
 * Created by jo on 2017-11-13.
 */

public class WebLogoutCall extends InstaWebApiCall {
    private final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36";
    private final String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
    private final String acceptLang = "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,id;q=0.6";

    public WebLogoutCall(InstaWebApiClient client) {
        super(client);
    }

    @Override
    public ApiResult work() throws Exception{

        ApiResult result = ApiQuickCaller.post(getInstaWebApiClient().client,
                "https://www.instagram.com/accounts/logout/"
                , InstaApiUtil.makeStringMap(
                        "accept-language", acceptLang
                        , "origin", "https://www.instagram.com"
                        , "upgrade-insecure-requests", "1"
                        , "x-requested-with", "XMLHttpRequest"
                        , "user-agent", userAgent
                        , "accept", accept
                        , "cache-control", "max-age=0"
                        , "authority", "www.instagram.com"
                        , "save-data", "on"
                        , "referer", "https://www.instagram.com/" + getInstaWebApiClient().name + "/"
                        , "authority", "www.instagram.com"
                )
                , InstaApiUtil.makeStringMap());


        return result;
    }
}
