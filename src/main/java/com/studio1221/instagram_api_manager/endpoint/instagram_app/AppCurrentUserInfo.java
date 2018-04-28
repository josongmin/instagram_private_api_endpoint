package com.studio1221.instagram_api_manager.endpoint.instagram_app;

import com.studio1221.instagram_api_manager.browser.InstaAppApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.InstaAppApiCall;
import com.studio1221.instagram_api_manager.browser.api_quick_caller.ApiQuickCaller;
import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.model.InstaLoginUser;
import com.studio1221.instagram_api_manager.util.InstaApiUtil;

import java.util.Map;

/**
 * Created by jo on 2017-11-13.
 */

public class AppCurrentUserInfo extends InstaAppApiCall {

    public AppCurrentUserInfo(InstaAppApiClient client) {
        super(client);
    }

    @Override
    public ApiResult<InstaLoginUser> syncWork() {
        return super.syncWork();
    }

    @Override
    public ApiResult<InstaLoginUser> work() throws Exception{

        final String url = "https://i.instagram.com/api/v1/accounts/current_user/?edit=true";

        final String guid = getInstaAppApiClient().guid;
        final String userId = getInstaAppApiClient().userId;

        ApiResult resultModel = ApiQuickCaller.postForAppInstagram(getInstaAppApiClient().client, url
                , makeHeaderWithCommonHeader("Connection", "close")
                , InstaApiUtil.makeStringMap(
                        "_uuid", guid
                        ,  "_uid", userId
                        ,  "_csrftoken", getInstaAppApiClient().getCsrfToken()
                )
        );

        //췍
        ApiException.chkApiException(resultModel);

        //성공했을때만
        Map<String, Object> resultMap = getResultMap(resultModel.result);
        resultMap = (Map<String, Object>)resultMap.get("user");

//            0 = {LinkedTreeMap$Node@4835} "pk" -> "3.66477841E8"
//            1 = {LinkedTreeMap$Node@4836} "username" -> "domangchu"
//            2 = {LinkedTreeMap$Node@4837} "full_name" -> "song"
//            3 = {LinkedTreeMap$Node@4838} "is_private" -> "false"
//            4 = {LinkedTreeMap$Node@4839} "profile_pic_url" -> "https://scontent-sit4-1.cdninstagram.com/t51.2885-19/s150x150/23596028_719986838207213_8868409907716554752_n.jpg"
//            5 = {LinkedTreeMap$Node@4840} "profile_pic_id" -> "1649559323673740513_366477841"
//            6 = {LinkedTreeMap$Node@4841} "is_verified" -> "false"
//            7 = {LinkedTreeMap$Node@4842} "has_anonymous_profile_picture" -> "false"
//            8 = {LinkedTreeMap$Node@4843} "biography" -> "지박령인데 가끔 여행왕"
//            9 = {LinkedTreeMap$Node@4844} "external_url" ->
//            10 = {LinkedTreeMap$Node@4845} "hd_profile_pic_versions" -> " size = 1"
//            11 = {LinkedTreeMap$Node@4846} "hd_profile_pic_url_info" -> " size = 3"
//            12 = {LinkedTreeMap$Node@4847} "show_conversion_edit_entry" -> "true"
//            13 = {LinkedTreeMap$Node@4848} "birthday" -> "null"
//            14 = {LinkedTreeMap$Node@4849} "phone_number" -> "+821093149449"
//            15 = {LinkedTreeMap$Node@4850} "country_code" -> "82.0"
//            16 = {LinkedTreeMap$Node@4851} "national_number" -> "1.093149449E9"
//            17 = {LinkedTreeMap$Node@4852} "gender" -> "3.0"
//            18 = {LinkedTreeMap$Node@4853} "email" -> "944899@gmail.com"
//            19 = {LinkedTreeMap$Node@4854} "can_link_entities_in_bio" -> "false"
//            20 = {LinkedTreeMap$Node@4855} "max_num_linked_entities_in_bio" -> "5.0"

        InstaLoginUser instaLoginUser = new InstaLoginUser();
        instaLoginUser.userName = (String)resultMap.get("username");
        instaLoginUser.bio = (String)resultMap.get("biography");
        instaLoginUser.countyCode = (Double)resultMap.get("country_code")+"";
        instaLoginUser.email = (String)resultMap.get("email");
        instaLoginUser.fullName = (String)resultMap.get("full_name");
        instaLoginUser.id = ((Double)resultMap.get("pk"))+"";
        instaLoginUser.isPrivate = ((Boolean)resultMap.get("is_private"));
        instaLoginUser.photoUrl = (String)resultMap.get("profile_pic_url");

        ApiResult<InstaLoginUser> apiResult = new ApiResult(resultModel.resultCode, resultModel.result, instaLoginUser);
        return apiResult;

    }
}
