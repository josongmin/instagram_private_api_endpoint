package com.studio1221.instagram_api_manager.browser;

import com.google.gson.Gson;
import com.studio1221.instagram_api_manager.browser.api_quick_caller.ApiQuickCaller;
import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by jo on 2017-11-10.
 */

public class InstaWebApiClient extends CommonApiClient {

    public final static String USERAGENT_WEB = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36";
    public final static String ACCEPT_WEB = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
    public final static String ACCEPTLANG_WEB = "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,id;q=0.6";

    public final static String USERAGENT_MOBILEWEB = "Mozilla/5.0 (Linux; Android 4.4.2; Nexus 4 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.114 Mobile Safari/537.36";
    public final static String ACCEPT_MOBILEWEB = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
    public final static String ACCEPTLANG_MOBILEWEB = "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,id;q=0.6";

    String acceptLang, userAgent, accept;

    public boolean isWebVersion = true;
    public boolean isLogin = false;
    public String id, name;

    @Override
    protected void init() {
        super.init();
        initDefaultForPcWeb();
    }

    public void createCookie(String key, String value) {
        super.createCookie("www.instagram.com", key, value);
    }

    public void initDefaultForPcWeb(){
        acceptLang = ACCEPTLANG_WEB;
        userAgent = USERAGENT_WEB;
        accept = ACCEPT_WEB;
        isWebVersion = true;
        createCookie("ig_pr", "2.5");
        createCookie("ig_vw", "1536");
        createCookie("ig_vh", "734");
        createCookie("ig_or", "landscape-primary");
        createCookie("ig_oia_dismiss", "true");
    }

    public void initDefaultForMobileWeb(){
        acceptLang = ACCEPTLANG_MOBILEWEB;
        userAgent = USERAGENT_MOBILEWEB;
        accept = ACCEPT_MOBILEWEB;
        isWebVersion = false;

        createCookie("ig_pr", "2.0000000298023224");
        createCookie("ig_vw", "600");
        createCookie("ig_vh", "960");
        createCookie("ig_or", "portrait-primary");
        createCookie("ig_oia_dismiss", "true");
    }

    public String getCsrfToken(){
        return getCookieValue("csrftoken");
    }

    /**로그인, 회원가입 완료ㅗ딨을때 올려주자*/
    public void onFinishLogin(String id, String name){
        isLogin = true;
        this.id  = id;
        this.name = name;
    }

    //static
    public static Map<String, String> getDefaultHeader(){
        return InstaApiUtil.makeStringMap(
//                "Accept-Encoding", "gzip, deflate, br"
                "Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,id;q=0.6"
                , "Connection", "keep-alive"
                , "Host", "www.instagram.com"
                , "Origin", "https://www.instagram.com"
                , "Referer", "https://www.instagram.com/"
                , "User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36"
                , "X-Instagram-AJAX", "1"
                , "X-Requested-With", "XMLHttpRequest"
        );
    }

    public static Map<String, String> makeHeaderWithDefault(String ... keyValues){
        Map<String, String> mapHeader = getDefaultHeader();
        for(int i = 0; i < keyValues.length; i+= 2){
            mapHeader.put(keyValues[i], keyValues[i+1]);
        }

        return mapHeader;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getAccept() {
        return accept;
    }

    public String getAcceptLang() {
        return acceptLang;
    }

    /**로그인*/
    public ApiResult login(String userName, String password) throws Exception{
        ApiResult result = ApiQuickCaller.get(client, "https://www.instagram.com/", getDefaultHeader(), null);
        if(result.resultCode != 200) throw new ApiException(-100, "first login failed");

        Thread.sleep(800);
        result = ApiQuickCaller.post(client, "https://www.instagram.com/accounts/login/ajax/"
                , makeHeaderWithDefault("X-CSRFToken", getCsrfToken())
                , InstaApiUtil.makeStringMap("username", userName, "password", password));

        //실패 처리
        if(result.resultCode != 200){
            JSONObject jsonResult = new JSONObject(result.result);
            if(jsonResult.has("two_factor_required")){
                boolean twoFactor = jsonResult.getBoolean("two_factor_required");
                if(twoFactor){
                    throw new ApiException(-111, "You have to turn off two factor login.");
                }
            }

            if(jsonResult.getString("user").equals("false")){
                throw new ApiException(-6, "Invalid username.");
            }

            if(jsonResult.getString("authenticated").equals("false")){
                throw new ApiException(-5, "Check your password.");
            }

            if(!jsonResult.getString("status").equals("ok")){
                throw new ApiException(-7, "blocked user");
            }

            throw new ApiException(-2, "faild on login");
        }

        result = ApiQuickCaller.get(client, "https://www.instagram.com/"
                , makeHeaderWithDefault("X-CSRFToken", getCsrfToken())
                , null);

        result = ApiQuickCaller.get(client, "https://www.instagram.com/" + userName + "?__a=1"
                , makeHeaderWithDefault("X-CSRFToken", getCsrfToken())
                , null);

        if(result.resultCode == 200){
            JSONObject jsonResult = new JSONObject(result.result);
            JSONObject jsonUserInfo = jsonResult.getJSONObject("user");
            String profilePhotoUrl = jsonUserInfo.getString("profile_pic_url");
            String id = jsonUserInfo.getString("id");
            String username = jsonUserInfo.getString("username");
            int followerCount = jsonUserInfo.getJSONObject("followed_by").getInt("count");
            int followingCount = jsonUserInfo.getJSONObject("follows").getInt("count");

            onFinishLogin(id, username);
        }


        removeCookie("target");
        return result;
    }

    public void switchBrowser(boolean toMobile) throws Exception{
        if(toMobile){
            initDefaultForMobileWeb();
        }else{
            initDefaultForPcWeb();
        }

        //1. http://www.instagram.com/내이름
        ApiResult result = ApiQuickCaller.get(client,
                "https://www.instagram.com/"
                , InstaApiUtil.makeStringMap(
                        "accept-language", acceptLang
                        , "pragma", "no-cache"
                        , "upgrade-insecure-requests", "1"
                        , "user-agent", userAgent
                        , "accept", accept
                        , "cache-control", "no-cache"
                        , "authority", "www.instagram.com"
                        , "save-data", "on"
                )
                , null);

        ApiException.chkApiException(result);

        String queryParam = new Gson().toJson(InstaApiUtil.makeStringMap("id", id));
        //2. https://www.instagram.com/graphql/query/?query_id=17845312237175864&variables={"id":"6330709883"}
        ApiResult result2 = ApiQuickCaller.get(client,
                "https://www.instagram.com/graphql/query/?query_id=17845312237175864&variables=" + URLEncoder.encode(queryParam, "utf-8")
                , InstaApiUtil.makeStringMap(
                        "accept-language", acceptLang
                        , "pragma", "no-cache"
                        , "user-agent", userAgent
                        , "accept", "*/*"
                        , "authority", "www.instagram.com"
                        , "x-requested-with", "XMLHttpRequest"
                        , "referer", "https://www.instagram.com/" + name + "/"
                        , "cache-control", "no-cache"
                        , "save-data", "on"
                )
                , null);

        ApiException.chkApiException(result2);
    }
}
