package com.studio1221.instagram_api_manager.browser;

import com.studio1221.instagram_api_manager.browser.model.ApiResult;

/**
 * Created by jo on 2017-11-21.
 */

public interface ApiResultCallback<T> {
    void onFinish(ApiResult<T> apiResult);
}
