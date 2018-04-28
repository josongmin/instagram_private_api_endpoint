package com.studio1221.instagram_api_manager.util;

import com.studio1221.instagram_api_manager.browser.CommonApiClient;
import com.studio1221.instagram_api_manager.browser.InstaAppApiClient;
import com.studio1221.instagram_api_manager.browser.InstaWebApiClient;
import com.studio1221.instagram_api_manager.libs.JoSharedPreference;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;

/**
 * Created by jo on 2017-11-28.
 */

public class BrowserSessionUtil {

    /**브라우저 데이터, 쿠키 저장*/
    public static void saveClientDataToLocal(final CommonApiClient apiClient, final String key){
        final List<CookieTemplateModel> listCookieTemplate = new ArrayList<>();
        for(Cookie cookie : apiClient.cookieStore){
            CookieTemplateModel cookieTemplateModel = new CookieTemplateModel();
            cookieTemplateModel.name = cookie.name();
            cookieTemplateModel.value = cookie.value();
            cookieTemplateModel.domain = cookie.domain();
            cookieTemplateModel.expiresAt = cookie.expiresAt();
            cookieTemplateModel.path = cookie.path();

            listCookieTemplate.add(cookieTemplateModel);
        }

        new Thread(){
            @Override
            public void run() {
                super.run();
                //쿠키저장
                JoSharedPreference.with().push(key+"_cookies", listCookieTemplate);
                //apiClient 에 따라 분기 저장
                if(apiClient instanceof InstaWebApiClient){
                    InstaWebApiClient instaWebApiClient = ((InstaWebApiClient) apiClient);

                    String name = instaWebApiClient.name;
                    String id = instaWebApiClient.id;
                    boolean isWebVersion = instaWebApiClient.isWebVersion;
                    boolean isLogin = instaWebApiClient.isLogin;

                    JoSharedPreference.with().push(key+"_name", name);
                    JoSharedPreference.with().push(key+"_id", id);
                    JoSharedPreference.with().push(key+"_is_web_version", isWebVersion);
                    JoSharedPreference.with().push(key+"_is_login", isLogin);
                    JoSharedPreference.with().push(key+"_browser_type", "InstaWebApiClient");

                    //저장끝.
                }
                else if(apiClient instanceof InstaAppApiClient){
                    InstaAppApiClient instaAppApiClient = ((InstaAppApiClient) apiClient);
//                    String guid, deviceId, phoneId;
//                    String userId, userName, rankToken;
//                    boolean isLogin = false;
                    JoSharedPreference.with().push(key + "_" + "guid", instaAppApiClient.guid);
                    JoSharedPreference.with().push(key + "_" + "deviceId", instaAppApiClient.deviceId);
                    JoSharedPreference.with().push(key + "_" + "phoneId", instaAppApiClient.phoneId);
                    JoSharedPreference.with().push(key + "_" + "userId", instaAppApiClient.userId);
                    JoSharedPreference.with().push(key + "_" + "userName", instaAppApiClient.userName);
                    JoSharedPreference.with().push(key + "_" + "rankToken", instaAppApiClient.rankToken);
                    JoSharedPreference.with().push(key + "_" + "isLogin", instaAppApiClient.isLogin);
                    JoSharedPreference.with().push(key + "_" + "browser_type", "InstaAppApiClient");
                }

                else{
                    JoSharedPreference.with().push(key+"_browser_type", "CommonApiClient");
                }
                //시간저장
                JoSharedPreference.with().push(key+"_datetime", System.currentTimeMillis());
            }
        }.start();

    }

    //로컬에 저장되있는지 확인
    public static boolean isAbleToRestoreFromLocal(final String key){
        Long saveMills = JoSharedPreference.with().get(key+"_datetime");
        if(saveMills == null) return false;
        long currentTimeMills = System.currentTimeMillis();
        if(currentTimeMills - saveMills < 1000 * 60 * 60 * 24 * 30){
            //위 시간 초과하면 안씀. 새로 세션 시작.
            return false;
        }

        return true;
    }

    public static <T extends CommonApiClient> T restoreCookiesFromLocal(final String key){
        //시간땡겨오고
        Long saveMills = JoSharedPreference.with().get(key+"_datetime");
        if(saveMills == null) return null;
        long currentTimeMills = System.currentTimeMillis();
        if(currentTimeMills - saveMills < 1000 * 60 * 60 * 24 * 30){
            //위 시간 초과하면 안씀. 새로 세션 시작.
            return null;
        }

        final String browserType = JoSharedPreference.with().get(key + "_browser_type");
        final List<CookieTemplateModel> listCookieTemplates = JoSharedPreference.with().get(key + "_cookies");
        CommonApiClient resultApiClient = null;

        //웹브라우저용 복구
        if(browserType.equals("InstaWebApiClient")){
            String name = JoSharedPreference.with().get(key+"_name");
            String id = JoSharedPreference.with().get(key+"_id");
            Boolean isWebVersion = JoSharedPreference.with().get(key+"_is_web_version");
            Boolean isLogin = JoSharedPreference.with().get(key+"_is_login");

            InstaWebApiClient instaWebApiClient = new InstaWebApiClient();
            instaWebApiClient.name = name;
            instaWebApiClient.id = id;
            instaWebApiClient.isWebVersion = isWebVersion;
            instaWebApiClient.isLogin = isLogin;


            resultApiClient = instaWebApiClient;
        }
        else if(browserType.equals("InstaAppApiClient")){
            //do something later
            InstaAppApiClient instaAppApiClient = new InstaAppApiClient();

            instaAppApiClient.guid = JoSharedPreference.with().get(key + "_" + "guid");
            instaAppApiClient.deviceId = JoSharedPreference.with().get(key + "_" + "deviceId");
            instaAppApiClient.phoneId = JoSharedPreference.with().get(key + "_" + "phoneId");
            instaAppApiClient.userId = JoSharedPreference.with().get(key + "_" + "userId");
            instaAppApiClient.userName = JoSharedPreference.with().get(key + "_" + "userName");
            instaAppApiClient.rankToken = JoSharedPreference.with().get(key + "_" + "rankToken");
            instaAppApiClient.isLogin = JoSharedPreference.with().get(key + "_" + "isLogin");

            resultApiClient = instaAppApiClient;
        }

        else{
            resultApiClient = new CommonApiClient();
        }

        //쿠키 복구
        for(CookieTemplateModel cookieModel : listCookieTemplates){
            resultApiClient.createCookie(cookieModel.domain, cookieModel.name, cookieModel.value, cookieModel.expiresAt, cookieModel.path);
        }

        return (T)resultApiClient;
    }

    //쿠키 관리용 임시 모델
    public static class CookieTemplateModel{
        public String name, value, domain;
        public long expiresAt;
        public String path;

    }


}
