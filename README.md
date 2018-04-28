# instagram_private_api_endpoint
instagram private api endpoint of App and Web

This project contains instagram private API of Web and App.

All api call classes extend ApiCall class.
The ApiCall class provide Async call and Sync call.

Below is a call to get instagram user profile by using Web Api.

```java
//Async call
new WebGetUserInfo(instaWebApiClient)
                .setParam("userName", instaAppApiClient.userName)
                .asyncWork(new ApiResultCallback<InstaUserWebSummary>() {
            @Override
            public void onFinish(ApiResult<InstaUserWebSummary> apiResult) {
                InstaUserWebSummary instaUserWebSummary = apiResult.object;
                //to do something.
            }
        });
```

```java
//Sync call
ApiResult<InstaUserWebSummary> apiResult = new WebGetUserInfo(instaWebApiClient)
                .setParam("userName", instaAppApiClient.userName)
                .syncWork();
InstaUserWebSummary instaUserWebSummary = apiResult.object;
```

To check more Api,
See classes in this package 'com.studio1221.instagram_api_manager.endpoint'

** It is not legally responsible.

