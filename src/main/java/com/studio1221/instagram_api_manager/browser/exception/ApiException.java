package com.studio1221.instagram_api_manager.browser.exception;


import com.google.gson.Gson;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jo on 2017-11-10.
 */

public class ApiException extends Exception{
    public int code;
    public String message;
    public Object obj;

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ApiException(int code, String message, Object obj) {
        super(message);
        this.code = code;
        this.message = message;
        this.obj = obj;
    }

    public int getCode() {
        return code;
    }

    public final static void chkApiException(final ApiResult apiResult) throws ApiException{

        if (apiResult.resultCode != 200) {

            if(apiResult.object == null){
                try{
                    Map<String,Object> mapResult = new HashMap<>();
                    mapResult = (Map<String,Object>) new Gson().fromJson(apiResult.result, mapResult.getClass());

                    throw new ApiException(apiResult.resultCode, apiResult.result, mapResult);
                }catch(Exception e){
                    throw e;
                }
            }

            throw new ApiException(apiResult.resultCode, apiResult.result, apiResult.object);
        }

    }
}
