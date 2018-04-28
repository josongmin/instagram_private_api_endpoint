package com.studio1221.instagram_api_manager.endpoint.instagram_web;


import com.studio1221.instagram_api_manager.browser.InstaWebApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.api_call.InstaWebApiCall;
import com.studio1221.instagram_api_manager.browser.api_quick_caller.ApiQuickCaller;
import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;

/**
 * Created by jo on 2017-11-13.
 */

public class WebLikePostCall extends InstaWebApiCall {

    //
    public final static String REFERERTYPE_IN_USER_PAGE = "IN_USER_PAGE";
    public final static String REFERERTYPE_BY_TAG = "BY_TAG";

    public WebLikePostCall(InstaWebApiClient client) {
        super(client);
    }

    @Override
    public ApiResult work() throws Exception{

        Boolean fromPostDetailPage = (Boolean) ApiCall.getParam(this, "fromPostDetailPage");
        String postId = ApiCall.getParam(this, "postId");
        String postCode = ApiCall.getParam(this, "postCode");
        String refererType = ApiCall.getParam(this, "refererType");
        String refererString = null;


        if (refererType.equals(REFERERTYPE_IN_USER_PAGE)) {
            //https://www.instagram.com/p/Bbi45dggXuh/?taken-by=afetsener
            String userName = ApiCall.getParam(this, "userName");
            if(userName == null)  throw new ApiException(-100, "userName not ready", null);
            refererString = "https://www.instagram.com/p/" + postCode + "/?taken-by=" + userName;
        }
        else if(refererType.equals(REFERERTYPE_BY_TAG)){
            String searchTag = ApiCall.getParam(this, "searchTag");
            if(searchTag == null)  throw new ApiException(-100, "searchTag not ready", null);
            refererString = "https://www.instagram.com/p/" + postCode + "/?tagged=" + InstaApiUtil.encodeUTF8(searchTag);
        }

        if(refererString == null){
            throw new ApiException(-100, "refererString not ready", null);
        }

        if(fromPostDetailPage == null){
            fromPostDetailPage = false;
        }

        //디테일페이지엥서 왔으면 안열어줘도댐.
        if(!fromPostDetailPage){
            ApiResult firstResult = ApiQuickCaller.get(getInstaWebApiClient().client,
                    "https://www.instagram.com/p/" + postCode + "/?__a=1"
                    , InstaApiUtil.makeStringMap(
                            "accept-language", getInstaWebApiClient().getAcceptLang()
                            , "user-agent", getInstaWebApiClient().getUserAgent()
                            , "accept", "*/*"
                            , "x-requested-with", "XMLHttpRequest"
                            , "referer", refererString
                            , "save-data", "on"
                    )
                    , InstaApiUtil.makeStringMap());

        }


        //ex) https://www.instagram.com/p/Bbjep9gl9ZK/?explore=true
        //ex) https://www.instagram.com/p/Bbjep9gl9ZK/?tagged=%EB%A7%9E%ED%8C%94

        //referer https://www.instagram.com/p/BbkXOM-n1Pw/?tagged=%EB%A7%9E%ED%8C%94
        ApiResult result = ApiQuickCaller.post(getInstaWebApiClient().client,
                "https://www.instagram.com/web/likes/" + postId + "/like/"
                , InstaApiUtil.makeStringMap(
                        "accept-language", getInstaWebApiClient().getAcceptLang()
                        , "user-agent", getInstaWebApiClient().getUserAgent()
                        , "accept", "*/*"
                        , "origin", "https://www.instagram.com"
                        , "x-requested-with", "XMLHttpRequest"
                        , "referer", refererString
                        , "x-csrftoken", getInstaWebApiClient().getCsrfToken()
                        , "x-instagram-ajax", "1"
                        , "save-data", "on"
                )
                , InstaApiUtil.makeStringMap());


        return result;
    }
}
