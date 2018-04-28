package com.studio1221.instagram_api_manager.endpoint.instagram_app;

import com.studio1221.instagram_api_manager.browser.InstaAppApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.api_call.InstaAppApiCall;
import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.model.InstaLoginUser;

public class AppLoginCall extends InstaAppApiCall {

    public AppLoginCall(InstaAppApiClient client) {
        super(client);
    }

    @Override
    public ApiResult work() throws Exception{

        String userName = ApiCall.getParam(this, "userName");
        String password = ApiCall.getParam(this, "password");
        Boolean loadUserInfo = ApiCall.getParam(this, "loadUserInfo", false);
        Boolean restoreDeviceInfo = ApiCall.getParam(this, "restoreDeviceInfo", new Boolean(true));

        ApiResult apiResult = getInstaAppApiClient().login(userName, password, restoreDeviceInfo);
        ApiException.chkApiException(apiResult);

        if(loadUserInfo){
            ApiResult<InstaLoginUser> userApiResult = new AppCurrentUserInfo(getInstaAppApiClient()).syncWork();
            ApiException.chkApiException(userApiResult);

            getInstaAppApiClient().instaLoginUser = userApiResult.object;
        }

        //
        ApiResult result = new ApiResult();
        result.setResultCode(200);
        result.setResult("success");
        return result;
    }
}
