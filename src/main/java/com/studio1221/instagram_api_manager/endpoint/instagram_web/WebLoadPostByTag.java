package com.studio1221.instagram_api_manager.endpoint.instagram_web;


import com.studio1221.instagram_api_manager.browser.InstaWebApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.api_call.InstaWebApiCall;
import com.studio1221.instagram_api_manager.browser.api_quick_caller.ApiQuickCaller;
import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jo on 2017-11-13.
 */

public class WebLoadPostByTag extends InstaWebApiCall {

    public WebLoadPostByTag(InstaWebApiClient client) {
        super(client);
    }

    @Override
    public WebLoadPostByTag setParam(String key, Object value) {
        super.setParam(key, value);
        return this;
    }

    @Override
    public WebLoadPostByTag setParams(Object... params) {
        super.setParams(params);
        return this;
    }

    @Override
    public ApiResult<PostingDatasModel> work() throws Exception{

        final String searchTag = ApiCall.getParam(this, "searchTag");
        if(searchTag == null) throw new ApiException(-100, "searchTag not ready", null);

        ApiResult result = ApiQuickCaller.post(getInstaWebApiClient().client,
                "https://www.instagram.com/explore/tags/" + InstaApiUtil.encodeUTF8(searchTag) + "/?__a=1"
                , InstaApiUtil.makeStringMap(
                        "accept-language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,id;q=0.6"
                        , "accept", "*/*"
                        , "referer", "https://www.instagram.com/explore/tags/" + InstaApiUtil.encodeUTF8(searchTag)
                        , "user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36"
                        , "save-data", "on"
                        , "authority", "www.instagram.com"
                        , "x-requested-with", "XMLHttpRequest"
                )
                , InstaApiUtil.makeStringMap(
                        "surface_param", "5095"
                        , "vc_policy", "default"
                        , "version", "1"));

        JSONObject jResult = new JSONObject(result.result);

        List<Map<String, String>> listResult = new ArrayList<>();
        JSONObject jMedia = jResult.getJSONObject("tag").getJSONObject("media");
        JSONArray jArrPostNodes = jMedia.getJSONArray("nodes");

        PostingDatasModel postingDatasModel = new PostingDatasModel();
        for(int i = 0; i < jArrPostNodes.length(); i++){
            JSONObject jPost = jArrPostNodes.getJSONObject(i);
            PostingDataModel postingDataModel = new PostingDataModel();

            postingDataModel.postId = jPost.getString("id");
            postingDataModel.postCode = jPost.getString("code");
            postingDataModel.ownerId = jPost.getJSONObject("owner").getString("id");
            postingDataModel.postId = jPost.getString("id");
//            postingDataModel.caption = jPost.getString("caption");
//            postingDataModel.commentCount = jPost.getJSONObject("comments").getInt("count");
//            postingDataModel.likeCount = jPost.getJSONObject("owner").getString("id");

            postingDatasModel.listPostingDatas.add(postingDataModel);
        }

//        JSONObject jPageInfo = jResult.getJSONObject("page_info");
//        postingDatasModel.endCursor = jPageInfo.getString("end_cursor");

        //
        ApiResult<PostingDatasModel> apiResult = new ApiResult(200, "success", postingDatasModel);
        return apiResult;
    }

    public static class PostingDatasModel{
        public String endCursor;
        public List<PostingDataModel> listPostingDatas = new ArrayList<>();
    }

    public static class PostingDataModel{
        public String postCode, postId;
        public String caption;
        public String ownerId;

    }

}
