package com.studio1221.instagram_api_manager.transaction_service;

import android.util.Log;

import com.google.gson.Gson;
import com.studio1221.instagram_api_manager.browser.InstaWebApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.api_quick_caller.ApiQuickCaller;
import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.endpoint.instagram_web.WebLoadFollowerCall;
import com.studio1221.instagram_api_manager.endpoint.instagram_web.WebLoginCall;
import com.studio1221.instagram_api_manager.model.InstaUser;
import com.studio1221.instagram_api_manager.model.InstaUsers;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;
import com.studio1221.instagram_api_manager.util.RandomIdManager;
import com.studio1221.instagram_api_manager.variables.EndPointUrl;
import com.studio1221.instagram_api_manager.variables.Variables;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Created by jo on 2017-11-21.
 */

public class MakeBulkMockInstagramAccountTService extends ApiCall {

    InstaWebApiClient webApiClient = new InstaWebApiClient();
    String targetUserName, targetUserId, national;
    int size;
    boolean going = true;

    public MakeBulkMockInstagramAccountTService(String targetUserName, String targetUserId, String national, int size) {
        this.targetUserName = targetUserName;
        this.targetUserId = targetUserId;
        this.national = national;
        this.size = size;
    }

    @Override
    public ApiResult work() throws Exception {
        if(!webApiClient.isLogin){
            ApiResult<String> result = new WebLoginCall(webApiClient).setParams("userName", "loremi249517", "password", "whthdals").syncWork();
            ApiException.chkApiException(result);
        }

        InstaUsers users;
        int count = 0;
        String lastItemKey = null;

        List<Map<String, String>> listParamForMockData = new ArrayList<>();
        do {
            ApiResult<InstaUsers> followerResult = new WebLoadFollowerCall(webApiClient)
                    .setParams("targetUserName", targetUserName, "targetUserId", targetUserId, "loadSize", Math.min(3000, size), "lastItemKey", lastItemKey).syncWork();

            ApiException.chkApiException(followerResult);

            users = followerResult.object;
            lastItemKey = users.endCursor;
            //처리
            for(InstaUser user : users.listUsers){

                String originalUserId = user.id;
                String name = RandomIdManager.makeRandomIdFromId(user.userName);
                String firstName = user.fullName;
                if(firstName!= null && firstName.length() > 4){
                    firstName = firstName.substring(0, 4);
                }

                Map<String, String> mapParam = InstaApiUtil.makeStringMap(
                        "name", name, "firstName", firstName, "originalUserId", originalUserId, "national", national
                );
                listParamForMockData.add(mapParam);
                count++;
                Log.d(Variables.LOG_TAG, "Making mock data params... " + count + "/" + users.countForDisplay );

                if(count > size){
                    Log.d(Variables.LOG_TAG, "목표개수 {size} 초과해서 목록 정리 중단.".replace("{size}", size+""));
                    going = false;
                    break;
                }
            }
            Log.d(Variables.LOG_TAG, "Posting bulk mock data / " + count);

            String paramForMockData = new Gson().toJson(listParamForMockData);

            startPostMockAccountInfo(paramForMockData);
            listParamForMockData.removeAll(listParamForMockData);

        }while(users.hasNextPage && going);

        return new ApiResult<>(200, "success");
    }

    //쓰레드로 제출 시작
    private void startPostMockAccountInfo(String paramForMockData){
        new WorkThread(paramForMockData){
            @Override
            public void run() {
                super.run();
                postMockInstaId((String)getObject());
            }
        }.start();
    }

    //제출
    private ApiResult postMockInstaId(String params){
        ApiResult result = ApiQuickCaller.post(new OkHttpClient(),
                EndPointUrl.URL_SUPERFAMOUS + "insta_mockid/new_mock_account/bulk"
                , null
                , InstaApiUtil.makeStringMap("params", params));
        return result;
    }

    class WorkThread extends Thread{
        Object object;
        public WorkThread(Object obj){
            this.object = obj;
        }

        public Object getObject() {
            return object;
        }
    }

}
