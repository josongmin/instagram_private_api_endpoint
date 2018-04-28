package com.studio1221.instagram_api_manager.endpoint.instagram_web;

import com.studio1221.instagram_api_manager.browser.InstaWebApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.api_call.InstaWebApiCall;
import com.studio1221.instagram_api_manager.browser.api_quick_caller.ApiQuickCaller;
import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;
import com.studio1221.instagram_api_manager.util.RandomIdManager;

import java.io.File;
import java.net.URLDecoder;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by jo on 2017-11-13.
 */

public class WebUploadPostingCall extends InstaWebApiCall {

    public WebUploadPostingCall(InstaWebApiClient client) {
        super(client);
    }

    @Override
    public ApiResult work() throws Exception{

        //모바일 버전 아니면 모바일로 변경함.
        final String filePath = ApiCall.getParam(this, "filePath");

        //일단 마이페이지로 이동
        String uploadId = String.valueOf(System.currentTimeMillis());

        MultipartBody body = new MultipartBody.Builder(RandomIdManager.makeRandomWebkitBoundary())
                .setType(MultipartBody.FORM)
                .addFormDataPart("upload_id", uploadId)
                .addFormDataPart("photo", "photo.jpg"
                        , RequestBody.create(MediaType.parse("image/jpeg"), new File(filePath)))
                .addFormDataPart("media_type", "1")
                .build();

        ApiResult result = ApiQuickCaller.post(getInstaWebApiClient().client,
                "https://www.instagram.com/create/upload/photo/"
                , InstaApiUtil.makeStringMap(
                        "accept-language", getInstaWebApiClient().getAcceptLang()
                        , "origin", "https://www.instagram.com"
                        , "x-requested-with", "XMLHttpRequest"
                        , "save-data", "on"
                        , "x-csrftoken", getInstaWebApiClient().getCsrfToken()
                        , "x-instagram-ajax", "1"
                        , "user-agent", getInstaWebApiClient().getUserAgent()
                        , "accept", "*/*"
                        , "referer", "https://www.instagram.com/create/style/"
                        , "authority", "www.instagram.com"
                )
                , body);
        //"upload_id": "1511028440715", "xsharing_nonces": {}, "status": "ok"}
        ApiException.chkApiException(result);

        //https://www.instagram.com/create/configure
//        upload_id:1511028440715
//        caption:
        //&caption=
        String configureParam = "upload_id={uploadId}&caption=".replace("{uploadId}", uploadId);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), configureParam);

        ApiResult resultConfigure = ApiQuickCaller.post(getInstaWebApiClient().client
                , "https://www.instagram.com/create/configure/"
                , InstaApiUtil.makeStringMap(
                        "origin", "https://www.instagram.com"
                        , "accept-language", getInstaWebApiClient().getAcceptLang()
                        , "user-agent", getInstaWebApiClient().getUserAgent()
                        , "save-data", "on"
                        , "x-csrftoken", getInstaWebApiClient().getCsrfToken()
                        , "x-instagram-ajax", "1"
                        , "x-requested-with", "XMLHttpRequest"
                        , "accept", "*/*"
                        , "referer", "https://www.instagram.com/create/details/"
                        , "authority", "www.instagram.com"
                )
                , requestBody
        );

        ApiException.chkApiException(resultConfigure);

        //query_id 1784---호출

        String variableForQuery = "{\"fetch_media_item_count\":12,\"fetch_media_item_cursor\":null,\"fetch_comment_count\":4,\"fetch_like\":10,\"has_stories\":false}";
        String queryUrl = "https://www.instagram.com/graphql/query/?query_id=17842794232208280&variables=" + variableForQuery;

        ApiResult queryResult = ApiQuickCaller.get(getInstaWebApiClient().client
                , queryUrl
                , InstaApiUtil.makeStringMap(
                        "accept-language", getInstaWebApiClient().getAcceptLang()
                        , "user-agent", getInstaWebApiClient().getUserAgent()
                        , "accept", "*/*"
                        , "referer", "https://www.instagram.com/create/details/"
                        , "authority", "www.instagram.com"
                        , "x-requested-with", "XMLHttpRequest"
                        , "save-data", "on"
                )
                , InstaApiUtil.makeStringMap(
                )
        );

                //fetch_web 호출
        String query = "viewer()+%7B%0A++eligible_promotions.surface_nux_id(%3Csurface%3E).external_gating_permitted_qps(%3Cexternal_gating_permitted_qps%3E)+%7B%0A++++edges+%7B%0A++++++priority%2C%0A++++++time_range+%7B%0A++++++++start%2C%0A++++++++end%0A++++++%7D%2C%0A++++++node+%7B%0A++++++++id%2C%0A++++++++promotion_id%2C%0A++++++++max_impressions%2C%0A++++++++triggers%2C%0A++++++++template+%7B%0A++++++++++name%2C%0A++++++++++parameters+%7B%0A++++++++++++name%2C%0A++++++++++++string_value%0A++++++++++%7D%0A++++++++%7D%2C%0A++++++++creatives+%7B%0A++++++++++title+%7B%0A++++++++++++text%0A++++++++++%7D%2C%0A++++++++++content+%7B%0A++++++++++++text%0A++++++++++%7D%2C%0A++++++++++footer+%7B%0A++++++++++++text%0A++++++++++%7D%2C%0A++++++++++social_context+%7B%0A++++++++++++text%0A++++++++++%7D%2C%0A++++++++++primary_action%7B%0A++++++++++++title+%7B%0A++++++++++++++text%0A++++++++++++%7D%2C%0A++++++++++++url%2C%0A++++++++++++limit%2C%0A++++++++++++dismiss_promotion%0A++++++++++%7D%2C%0A++++++++++secondary_action%7B%0A++++++++++++title+%7B%0A++++++++++++++text%0A++++++++++++%7D%2C%0A++++++++++++url%2C%0A++++++++++++limit%2C%0A++++++++++++dismiss_promotion%0A++++++++++%7D%2C%0A++++++++++dismiss_action%7B%0A++++++++++++title+%7B%0A++++++++++++++text%0A++++++++++++%7D%2C%0A++++++++++++url%2C%0A++++++++++++limit%2C%0A++++++++++++dismiss_promotion%0A++++++++++%7D%2C%0A++++++++++image+%7B%0A++++++++++++uri%0A++++++++++%7D%0A++++++++%7D%0A++++++%7D%0A++++%7D%0A++%7D%0A%7D";
        String str = URLDecoder.decode(query, "utf-8");

        ApiResult queryResult2 = ApiQuickCaller.post(getInstaWebApiClient().client,
                "https://www.instagram.com/qp/fetch_web/"
                , InstaApiUtil.makeStringMap(
                        "origin", "https://www.instagram.com"
                        , "accept-language", getInstaWebApiClient().getAcceptLang()
                        , "user-agent", getInstaWebApiClient().getUserAgent()
                        , "x-requested-with", "XMLHttpRequest"
                        , "save-data", "on"
                        , "x-csrftoken", getInstaWebApiClient().getCsrfToken()
                        , "x-instagram-ajax", "1"
                        , "accept", "*/*"
                        , "referer", "https://www.instagram.com/"
                        , "authority", "www.instagram.com"
                )
                , InstaApiUtil.makeStringMap(
                        "query", str
                        , "surface_param", "5095"
                        , "vc_policy", "default"
                        , "version", "1"));

        ApiException.chkApiException(queryResult2);


        return result;
    }
}
