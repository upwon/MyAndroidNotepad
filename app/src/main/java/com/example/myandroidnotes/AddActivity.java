package com.example.myandroidnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myandroidnotes.DB.NoteDbOpenHelper;
import com.example.myandroidnotes.util.MyTimeUtil;
import com.example.myandroidnotes.util.RealPathFromUriUtils;
import com.example.myandroidnotes.util.ScreenSizeUtils;
import com.example.myandroidnotes.util.ToastUtil;
import com.yyp.editor.RichEditor;
import com.yyp.editor.bean.MaterialsMenuBean;
import com.yyp.editor.interfaces.OnEditorFocusListener;
import com.yyp.editor.interfaces.OnMaterialsItemClickListener;
import com.yyp.editor.interfaces.OnTextChangeListener;
import com.yyp.editor.widget.EditorOpMenuView;

/**
 * @ProjectName:
 * @Package: com.example.myandroidnotes
 * @ClassName: AddActivity
 * @Description: 增加笔记
 * @Author: wangxianwen
 * @CreateDate: 2021/9/10 21:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/9/10 21:51
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class AddActivity extends AppCompatActivity {

    private static final String TAG = "AddActivityDebug";
    private EditText etTitle;
    private EditText etContent;
    private RichEditor mEditor;
    private EditorOpMenuView mEditorOpMenuView;
    private NoteDbOpenHelper mNoteDbOpenHelper;
    // 从相册选择照片的类库
    public static final int RC_CHOOSE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etTitle = findViewById(R.id.et_title);
        mEditor = findViewById(R.id.et_content);
        mEditorOpMenuView = findViewById(R.id.editor_op_menu_view);

        // -------------------------mEditor 初始化设置--------------------------------------
        //设置占位文字
        mEditor.setPlaceholder("请填写文章正文内容（必填）");
        //设置文字大小
        mEditor.setEditorFontSize(16);
        //设置编辑器内边距
        mEditor.setPadding(10, 10, 10, 10);
        //设置编辑器背景色
        mEditor.setBackgroundColor(getResources().getColor(R.color._ffffff));
        //设置焦点变化监听
        mEditor.hideWhenViewFocused((EditText) findViewById(R.id.et_title));
        mEditor.setOnEditorFocusListener(new OnEditorFocusListener() {
            @Override
            public void onEditorFocus(boolean isFocus) {
                //编辑器重获焦点，素材菜单要隐藏
                mEditorOpMenuView.displayMaterialsMenuView(false);
                mEditorOpMenuView.setVisibility(isFocus ? View.VISIBLE : View.GONE);
            }
        });
        mEditor.setOnTextChangeListener(new OnTextChangeListener() {
            @Override
            //输入文本回调监听
            public void onTextChange(String text) {

            }
        });
        //绑定编辑器
        mEditorOpMenuView.setRichEditor(mEditor);
        //监听素材菜单点击事件
        mEditorOpMenuView.setOnMaterialsItemClickListener(new OnMaterialsItemClickListener() {

            @Override
            public void onMaterialsItemClick(MaterialsMenuBean bean) {
                switch (bean.getId()) {

                    // TODO: 待完成输入网络图片的输入框
                    case MATERIALS_IMAGE: //从素材图片库选择 最大3个
//                        String result=getPictureFromCamera();
                        //  mEditor.insertImage("https://tvax3.sinaimg.cn/large/003pPIslgy1gu9kz1s96xj60y70j5aio02.jpg", ""); //插入图片到编辑器

                        customDialog();


                        break;
                    case MATERIALS_VIDEO: //从素材视频库选择 最大3个
                        mEditor.insertVideoFrame("视频封面地址",
                                123, "视频名字", 32); //插入视频到编辑器
                        break;
                    case MATERIALS_TXT: //从素材文字库选择 最大1个
                        mEditor.insertHtml("新增文本内容"); //插入文本到编辑器
                        break;
                    // TODO: 待完成插入本地图片
                    case LOCAL_IMAGE:
                        Log.d(TAG, "onMaterialsItemClick: 插入本地图片");

                        getPictureFromCamera();
                        break;
                    //   mEditor.insertImage("https://tvax2.sinaimg.cn/large/ba920825gy1grdb8wqvaaj21s80to13l.jpg", "");
                    default:
                        Log.d(TAG, "onMaterialsItemClick: switch case 异常");
                        break;
                }
            }
        });

        mEditor.setHtml("请输入正文...");

        // -------------------------mEditor 初始化设置完毕--------------------------------------


    }

    
    private void customDialog() {

        final Dialog dialog = new Dialog(this, R.style.NormalDialogStyle);

        View view_dialg = View.inflate(this, R.layout.dialog_layout, null);


        final EditText editTextPictureURL = view_dialg.findViewById(R.id.editTextPictureURL);
        final Button openPictureBedActivity = view_dialg.findViewById(R.id.open_picture_bed_activity);
        final Button button_confirmUpload = view_dialg.findViewById(R.id.button_confirmUpload);
        final Button button_cancelOperation = view_dialg.findViewById(R.id.button_cancelOperation);


        dialog.setContentView(view_dialg);


        // 设置点击对话框外部不消失对话款
        dialog.setCanceledOnTouchOutside(true);

        // 设置对话框大小

        view_dialg.setMinimumHeight(ScreenSizeUtils.getInstance(this).getScreenHeight());

        Window dialogWindow = dialog.getWindow();

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        lp.width = ScreenSizeUtils.getInstance(this).getScreenWidth();
        lp.height = ScreenSizeUtils.getInstance(this).getScreenHeight();
        lp.gravity = Gravity.CENTER;

        dialogWindow.setAttributes(lp);

        openPictureBedActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "打开图床操作", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onClick: 打开图床");
                dialog.dismiss();
            }
        });

        button_confirmUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "确认操作", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onClick: 确认");
                dialog.dismiss();

            }
        });

        button_cancelOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "取消操作", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onClick: 取消操作");
                dialog.dismiss();
            }
        });


        dialog.show();


    }

    private void getPictureFromCamera() {
        int permission = ActivityCompat.checkSelfPermission(AddActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onClick: DON't HAVE permission to access the sd image.");
            verifyStoragePermissions(AddActivity.this, 1);
        } else {
            // Log.d(TAG, "onClick: Have permission to access the SD image.");
            loadSDImage();
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

    /**
     * @param
     * @return
     * @method
     * @description 权限请求
     * @date: 2021/9/11 22:51
     * @author: wangxianwen
     */
    private void verifyStoragePermissions(Activity activity, int requestCode) {
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
                    mEditor.insertImage("file://" + realPathFromUri, "");
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


    /**
     * @param
     * @return
     * @method addNote
     * @description 确认添加笔记则持久化至数据库
     * @date: 2021/9/10 22:19
     * @author: wangxianwen
     */
    public void addNote(View view) {
        String title = etTitle.getText().toString();
        String content = mEditor.getHtml();

        if (TextUtils.isEmpty(title)) {
            ToastUtil.toastShort(this, "标题不能为空");
            return;
        }

        Notes note = new Notes();
        note.setTitle(title);
        note.setContents(content);
        note.setTime(MyTimeUtil.getCurrentTime());


        if (mNoteDbOpenHelper == null) {
            mNoteDbOpenHelper = new NoteDbOpenHelper(this);
        }
        // 新增note至数据库，返回true表示操作成功
        boolean result = mNoteDbOpenHelper.insertData(note);
        if (result) {
            ToastUtil.toastShort(this, "新增笔记成功");
            finish();   // 成功则关闭当前界面
        } else {
            ToastUtil.toastLong(this, "新增笔记失败");
        }
    }


}