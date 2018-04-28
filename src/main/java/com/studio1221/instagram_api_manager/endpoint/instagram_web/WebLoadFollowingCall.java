package com.studio1221.instagram_api_manager.endpoint.instagram_web;

import com.google.gson.Gson;
import com.studio1221.instagram_api_manager.browser.InstaWebApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.api_call.InstaWebApiCall;
import com.studio1221.instagram_api_manager.browser.api_quick_caller.ApiQuickCaller;
import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.model.InstaUser;
import com.studio1221.instagram_api_manager.model.InstaUsers;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jo on 2017-11-13.
 */

public class WebLoadFollowingCall extends InstaWebApiCall {

    public WebLoadFollowingCall(InstaWebApiClient client) {
        super(client);
    }

    @Override
    public ApiResult<InstaUsers> work() throws Exception{

        final String userId = ApiCall.getParam(this, "targetUserId");
        final String userName = ApiCall.getParam(this, "targetUserName");
        final int loadSize = ApiCall.getParam(this, "loadSize");
        final String lastItemKey = ApiCall.getParam(this, "lastItemKey");

        //{"id":"1584564164","first":10,"after":"AQDRZRcCJBfjcCnCL_7oxHaQ3V7gq7VG4HHi24PBs0e5kjaeMr2_bz2DS8lvveJF6qCzsiWYtL2vQrlVBC4fdhy6aJiIJHO89hrDOdo7HOj0sA"}

        Map<String, Object> mapExtraParams = new HashMap<>();
        mapExtraParams.put("id", userId);
        mapExtraParams.put("first", loadSize);
        if (lastItemKey != null) {
            mapExtraParams.put("after", lastItemKey);
        }

        String jsonParam = new Gson().toJson(mapExtraParams);
        jsonParam = URLEncoder.encode(jsonParam, "UTF-8");
        String url = "https://www.instagram.com/graphql/query/?query_id=" + "17874545323001329" + "&variables=" + jsonParam;

        ApiResult result = ApiQuickCaller.get(getInstaWebApiClient().client,
                url
                , InstaApiUtil.makeStringMap(
                        "accept-language", getInstaWebApiClient().getAcceptLang()
                        , "upgrade-insecure-requests", "1"
                        , "user-agent", getInstaWebApiClient().getUserAgent()
                        , "accept", "*/*"
                        , "referer", "https://www.instagram.com/" + userName + "/followers/"
                        , "x-requested-with", "XMLHttpRequest"
                        , "save-data", "on"
                )
                , null);

        ApiException.chkApiException(result);

        //분석
        JSONObject jsonObject = new JSONObject(result.result).getJSONObject("data");
//        if(!jsonObject.getString("status").equals("OK")){
//            throw new ApiException(result.resultCode, result.result, result.object);
//        }

        InstaUsers users = new InstaUsers();

        jsonObject = jsonObject.getJSONObject("user").getJSONObject("edge_follow");
        users.countForDisplay = jsonObject.getInt("count");
        JSONObject jPageInfo = jsonObject.getJSONObject("page_info");
        users.hasNextPage = jPageInfo.getBoolean("has_next_page");
        users.endCursor = jPageInfo.getString("end_cursor");

        //사용자정보
        JSONArray jUserArr = jsonObject.getJSONArray("edges");
        for (int i = 0; i < jUserArr.length(); i++) {
            InstaUser user = new InstaUser();

            JSONObject jUser = jUserArr.getJSONObject(i).getJSONObject("node");
            //뮤츄얼아닌 나를 팔로우하는애들
            user.id = jUser.getString("id");
            user.userName = jUser.getString("username");
            user.fullName = jUser.getString("full_name");
            user.userPhotoThumbUrl = InstaApiUtil.getThumbImgUrl(InstaApiUtil.IMG_THUMB_640, jUser.getString("profile_pic_url"));
            user.userPhotoUrl = InstaApiUtil.getThumbImgUrl(InstaApiUtil.IMG_ORIGINAL, jUser.getString("profile_pic_url"));
            user.followedByMe = jUser.getBoolean("followed_by_viewer");
            user.requestedByMe = jUser.getBoolean("requested_by_viewer");
            users.listUsers.add(user);
            users.mapUsers.put(user.id, user);
        }

        return new ApiResult<InstaUsers>()
                .setResultCode(200)
                .setResult("success")
                .setObject(users);
    }
}
