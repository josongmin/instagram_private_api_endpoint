package com.studio1221.instagram_api_manager.endpoint.instagram_web;

import com.studio1221.instagram_api_manager.browser.InstaWebApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.api_call.InstaWebApiCall;
import com.studio1221.instagram_api_manager.browser.api_quick_caller.ApiQuickCaller;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;
import com.studio1221.instagram_api_manager.util.RandomIdManager;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by jo on 2017-11-13.
 */

public class WebChangeProfilePictureCall extends InstaWebApiCall {


    public WebChangeProfilePictureCall(InstaWebApiClient client) {
        super(client);
    }

    @Override
    public ApiResult work() throws Exception{

        final String filePath = ApiCall.getParam(this, "filePath");

        //일단 마이페이지로 이동

        MultipartBody body = new MultipartBody.Builder(RandomIdManager.makeRandomWebkitBoundary())
                .setType(MultipartBody.FORM)
                .addFormDataPart("profile_pic", "profilepic.jpg"
                        , RequestBody.create(MediaType.parse("image/jpeg"), new File(filePath)))
                .build();

        //----WebKitFormBoundaryElKBAImEHc7210vd
        //----WebKitFormBoundarys55MsvYX8AXVWtTp
        //----WebKitFormBoundaryrZ0rULd8qlsdQM8W //내가만든거
        ApiResult result = ApiQuickCaller.post(getInstaWebApiClient().client,
                "https://www.instagram.com/accounts/web_change_profile_picture/"
                , InstaApiUtil.makeStringMap(
                        "accept-language", getInstaWebApiClient().getAcceptLang()
                        , "origin", "https://www.instagram.com"
                        , "x-requested-with", "XMLHttpRequest"
                        , "x-instagram-ajax", "1"
                        , "x-csrftoken", getInstaWebApiClient().getCsrfToken()
                        , "user-agent", getInstaWebApiClient().getUserAgent()
                        , "accept", "*/*"
                        , "save-data", "on"
                        , "referer", "https://www.instagram.com/" + getInstaWebApiClient().name + "/"
                        , "authority", "www.instagram.com"
                )
                , body);


        //{"changed_profile": true, "id": 366477841, "has_profile_pic": true, "profile_pic_url": "https://scontent-sin6-1.cdninstagram.com/t51.2885-19/s150x150/23596028_719986838207213_8868409907716554752_n.jpg", "profile_pic_url_hd": "https://scontent-sin6-1.cdninstagram.com/t51.2885-19/s320x320/23596028_719986838207213_8868409907716554752_n.jpg", "status": "ok"}
        return result;
    }
}
