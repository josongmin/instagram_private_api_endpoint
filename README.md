# instagram_private_api_endpoint
It is Instagram's private api endpoint of App and Web for Android.
This project contains several instagram private API of Web and App.

All api call classes extend ApiCall class, and the ApiCall class provides Async call and Sync call.

Below is an example to get instagram user profile by using Web Api.

```java
//Async call
new WebGetUserInfo(instaWebApiClient)
                .setParam("userName", '...')
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

To check more Apis(Signin, Posting, Leaving comment, Change profile, Follow/Unfollow, Like/Unlike...)
See classes stored in the package 'com.studio1221.instagram_api_manager.endpoint'



** It is not legally responsible.

