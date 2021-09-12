package com.example.myandroidnotes.util;


import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myandroidnotes.AddActivity;

/**
 * @ClassName: PictureUtil
 * @Description: java类作用描述
 * @Author: wangxianwen
 * @Date: 2021/9/12 18:25
 */

public class PictureUtil extends AppCompatActivity {
    private static final String TAG = "PictureUtil";
    private static final int RC_CHOOSE_PHOTO = 1;


    public void getPictureFromCamera(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onClick: DON't HAVE permission to access the sd image.");
//            verifyStoragePermissions(activity, 1);
            PictureUtil.verifyStoragePermissions(activity, 1);

        } else {
            // Log.d(TAG, "onClick: Have permission to access the SD image.");
            loadSDImage();
        }

    }

    /**
     * @param activity 活动
     * @param   requestCode 请求码
     * @return
     * @method
     * @description 权限请求
     * @date: 2021/9/11 22:51
     * @author: wangxianwen
     */
    public static void verifyStoragePermissions(Activity activity, int requestCode) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    requestCode
            );
        }

    }


    private void loadSDImage() {

        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);


    }

    // 使用Glide加载手机相册中的图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_CHOOSE_PHOTO:
                if (data != null) {
                    String realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(this, data.getData());
                    // mEditor.insertImage("file://"+realPathFromUri, "");
                    Log.d(TAG, "onActivityResult: image url=" + realPathFromUri);
                } else {
                    Toast.makeText(this, "图片损坏，请重新选择", Toast.LENGTH_SHORT).show();
                }

                break;

            default:

                Log.d(TAG, "onActivityResult: default");

                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Log.d(TAG, "onRequestPermissionsResult: User Granted the SD Permission");
                    loadSDImage();
                } else {
                    //Log.d(TAG, "onRequestPermissionsResult: User denied the SD Permission");
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }

    }


}
