package com.studio1221.instagram_api_manager.browser.model;

/**
 * Created by jo on 2017-11-10.
 */

public class ApiResult<E> {
    public int resultCode;
    public String result;
    public E object;
    public Object errObject;

    public ApiResult(){

    }
    public ApiResult(int resultCode, String result) {
        this.resultCode = resultCode;
        this.result = result;
    }

    public ApiResult(int resultCode, String result, E obj) {
        this.resultCode = resultCode;
        this.result = result;
        this.object = obj;
    }

    public ApiResult(int resultCode, String result, E obj, Object errObject) {
        this.resultCode = resultCode;
        this.result = result;
        this.object = obj;
        this.errObject = errObject;
    }

    public ApiResult setResultCode(int resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public ApiResult setResult(String result) {
        this.result = result;
        return this;
    }

    public ApiResult setObject(E object) {
        this.object = object;
        return this;
    }

    public ApiResult setErrObject(Object errObject) {
        this.errObject = errObject;
        return this;
    }

    public String getSummaryForLog(){
        if(errObject != null){
            return resultCode + " " + result + " " + errObject;
        }else{
            return resultCode + " " + result + " " + object;
        }
    }
    public boolean fromException(){
        return errObject != null;
    }
}
