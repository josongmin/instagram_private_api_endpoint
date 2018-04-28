package com.studio1221.instagram_api_manager.endpoint.instagram_web;

import com.studio1221.instagram_api_manager.browser.InstaWebApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.api_call.InstaWebApiCall;
import com.studio1221.instagram_api_manager.browser.api_quick_caller.ApiQuickCaller;
import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by jo on 2017-11-13.
 */

public class WebSignUpCall extends InstaWebApiCall{

    //text/html, application/xhtml+xml, image/jxr, */*

    private final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36";
    private final String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
    private final String acceptLang = "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,id;q=0.6";

    public WebSignUpCall(InstaWebApiClient client) {
        super(client);
    }

    @Override
    public ApiResult work() throws Exception{

        final String email = ApiCall.getParam(this, "email");
        final String name = ApiCall.getParam(this, "name");
        final String firstName = ApiCall.getParam(this, "firstName");
        final String password = ApiCall.getParam(this, "password");


        ApiResult result = ApiQuickCaller.get(getInstaWebApiClient().client,
                "https://www.instagram.com/"
                , InstaApiUtil.makeStringMap(
                        "accept-language", acceptLang
                        , "upgrade-insecure-requests", "1"
                        , "user-agent", userAgent
                        , "accept", accept
                        , "cache-control", "max-age=0"
                        , "authority", "www.instagram.com"
                        , "save-data", "on"
                )
                , null);


        if(result.resultCode != 200){
            throw new ApiException(-100, "err on step 1. 'https://www.instagram.com'");
        }

        // check accout info is valid or not;
        final Map<String, String> mapHeaderForAccountCheck = InstaApiUtil.makeStringMap(
                "origin", "https://www.instagram.com"
                , "accept-language", acceptLang
                , "user-agent", userAgent
                , "x-requested-with", "XMLHttpRequest"
                , "save-data", "on"
                , "x-csrftoken", getInstaWebApiClient().getCsrfToken()
                , "x-instagram-ajax", "1"
                , "accept", "*/*"
                , "Host", "www.instagram.com"
                , "referer", "https://www.instagram.com/"
                , "authority", "www.instagram.com"
        );

        result = ApiQuickCaller.post(getInstaWebApiClient().client,
                "https://www.instagram.com/accounts/web_create_ajax/attempt/"
                , mapHeaderForAccountCheck
                , InstaApiUtil.makeStringMap(
                        "email", email, "password", "", "username", "", "first_name", ""
                ));

        if(result.resultCode != 200){
            throw new ApiException(-100, "err on step 2-1. 'web_create_ajax/attempt/'");
        }

        Thread.sleep(700);
        result = ApiQuickCaller.post(getInstaWebApiClient().client,
                "https://www.instagram.com/accounts/web_create_ajax/attempt/"
                , mapHeaderForAccountCheck
                , InstaApiUtil.makeStringMap(
                        "email", email, "password", "", "username", name, "first_name", ""
                ));

        if(result.resultCode != 200){
            throw new ApiException(-100, "err on step 2-2. 'web_create_ajax/attempt/'");
        }

        Thread.sleep(800);
        result = ApiQuickCaller.post(getInstaWebApiClient().client,
                "https://www.instagram.com/accounts/web_create_ajax/attempt/"
                , mapHeaderForAccountCheck
                , InstaApiUtil.makeStringMap(
                        "email", email, "password", "", "username", name, "first_name", firstName
                ));

        if(result.resultCode != 200){
            throw new ApiException(-100, "err on step 2-3. 'web_create_ajax/attempt/'");
        }

        Thread.sleep(700);
        result = ApiQuickCaller.post(getInstaWebApiClient().client,
                "https://www.instagram.com/accounts/web_create_ajax/attempt/"
                , mapHeaderForAccountCheck
                , InstaApiUtil.makeStringMap(
                        "email", email, "password", password, "username", name, "first_name", firstName
                ));

        if(result.resultCode != 200){
            throw new ApiException(-100, "err on step 2-4. 'web_create_ajax/attempt/'");
        }

        //결과 확인
        JSONObject jAccountChkResult = new JSONObject(result.result);
        if(jAccountChkResult.has("errors")){
            //에러있음. 나중에 결과 분석하자
            JSONObject jErr = jAccountChkResult.getJSONObject("errors");
            if(jErr.has("email")){
                throw new ApiException(-201, "email already taken");
            }

            if(jErr.has("username")){
                JSONArray jNameArr = jAccountChkResult.getJSONArray("username_suggestions");
                for(int i = 0; i < jNameArr.length(); i++){
                    throw new ApiException(-202, "name already taken", jNameArr.getString(i)); //name already taken. so callback name to caller
                }
            }

            throw new ApiException(-200, result.result); //공용 에러
        }

        Thread.sleep(700);
        //가입시작
        result = ApiQuickCaller.post(getInstaWebApiClient().client,
                "https://www.instagram.com/accounts/web_create_ajax/"
                , InstaApiUtil.makeStringMap(
                         "origin", "https://www.instagram.com"
                        , "accept-language", acceptLang
                        , "user-agent", userAgent
                        , "x-requested-with", "XMLHttpRequest"
                        , "save-data", "on"
                        , "x-csrftoken", getInstaWebApiClient().getCsrfToken()
                        , "accept", "*/*"
                        , "referer", "https://www.instagram.com/"
                        , "authority", "www.instagram.com"
                )
                , InstaApiUtil.makeStringMap(
                        "email", email, "password", password, "username", name, "first_name", firstName, "seamless_login_enabled", "1"
                ));

        if(result.resultCode != 200){
            throw new ApiException(-300, "err on step 2-4. 'accounts/web_create_ajax'");
        }

        JSONObject jSignUp = new JSONObject(result.result);
        if(jSignUp.has("errors")){
            JSONObject jsonErrors = jSignUp.getJSONObject("errors");
            if(jsonErrors.has("ip")){
                throw new ApiException(-301, "ip is reported as proxy");
            }
        }
        //가입완료
        String userId = jSignUp.getString("user_id");

        result = ApiQuickCaller.get(getInstaWebApiClient().client,
                "https://www.instagram.com/accounts/registered/"
                , InstaApiUtil.makeStringMap(
                        "accept-language", acceptLang
                        , "upgrade-insecure-requests", "1"
                        , "user-agent", userAgent
                        , "accept", accept
                        , "referer", "https://www.instagram.com/"
                        , "authority", "www.instagram.com"
                        , "save-data", "on"
//                        , "connection", "keep-alive"
//                        , "host", "www.instagram.com"
//                        , "x-instagram-ajax", "1"
//                        , "x-requested-with", "XMLHttpRequest"
                )
                , InstaApiUtil.makeStringMap());

        Thread.sleep(400);
        String query = "viewer()+%7B%0A++eligible_promotions.surface_nux_id(%3Csurface%3E).external_gating_permitted_qps(%3Cexternal_gating_permitted_qps%3E)+%7B%0A++++edges+%7B%0A++++++priority%2C%0A++++++time_range+%7B%0A++++++++start%2C%0A++++++++end%0A++++++%7D%2C%0A++++++node+%7B%0A++++++++id%2C%0A++++++++promotion_id%2C%0A++++++++max_impressions%2C%0A++++++++triggers%2C%0A++++++++template+%7B%0A++++++++++name%2C%0A++++++++++parameters+%7B%0A++++++++++++name%2C%0A++++++++++++string_value%0A++++++++++%7D%0A++++++++%7D%2C%0A++++++++creatives+%7B%0A++++++++++title+%7B%0A++++++++++++text%0A++++++++++%7D%2C%0A++++++++++content+%7B%0A++++++++++++text%0A++++++++++%7D%2C%0A++++++++++footer+%7B%0A++++++++++++text%0A++++++++++%7D%2C%0A++++++++++social_context+%7B%0A++++++++++++text%0A++++++++++%7D%2C%0A++++++++++primary_action%7B%0A++++++++++++title+%7B%0A++++++++++++++text%0A++++++++++++%7D%2C%0A++++++++++++url%2C%0A++++++++++++limit%2C%0A++++++++++++dismiss_promotion%0A++++++++++%7D%2C%0A++++++++++secondary_action%7B%0A++++++++++++title+%7B%0A++++++++++++++text%0A++++++++++++%7D%2C%0A++++++++++++url%2C%0A++++++++++++limit%2C%0A++++++++++++dismiss_promotion%0A++++++++++%7D%2C%0A++++++++++dismiss_action%7B%0A++++++++++++title+%7B%0A++++++++++++++text%0A++++++++++++%7D%2C%0A++++++++++++url%2C%0A++++++++++++limit%2C%0A++++++++++++dismiss_promotion%0A++++++++++%7D%2C%0A++++++++++image+%7B%0A++++++++++++uri%0A++++++++++%7D%0A++++++++%7D%0A++++++%7D%0A++++%7D%0A++%7D%0A%7D";
        String str = URLDecoder.decode(query, "utf-8");

        result = ApiQuickCaller.post(getInstaWebApiClient().client,
                "https://www.instagram.com/qp/fetch_web/"
                , InstaApiUtil.makeStringMap(
                        "origin", "https://www.instagram.com"
                        , "accept-language", acceptLang
                        , "user-agent", userAgent
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
        chkErr(result.result);

        Thread.sleep(300);
        result = ApiQuickCaller.get(getInstaWebApiClient().client,
                "https://www.instagram.com/" + name + "/?__a=1"
                , InstaApiUtil.makeStringMap(
                        "accept-language", acceptLang
                        , "user-agent", userAgent
                        , "accept", "*/*"
                        , "referer", "https://www.instagram.com/" + name + "/"
                        , "authority", "www.instagram.com"
                        , "x-requested-with", "XMLHttpRequest"
                        , "save-data", "on"
                )
                , InstaApiUtil.makeStringMap());

        chkErr(result.result);
        if(result.resultCode == 200){
            JSONObject jsonResult = new JSONObject(result.result);
            JSONObject jsonUserInfo = jsonResult.getJSONObject("user");
            String profilePhotoUrl = jsonUserInfo.getString("profile_pic_url");
            String id = jsonUserInfo.getString("id");
            String username = jsonUserInfo.getString("username");
            int followerCount = jsonUserInfo.getJSONObject("followed_by").getInt("count");
            int followingCount = jsonUserInfo.getJSONObject("follows").getInt("count");

            //로그인됬다고 알림.
            getInstaWebApiClient().onFinishLogin(id, username);
        }

//
//        result = ApiCaller.get(getClient().client,
//                "https://www.instagram.com/" + name + "/"
//                , InstaApiUtil.makeStringMap(
//                        "Accept-Language", acceptLang
//                        , "Accept", "*/*"
//                        , "Connection", "keep-alive"
//                        , "Host", "www.instagram.com"
//                        , "Referer", "https://www.instagram.com/"
//                        , "User-Agent", userAgent
//                        , "X-Requested-With", "XMLHttpRequest"
//                        , "X-Instagram-AJAX", "1")
//                , InstaApiUtil.makeStringMap());

        return result;
    }

    private void chkErr(String jsonResult) throws Exception{
        JSONObject jErrOnGetUserInfo = new JSONObject(jsonResult);
        if(jErrOnGetUserInfo.has("challengeType")){
            String challengeType = jErrOnGetUserInfo.getString("challengeType");
            if(challengeType.equals("SubmitPhoneNumberForm")){
                throw new ApiException(-302, "challengeType SubmitPhoneNumberForm");
            }
        }
    }
}
