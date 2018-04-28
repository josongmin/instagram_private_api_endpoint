package com.studio1221.instagram_api_manager.endpoint.instagram_app;

import com.studio1221.instagram_api_manager.browser.InstaAppApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.api_call.InstaAppApiCall;
import com.studio1221.instagram_api_manager.browser.api_quick_caller.ApiQuickCaller;
import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by jo on 2017-11-13.
 */

public class AppChangeProfileImageCall extends InstaAppApiCall {

    public AppChangeProfileImageCall(InstaAppApiClient client) {
        super(client);
    }

    @Override
    public ApiResult work() throws Exception{

        final String urlUpload = "https://i.instagram.com/api/v1/accounts/change_profile_picture/";
        final String guid = getInstaAppApiClient().guid;
        final String csrfToken = getInstaAppApiClient().getCsrfToken();
        final String userId = getInstaAppApiClient().userId;
        final File fileToUpload = ApiCall.getParam(this, "file");

        MultipartBody body = new MultipartBody.Builder(guid)
                .setType(MultipartBody.FORM)
                .addFormDataPart("_csrftoken", csrfToken)
                .addFormDataPart("_uuid", guid)
                .addFormDataPart("_uid", userId)
                .addFormDataPart("profile_pic", "profile_pic", RequestBody.create(MediaType.parse("application/octet-stream"), fileToUpload))
                .build();

        ApiResult result = ApiQuickCaller.post(
                getInstaAppApiClient().client
                , urlUpload
                , getCommonHeader()
                , body);

        if(result.resultCode != 200){
            //실패
            throw new ApiException(result.resultCode, result.result);
        }

        result.object = result.result;
        return result;
    }
}
