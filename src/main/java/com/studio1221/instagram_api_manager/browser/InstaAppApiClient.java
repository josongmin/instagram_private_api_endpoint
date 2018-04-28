package com.studio1221.instagram_api_manager.browser;


import com.google.gson.Gson;
import com.studio1221.instagram_api_manager.browser.api_quick_caller.ApiQuickCaller;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.libs.JoSharedPreference;
import com.studio1221.instagram_api_manager.model.InstaLoginUser;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by jo on 2017-11-10.
 */

public class InstaAppApiClient extends CommonApiClient {

    public String guid, deviceId, phoneId;
    public String userId, userName, rankToken;
    public String password;
    public boolean isLogin = false;
    public InstaLoginUser instaLoginUser;

    public String getCsrfToken(){
        return getCookieValue("csrftoken");
    }

    public void createCookie(String key, String value) {
        super.createCookie("www.instagram.com", key, value);
    }

    public String getAcceptLang(){
        String lang = InstaApiUtil.localeToBcp47Language(Locale.getDefault());
        return lang;
    }

    public void onLoginComplete(String userId){
        this.userId = userId;
        this.rankToken = this.userId + "_" + this.guid;
        isLogin = true;
    }

    public ApiResult login(String userName, String password) throws Exception{
        return login(userName, password, false);
    }

    public ApiResult login(String userName, String password, boolean restoreDeviceInfo) throws Exception{
        final String url = "https://i.instagram.com/api/v1/accounts/login/";

        String guid = null, phoneId = null, deviceId = null;

        if(restoreDeviceInfo){
            guid = JoSharedPreference.with().get("guid_" + userName);
            phoneId = JoSharedPreference.with().get("phoneId_" + userName);
            deviceId = JoSharedPreference.with().get("deviceId_" + userName);
        }

        if(guid == null)        guid = InstaApiUtil.getRandomUUID(true);
        if (deviceId == null)   deviceId = InstaApiUtil.getDeviceId(true);
        if(phoneId == null)     phoneId = InstaApiUtil.getPhoneId(true);

        JoSharedPreference.with().push("guid_" + userName, guid);
        JoSharedPreference.with().push("phoneId_" + userName, phoneId);
        JoSharedPreference.with().push("deviceId_" + userName, deviceId);

        //
        ApiResult resultModel = ApiQuickCaller.postForAppInstagram(client, url
                , InstaApiUtil.makeStringMap(
                        "Host", "i.instagram.com"
                        , "Accept", "*/*"
                        , "Connection", "close"
                        , "Cookie2", "$Version=1"
                        , "Accept-Language", getAcceptLang()
                        , "X-IG-Capabilities", "3Q=="
                        , "X-IG-Connection-Type", "WIFI"
                        , "User-Agent", InstaApiUtil.getMobileAppUserAgent()
                )
                , InstaApiUtil.makeStringMap(
                        "device_id", deviceId
                        ,  "guid", guid
                        ,  "phone_id", phoneId
                        ,  "username", userName
                        ,  "password", password
                        ,  "login_attempt_count", "0")
        );

        //브라우저에 고정값 설정
        this.guid = guid;
        this.deviceId = deviceId;
        this.phoneId = phoneId;
        this.userName = userName;
        this.password = password;

//        {"message": "The password you entered is incorrect. Please try again.", "invalid_credentials": true, "error_title": "Incorrect password for domangchu", "buttons": [{"title": "Try Again", "action": "dismiss"}], "status": "fail", "error_type": "bad_password"}
//        {"message": "", "two_factor_required": true, "two_factor_info": {"username": "domangchu", "obfuscated_phone_number": "9449", "two_factor_identifier": "HS4n5Tjm10", "show_messenger_code_option": false, "phone_verification_settings": {"max_sms_count": 2, "resend_sms_delay_sec": 60, "robocall_after_max_sms": true, "robocall_count_down_time_sec": 30}}, "phone_verification_settings": {"max_sms_count": 2, "resend_sms_delay_sec": 60, "robocall_after_max_sms": true, "robocall_count_down_time_sec": 30}, "status": "fail"}
        if(resultModel.resultCode == 200){
            JSONObject jUser = new JSONObject(resultModel.result).getJSONObject("logged_in_user");
            String userId = jUser.getString("pk");
//        instaAppLoginProfile.fullName = jUser.getString("full_name");
//        instaAppLoginProfile.photoThumbUrl = jUser.getString("profile_pic_url");
//        instaAppLoginProfile.photoUrl = InstaApiUtil.getThumbImgUrl(InstaApiUtil.IMG_ORIGINAL, jUser.getString("profile_pic_url"));
            onLoginComplete(userId);
        }else{
            Map<String,Object> mapResult = new HashMap<>();
            mapResult = (Map<String,Object>) new Gson().fromJson(resultModel.result, mapResult.getClass());
            resultModel.setErrObject(mapResult);
        }


        return resultModel;
    }


}
