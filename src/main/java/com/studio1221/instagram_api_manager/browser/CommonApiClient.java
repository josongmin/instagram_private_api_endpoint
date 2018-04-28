package com.studio1221.instagram_api_manager.browser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Created by jo on 2017-11-10.
 */

public class CommonApiClient {

    public final Set<Cookie> cookieStore = new HashSet<>();
    public OkHttpClient client;

    public CommonApiClient(){
        init();
    }

    protected void init(){
        client = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        for(Cookie cookie : cookies){
                            replaceCookie(cookie);
//                            cookieStore.addAll(cookies);
                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> validCookies = new ArrayList<>();
                        for(Cookie cookie: cookieStore) {
                            if(cookie.expiresAt() < System.currentTimeMillis()) {
                                ;
                            } else {
                                validCookies.add(cookie);
                            }
                        }
                        return validCookies;
                    }
                })
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public void createCookie(String domain, String key, String value, Long expiryAt, String path){

        replaceCookie(new Cookie.Builder()
                .name(key)
                .value(value)
                .domain(domain)
                .path(path)
                .expiresAt(expiryAt)
                .build());
    }

    public void createCookie(String domain, String key, String value){

        replaceCookie(new Cookie.Builder()
                .name(key)
                .value(value)
                .domain(domain)
                .path("/")
                .build());
    }

    public void replaceCookie(Cookie targetCookie){
        Iterator<Cookie> itr = cookieStore.iterator();
        Cookie cookieRemove = null;
        while(itr.hasNext()){
            Cookie cookie = itr.next();
            if(cookie.name().equals(targetCookie.name())){
                cookieRemove = cookie;
                break;
            }
        }

        cookieStore.remove(cookieRemove);
        cookieStore.add(targetCookie);
    }

    public void removeCookie(String cookieName){
        Cookie cookieTarget = null;
        for(Cookie cookie: client.cookieJar().loadForRequest(null)) {
            if(cookie.name().equalsIgnoreCase(cookieName)) {
                cookieTarget = cookie;
                break;
            }
        }

        if(cookieTarget != null){
            cookieStore.remove(cookieTarget);
        }
    }

    public String getCookieValue(String cookieName){
        for(Cookie cookie: client.cookieJar().loadForRequest(null)) {
            if(cookie.name().equalsIgnoreCase(cookieName)) {
                return cookie.value();
            }
        }
        return null;
    }


}
