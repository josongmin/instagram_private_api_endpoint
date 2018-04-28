package com.studio1221.instagram_api_manager.endpoint.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import com.studio1221.instagram_api_manager.browser.CommonApiClient;
import com.studio1221.instagram_api_manager.browser.api_call.ApiCall;
import com.studio1221.instagram_api_manager.browser.exception.ApiException;
import com.studio1221.instagram_api_manager.browser.model.ApiResult;
import com.studio1221.instagram_api_manager.util.RandomIdManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jo on 2017-11-21.
 */

public class RandomImageDownloadCall extends ApiCall<String>{

    CommonApiClient apiClient = new CommonApiClient();
    Random random = new Random();

    private String makeRandomImageUrl(){
        return "https://loremflickr.com/700/700/moviestar,cat,dog,girl,baby,toy";
    }

    @Override
    public ApiResult<String> work() throws Exception {
        String imgUrl = ApiCall.getParam(this, "imgUrl");
        int targetWidth = new Random().nextInt(400) + 200;
        if(imgUrl == null) imgUrl = makeRandomImageUrl();

        Request request = new Request.Builder()
                .url(imgUrl)
                .build();

        Bitmap bitmapResult = null;
        Response response = apiClient.client.newCall(request).execute();

        if(response.code() != 200){
            throw new ApiException(response.code(), response.body().string());
        }

        InputStream inputStream = response.body().byteStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float factor =  (float)height / (float)width;
        float targetHeight = targetWidth * factor;

        float scaleWidth = (float)targetWidth / (float)width;
        float scaleHeight = (float)targetHeight / (float)height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap( bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();

        int resultWidth = resizedBitmap.getWidth();
        int resultHeight = resizedBitmap.getHeight();

        final int cutSize = 50;
        int xPos = 0, yPos = 0;

        if(resultWidth == resultHeight){
            bitmapResult = resizedBitmap;
        }
        else if(resultWidth > resultHeight){
            xPos = (int)(resultWidth - resultHeight) / 2;
        }else{
            yPos = (int)(resultHeight - resultWidth) / 2;
        }

        Bitmap resizedBitmap2 = Bitmap.createBitmap( resizedBitmap, yPos + cutSize/2, yPos + cutSize/2, resultWidth - cutSize, resultWidth - cutSize);
        bitmapResult = resizedBitmap2;
        resizedBitmap.recycle();
        //bitmapResult

        //save
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/instaAutoUploader/";
        String fileName = RandomIdManager.makeRandomId(random.nextInt(4)+2, random.nextInt(3)+1) + ".jpg";
        String fullFilePath = dirPath + fileName;

        File fdirPath = new File(dirPath);
        if(!fdirPath.isDirectory()){
            fdirPath.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(fullFilePath);
        bitmapResult.compress(Bitmap.CompressFormat.JPEG, 100, out);
        out.close();

        bitmapResult.recycle();

        return new ApiResult<String>().setResultCode(200).setResult("success").setObject(fullFilePath);
    }
}
