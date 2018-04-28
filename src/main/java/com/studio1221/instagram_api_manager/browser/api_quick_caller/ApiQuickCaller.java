package com.studio1221.instagram_api_manager.browser.api_quick_caller;


import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;

import org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jo on 2017-11-13.
 */

public class ApiQuickCaller {

    public final static ApiResult postForAppInstagram(final OkHttpClient okHttpClient, final String url, final Map<String, String> mapHeader, final Map<String, String> mapParam) {
        String paramString = InstaApiUtil.makeParamForInstagram(mapParam);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), paramString);
        return post(okHttpClient, url, mapHeader, requestBody);
    }

    /**url encoded post*/
    public final static ApiResult post(final OkHttpClient okHttpClient, final String url, final Map<String, String> mapHeader, final Map<String, String> mapParam) {

        FormBody.Builder formBodyBulder = new FormBody.Builder();

        if(mapParam != null){
            Iterator<Map.Entry<String, String>> itr = mapParam.entrySet().iterator();

            while (itr.hasNext()) {
                Map.Entry<String, String> entry = itr.next();
                String key = entry.getKey();
                String value = entry.getValue();
                formBodyBulder.add(key, value);
            }
        }

        FormBody formBody = formBodyBulder.build();

        return post(okHttpClient, url, mapHeader, formBody);
    }

    public final static ApiResult post(final OkHttpClient okHttpClient, final String url, final Map<String, String> mapHeader, RequestBody requestBody) {
        Request.Builder builder = new Request.Builder();
        if(mapHeader != null){
            Iterator<Map.Entry<String, String>> itr = mapHeader.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, String> entry = itr.next();
                String key = entry.getKey();
                String value = entry.getValue();
                builder.addHeader(key, value);
            }
        }

        Request request = builder
                .url(url)
                .post(requestBody)
                .build();

        //처리 ㄱㄱ
        try {
            Response response = okHttpClient.newCall(request).execute();
            String result = IOUtils.toString(response.body().byteStream(), StandardCharsets.UTF_8);
            if (response.code() != 200) {
                throw new ApiException(response.code(), result);
            }

            return new ApiResult(response.code(), result);

        } catch (ApiException e) {
            return new ApiResult(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return new ApiResult(-100, e.getMessage());
        }
    }

    public final static ApiResult get(final OkHttpClient okHttpClient, final String url, final Map<String, String> mapHeader, final Map<String, String> mapParams) {
        Request.Builder builder = new Request.Builder();

        if(mapHeader != null){
            Iterator<Map.Entry<String, String>> itr = mapHeader.entrySet().iterator();

            while (itr.hasNext()) {
                Map.Entry<String, String> entry = itr.next();
                String key = entry.getKey();
                String value = entry.getValue();
                builder.addHeader(key, value);
            }
        }


        String urlWithParam = url;
        if(mapParams != null){
            Iterator<Map.Entry<String, String>> itrParam = mapParams.entrySet().iterator();
            int i = 0;
            while (itrParam.hasNext()) {
                Map.Entry<String, String> entry = itrParam.next();
                String key = entry.getKey();
                String value = entry.getValue();
                urlWithParam += (i == 0 ? "?" : "&") + key + "=" + value;
                i++;
            }
        }


        Request request = builder
                .url(urlWithParam)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            String result = IOUtils.toString(response.body().byteStream(), StandardCharsets.UTF_8);
            if (response.code() != 200) {
                throw new ApiException(response.code(), result);
            }

            return new ApiResult(response.code(), result);

        } catch (ApiException e) {
            return new ApiResult(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return new ApiResult(-100, e.getMessage());
        }
    }
}