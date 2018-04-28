package com.studio1221.instagram_api_manager.browser.api_call;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.studio1221.instagram_api_manager.browser.ApiResultCallback;
import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jo on 2017-11-21.
 */

public abstract class ApiCall<T> {

    /**get param*/
    public static <E> E getParam(ApiCall apiCall, String key){
        return (E)apiCall.mapParams.get(key);
    }

    public static <E> E getParam(ApiCall apiCall, String key, E defaultValue){

        if(apiCall.mapParams.get(key) == null){
            return defaultValue;
        }else{
            return (E)apiCall.mapParams.get(key);
        }

    }

    Map<String, Object> mapParams = new HashMap<>();

    public ApiCall<T> setParams(Object ... params) {
        for(int i = 0; i < params.length; i+=2){
            mapParams.put((String)params[i], params[i+1]);
        }
        return this;
    }

    public ApiCall<T> setParam(String key, Object value){
        mapParams.put(key, value);
        return this;
    }

    protected abstract ApiResult<T> work() throws Exception;

    /**동작 시작 익셉션 핸들링함. 실패시 errObject에 값 들어감.*/
    public ApiResult<T> syncWork(){

        ApiResult<T> result = null;
        try{
            result = work();
        }

        catch(ApiException e){
            result = new ApiResult<T>()
                    .setResultCode(e.getCode())
                    .setResult(e.getMessage())
                    .setErrObject(e.obj);
        }
        catch(Exception e){
            result = new ApiResult<T>()
                    .setResultCode(-100)
                    .setResult(e.getMessage())
                    .setErrObject(e.getMessage());
        }
        return result;
    }

    /**백그라운드 실행*/
    public void asyncWork(final ApiResultCallback<T> apiResultCallback){
        new AsyncTask<Integer, Integer, ApiResult<T>>(){
            @Override
            protected ApiResult<T> doInBackground(Integer... integers) {
                return syncWork();
            }

            @Override
            protected void onPostExecute(ApiResult<T> apiResult) {
                super.onPostExecute(apiResult);
                apiResultCallback.onFinish(apiResult);
            }
        }.execute(1);
    }

    public void setErrorResult(ApiResult resultModel){
        resultModel.setErrObject(getResultMap(resultModel.result));
    }

    public Map<String, Object> getResultMap(String jsonResult){
        Map<String,Object> mapResult = new HashMap<>();
        mapResult = (Map<String,Object>) new Gson().fromJson(jsonResult, mapResult.getClass());
        return mapResult;
    }
}
