package com.studio1221.instagram_api_manager.endpoint.common;

import com.studio1221.instagram_api_manager.browser.CommonApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.api_quick_caller.ApiQuickCaller;
import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;
import com.studio1221.instagram_api_manager.variables.EndPointUrl;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by jo on 2017-11-21.
 */

public class GetRandomMockAccountInfoCall extends ApiCall<GetRandomMockAccountInfoCall.MockAccountInfoModel> {

    @Override
    public ApiResult<GetRandomMockAccountInfoCall.MockAccountInfoModel> work() throws Exception {

        String national = getParam(this, "national");
        if(national == null){
            throw new ApiException(-200, "national not ready");
        }

        ApiResult result = ApiQuickCaller.get(new CommonApiClient().client,
                EndPointUrl.URL_SUPERFAMOUS + "insta_mockid/get_mock_account/" + national
                , null
                , null);

        JSONObject jResult = new JSONObject(result.result).getJSONObject("obj");
        Map<String, String> mapMockData = InstaApiUtil.makeStringMap(
                "name", jResult.getString("name")
                , "firstName", jResult.getString("first_name"));


        return new ApiResult<>(200, result.result, new MockAccountInfoModel(jResult.getString("name"), jResult.getString("first_name")));
    }

    public static class MockAccountInfoModel{
        public String name, firstName;

        public MockAccountInfoModel(String name, String firstName) {
            this.name = name;
            this.firstName = firstName;
        }
    }
}
