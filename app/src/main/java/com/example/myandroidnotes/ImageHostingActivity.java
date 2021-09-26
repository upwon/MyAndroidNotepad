package com.example.myandroidnotes;


import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myandroidnotes.HTTP.ClientUploadFileUtils;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.utils.permission.PermissionUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import okhttp3.ResponseBody;

/**
 * @ClassName: ImageHostingActivity
 * @Description: java类作用描述
 * @Author: wangxianwen
 * @Date: 2021/9/26 20:09
 */

public class ImageHostingActivity extends AppCompatActivity implements View.OnClickListener {

    // 结果码的作用：用于标示返回结果的来源  此处为标记拍照
    public static final int TAKE_PHOTO = 1;
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_SETTING = 1;
    // 结果码的作用：用于标示返回结果的来源  此处为标记从相册选择图片
    private static final int SELECTED_PHOTO = 101;


    private String baseUrl = "https://www.img11.top/api/upload";

    /**
     * 选择的图片集
     */
    private ArrayList<Photo> selectedPhotoList = new ArrayList<>();

    ArrayList<Photo> resultPhotos;

    private Uri imageUri;

    ImageView imageViewPicture;
    EditText editTextTextToken;
    Button button_open_picture;
    Button button_upload;
    TextView textView_info;
    EditText editTextText_PictureURLInfo;
    Button buttonCopyURL;
//    Button button_dailog;


    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String strData = (String) msg.obj;
                editTextText_PictureURLInfo.setText(strData);
                Log.d(TAG, "handleMessage: strData = " + strData);
                Toast.makeText(ImageHostingActivity.this, "主线程收到来自网络的消息啦！", Toast.LENGTH_SHORT).show();

            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_image_hosting);

        initView();
        if (PermissionUtil.checkAndRequestPermissionsInActivity(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            preLoadAlbums();
        }
    }


    private void initView() {
        button_upload = findViewById(R.id.button_upload);
        button_open_picture = findViewById(R.id.button_open_picture);
        editTextText_PictureURLInfo = findViewById(R.id.editTextText_PictureURLInfo);
        imageViewPicture = findViewById(R.id.imageViewPicture);
        button_open_picture.setOnClickListener(this);
        button_upload.setOnClickListener(this);

        buttonCopyURL = findViewById(R.id.buttonCopyURL);
        buttonCopyURL.setOnClickListener(this);

//        button_dailog = findViewById(R.id.button_dailog);
//        button_dailog.setOnClickListener(this);

    }


    private void preLoadAlbums() {
        EasyPhotos.preLoad(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onPermissionResult(this, permissions, grantResults,
                new PermissionUtil.PermissionCallBack() {
                    @Override
                    public void onSuccess() {
                        preLoadAlbums();
                    }

                    @Override
                    public void onShouldShow() {
                    }

                    @Override
                    public void onFailed() {
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 拍照回调
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        imageViewPicture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            // 从相册选择图片回调
            case SELECTED_PHOTO:
                if (RESULT_OK == resultCode) {
                    //相机或相册回调

                    //返回对象集合：如果你需要了解图片的宽、高、大小、用户是否选中原图选项等信息，可以用这个
                    ArrayList<Photo> resultPhotos =
                            data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);

                    //返回图片地址集合时如果你需要知道用户选择图片时是否选择了原图选项，用如下方法获取
                    boolean selectedOriginal =
                            data.getBooleanExtra(EasyPhotos.RESULT_SELECTED_ORIGINAL, false);


                    selectedPhotoList.clear();
                    selectedPhotoList.addAll(resultPhotos);
                    //  adapter.notifyDataSetChanged();
                    //  rvImage.smoothScrollToPosition(0);

                    Glide.with(this).load(resultPhotos.get(0).uri).into(imageViewPicture);

                }

            default:
                Log.d(TAG, "onActivityResult: switch case : default " + requestCode);
                break;
        }
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.button_open_picture:

                EasyPhotos.createAlbum(this, true, false, GlideEngine.getInstance())
                        .setFileProviderAuthority("com.huantansheng.easyphotos.demo.fileprovider")
                        .start(101);//也可以选择链式调用写法
                break;
            case R.id.button_upload:

                String url = "https://www.img11.top/api/upload";
                String token = "feb9e70f806f1198aa23c755775b46d6";

                if (imageViewPicture.getDrawable() == null) {
                    Log.d(TAG, "onClick: imageViewPicture未显示图片");
                    break;
                }

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        ResponseBody result = null;
                        try {
                            if (selectedPhotoList.size() < 1) {
                                Log.d(TAG, "run: selectedPhotoList.size = " + selectedPhotoList.size());

                            } else {
                                result = ClientUploadFileUtils.upload(url, token, selectedPhotoList.get(0).path, selectedPhotoList.get(0).name);
                                Log.d(TAG, "run: 图片uri = " + selectedPhotoList.get(0).uri);

                                // response.body().string() 只能调用一次
                                // https://juejin.cn/post/6844903545628524551
                                Log.d(TAG, "run:  ResponseBody result= " + result.toString());
                                // editTextText_PictureURLInfo.setText(result.string());

                                Message message = new Message();
                                message.what = 0;
//                                message.obj=new String(result.string());
                                message.obj = new String(result.string());
                                mHandler.sendMessage(message);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };

                new Thread(runnable).start();
                Toast.makeText(this, "开启子线程请求网络！", Toast.LENGTH_SHORT).show();

                break;

            // // if the user selects copy
            case R.id.buttonCopyURL:
                //获取剪贴板管理器：
                // Gets a handle to the clipboard service.
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                // 创建普通字符型ClipData
                ClipData clip = ClipData.newPlainText("pictureURL", editTextText_PictureURLInfo.getText());

                Log.d(TAG, "onClick: 准备复制到剪切板 clip = " + clip);

                // 将ClipData内容放到系统剪贴板里。
                // Set the clipboard's primary clip.
                clipboardManager.setPrimaryClip(clip);

                clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                    @Override
                    public void onPrimaryClipChanged() {
                        // 剪贴板中的数据被改变，此方法将被回调
                        Log.d(TAG, "onPrimaryClipChanged: 剪切板数据改变");

                        if (clipboardManager.hasPrimaryClip()) {
                            // 打印剪切板中的第一条数据
                            Log.d(TAG, "onPrimaryClipChanged: " + clipboardManager.getPrimaryClip().getItemAt(0).getText().toString());
                        }
                    }
                });

                break;

            default:

                Log.d(TAG, "onRequestPermissionsResult: 默认");
                break;
        }
    }


    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

}
