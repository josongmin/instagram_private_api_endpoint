package com.studio1221.instagram_api_manager.endpoint.instagram_web;


import com.studio1221.instagram_api_manager.browser.InstaWebApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.api_call.InstaWebApiCall;
import com.studio1221.instagram_api_manager.browser.api_quick_caller.ApiQuickCaller;
import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by jo on 2017-11-13.
 */

public class WebGetPostDetailCall extends InstaWebApiCall {
    public final static String REFERERTYPE_IN_USER_PAGE = "IN_USER_PAGE";
    public final static String REFERERTYPE_BY_TAG = "BY_TAG";

    public WebGetPostDetailCall(InstaWebApiClient client) {
        super(client);
    }

    @Override
    public ApiResult<Map<String, String>> work() throws Exception{

        final String postCode = ApiCall.getParam(this, "postCode");

        String refererType = ApiCall.getParam(this, "refererType");
        String refererString = null;


        if (refererType.equals(REFERERTYPE_IN_USER_PAGE)) {
            //https://www.instagram.com/p/Bbi45dggXuh/?taken-by=afetsener
            String userName = ApiCall.getParam(this, "userName");
            if(userName == null)  throw new ApiException(-100, "userName not ready", null);
            refererString = "https://www.instagram.com/p/" + postCode + "/?taken-by=" + userName;
        }
        else if(refererType.equals(REFERERTYPE_BY_TAG)){
            String searchTag = ApiCall.getParam(this, "searchTag");
            if(searchTag == null)  throw new ApiException(-100, "searchTag not ready", null);
            refererString = "https://www.instagram.com/p/" + postCode + "/?tagged=" + InstaApiUtil.encodeUTF8(searchTag);
        }
        ApiResult result = ApiQuickCaller.get(getInstaWebApiClient().client,
                "https://www.instagram.com/p/" + postCode + "/?__a=1"
                , InstaApiUtil.makeStringMap(
                        "accept-language", getInstaWebApiClient().getAcceptLang()
                        , "user-agent", getInstaWebApiClient().getUserAgent()
                        , "accept", "*/*"
                        , "x-requested-with", "XMLHttpRequest"
                        , "referer", refererString
                        , "save-data", "on"
                )
                , InstaApiUtil.makeStringMap());

        ApiException.chkApiException(result);

        //parsing
        String postId, ownerId, ownerName;
        JSONObject jResult = new JSONObject(result.result).getJSONObject("graphql").getJSONObject("shortcode_media");
        postId = jResult.getString("id");
        JSONObject jOwner = jResult.getJSONObject("owner");
        ownerId = jOwner.getString("id");
        ownerName = jOwner.getString("username");

        Map<String, String> mapResult = InstaApiUtil.makeStringMap("postId", postId, "ownerId", ownerId, "ownerName", ownerName);

        ApiResult<Map<String, String>> apiResult = new ApiResult(200, "success", mapResult);

        return apiResult;
    }
}
