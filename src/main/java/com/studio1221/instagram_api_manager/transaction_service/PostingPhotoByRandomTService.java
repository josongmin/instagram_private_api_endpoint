package com.studio1221.instagram_api_manager.transaction_service;

import com.studio1221.instagram_api_manager.browser.InstaWebApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.endpoint.common.RandomImageDownloadCall;
import com.studio1221.instagram_api_manager.endpoint.instagram_web.WebUploadPostingCall;

import java.io.File;

/**
 * Created by jo on 2017-11-21.
 */

public class PostingPhotoByRandomTService extends ApiCall {

    InstaWebApiClient instaWebApiClient = null;

    public PostingPhotoByRandomTService(InstaWebApiClient instaWebApiClient) {
        this.instaWebApiClient = instaWebApiClient;
    }

    @Override
    public ApiResult<String> work() throws Exception {

        //다운로드
        ApiResult<String> fileResult = new RandomImageDownloadCall().work();
        ApiException.chkApiException(fileResult);

        //업로드
        ApiResult changeResult = new WebUploadPostingCall(instaWebApiClient).setParam("filePath", fileResult.object).syncWork();
        ApiException.chkApiException(changeResult);

        //파일삭제
        File f = new File(fileResult.object);
        f.delete();

        return new ApiResult<>(200, "success");
    }
}
