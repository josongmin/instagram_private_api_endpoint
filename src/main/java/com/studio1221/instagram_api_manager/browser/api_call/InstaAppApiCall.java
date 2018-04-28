package com.studio1221.instagram_api_manager.browser.api_call;


import com.studio1221.instagram_api_manager.browser.InstaAppApiClient;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;

import java.util.Map;

/**
 * Created by jo on 2017-11-21.
 */

public abstract class InstaAppApiCall extends ApiCall{

    InstaAppApiClient instaAppApiClient;
    public InstaAppApiCall(InstaAppApiClient instaAppApiClient) {
        this.instaAppApiClient = instaAppApiClient;
    }

    public InstaAppApiClient getInstaAppApiClient() {
        return instaAppApiClient;
    }

    public Map<String, String> getCommonHeader(){
        return InstaApiUtil.makeStringMap(
                "Host", "i.instagram.com"
                , "Accept", "*/*"
                , "Cookie2", "$Version=1"
                , "Accept-Language", "en-US"
                , "X-IG-Capabilities", "3Q=="
                , "X-IG-Connection-Type", "WIFI"
                , "User-Agent", InstaApiUtil.getMobileAppUserAgent());

    }

    public Map<String, String> makeHeaderWithCommonHeader(String... keyValues){
        Map<String, String> mapHeader = getCommonHeader();
        for(int i = 0; i < keyValues.length; i+=2){
            mapHeader.put(keyValues[i], keyValues[i+1]);
        }

        return mapHeader;
    }
}
