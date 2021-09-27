package com.example.myandroidnotes.HTTP;


import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.*;



/**
 * 图像文件上传至图床
 * @author wangxianwen
 * @date 2021/9/17 23:58
 */
public class ClientUploadFileUtils {

    private static final String TAG = "ClientUploadFileUtils";
    /**
     * token 长度
     */
    private static int TokenLength = 32;

    /**
     * 上传图像文件至 图床
     *
     * @param url      URL
     * @param token    图床中账号的token
     * @param filePath 文件路径
     * @param fileName 文件名称
     * @return okhttp 的 响应报文body
     *
     */
    public static ResponseBody upload(String url, String token, String filePath, String fileName) throws Exception {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("fileupload", fileName,
                        RequestBody.create(MediaType.parse("multipart/form-data"), new File(filePath)))
                .build();

        Request request = new Request.Builder()
                // .header("Authorization", "Client-ID " + UUID.randomUUID())
                .url(url + "?token=" + token)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        Log.d(TAG, "upload: response code = " + response.code());
        Log.d(TAG, "upload: response message = " + response.message());
        Log.d(TAG, "upload: response protocol = " + response.protocol());


        return response.body();


    }

    /**
     * 校验 token 长度是否合法
     * @param token 待验证token
     * @return 合法则返回 true
     */
    public static boolean isValidToken(String token) {

        if(token.length()==TokenLength){

            return true;
        }

        return false;
    }


}



