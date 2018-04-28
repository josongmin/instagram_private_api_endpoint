package com.studio1221.instagram_api_manager.endpoint.instagram_app;

import android.util.Log;

import com.studio1221.instagram_api_manager.browser.InstaAppApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.api_call.InstaAppApiCall;
import com.studio1221.instagram_api_manager.browser.api_quick_caller.ApiQuickCaller;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;
import com.studio1221.instagram_api_manager.variables.Variables;

import java.util.Map;

/**
 * Created by jo on 2017-11-13.
 */

public class AppTwoFactorLoginCall extends InstaAppApiCall {

    public AppTwoFactorLoginCall(InstaAppApiClient client) {
        super(client);
    }

    @Override
    public ApiResult work() throws Exception{

        final String url = "https://i.instagram.com/api/v1/accounts/two_factor_login/";

        final String verificationCode = ApiCall.getParam(this, "verificationCode");
        final String twoFactorIdentifier = ApiCall.getParam(this, "twoFactorIdentifier");
        final String username = getInstaAppApiClient().userName;
        final String deviceId = getInstaAppApiClient().deviceId;
        final String password = getInstaAppApiClient().password;

        ApiResult resultModel = ApiQuickCaller.postForAppInstagram(getInstaAppApiClient().client, url
                , makeHeaderWithCommonHeader("Connection", "close")
                , InstaApiUtil.makeStringMap(
                        "verification_code", verificationCode
                        ,  "two_factor_identifier", twoFactorIdentifier
                        ,  "_csrftoken", getInstaAppApiClient().getCsrfToken()
                        ,  "username", username
                        ,  "device_id", deviceId
                        ,  "password", password)
        );

        //{"logged_in_user": {"pk": 366477841, "username": "domangchu", "full_name": "song", "is_private": false, "profile_pic_url": "https://scontent-sit4-1.cdninstagram.com/t51.2885-19/s150x150/23596028_719986838207213_8868409907716554752_n.jpg", "profile_pic_id": "1649559323673740513_366477841", "is_verified": false, "has_anonymous_profile_picture": false, "is_business": false, "can_see_organic_insights": false, "show_insights_terms": false, "allow_contacts_sync": true, "phone_number": "+821093149449", "country_code": 82, "national_number": 1093149449}, "status": "ok"}
        Log.d(Variables.LOG_TAG, resultModel.result);
        if(resultModel.resultCode == 200){
            Map<String, Object> mapResult = getResultMap(resultModel.result);
            Map<String, Object> mapUser = (Map<String, Object>)mapResult.get("logged_in_user");
            String userId = (String)mapUser.get("pk");
            getInstaAppApiClient().onLoginComplete(userId);
        }else{
            setErrorResult(resultModel);
        }

        return resultModel;
    }
}
