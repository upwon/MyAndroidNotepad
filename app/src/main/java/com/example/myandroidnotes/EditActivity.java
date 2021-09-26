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
import com.example.myandroidnotes.util.PictureUtil;
import com.example.myandroidnotes.util.RealPathFromUriUtils;
import com.example.myandroidnotes.util.ScreenSizeUtils;
import com.example.myandroidnotes.util.ToastUtil;
import com.yyp.editor.RichEditor;
import com.yyp.editor.bean.MaterialsMenuBean;
import com.yyp.editor.interfaces.OnEditorFocusListener;
import com.yyp.editor.interfaces.OnMaterialsItemClickListener;
import com.yyp.editor.interfaces.OnTextChangeListener;
import com.yyp.editor.widget.EditorOpMenuView;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";
    // 从相册选择照片的类库
    public static final int RC_CHOOSE_PHOTO = 1;
    private Notes note;
    private EditText etTitle;
    private EditText etContent;

    private NoteDbOpenHelper mNoteDbOpenHelper;

    private RichEditor mEditor;
    private EditorOpMenuView mEditorOpMenuView;
    PictureUtil pictureUtil = new PictureUtil();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        initEditor();


        iniData();

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


    private void initEditor() {
        etTitle = findViewById(R.id.et_title);
        mEditor = findViewById(R.id.et_content);
        mEditorOpMenuView = findViewById(R.id.editor_op_menu_view);

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
            public void onTextChange(String text) { //输入文本回调监听

            }
        });
        //绑定编辑器
        mEditorOpMenuView.setRichEditor(mEditor);
        //监听素材菜单点击事件
        mEditorOpMenuView.setOnMaterialsItemClickListener(new OnMaterialsItemClickListener() {

            @Override
            public void onMaterialsItemClick(MaterialsMenuBean bean) {
                switch (bean.getId()) {
                    case MATERIALS_IMAGE: //从素材图片库选择 最大3个
                        // 插入图片到编辑器
                        // mEditor.insertImage("https://tvax3.sinaimg.cn/large/003pPIslgy1gu9kz1s96xj60y70j5aio02.jpg", "");
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
                        //   pictureUtil.getPictureFromCamera(EditActivity.this);

                        getPictureFromCamera(EditActivity.this);
                        break;
                    default:
                        Log.d(TAG, "onMaterialsItemClick: switch case 异常");
                        break;


                }
            }
        });

    }


    // 获取传递过来的数据
    private void iniData() {
        Intent intent = getIntent();
        note = (Notes) intent.getSerializableExtra("note");
        if (note != null) {
            etTitle.setText(note.getTitle());
            // 更改编辑器
            // etContent.setText(note.getContents());
            mEditor.setHtml(note.getContents());
        }

    }


    private void customDialog() {
        
        final Dialog dialog = new Dialog(this, R.style.NormalDialogStyle);

        View viewDialg = View.inflate(this, R.layout.dialog_layout, null);


        final EditText editTextPictureURL = viewDialg.findViewById(R.id.editTextPictureURL);
        final Button openPictureBedActivity = viewDialg.findViewById(R.id.open_picture_bed_activity);
        final Button buttonOK = viewDialg.findViewById(R.id.button_confirmUpload);
        final Button buttonCancelOperation = viewDialg.findViewById(R.id.button_cancelOperation);


        dialog.setContentView(viewDialg);
        
        // 设置点击对话框外部不消失对话款
        dialog.setCanceledOnTouchOutside(true);

        // 设置对话框大小
        viewDialg.setMinimumHeight(ScreenSizeUtils.getInstance(this).getScreenHeight());

        Window dialogWindow = dialog.getWindow();

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();


        // 对话框宽度
        lp.width = (int) (ScreenSizeUtils.getInstance(this).getScreenWidth() * 0.85f);
        // 对话框高度
        lp.height = (int) (ScreenSizeUtils.getInstance(this).getScreenHeight() * 0.45f);
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        // 设置对话框出现与消失动画
        dialogWindow.setWindowAnimations(R.style.normalDialogAnim);
        
        // 打开图床
        openPictureBedActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "打开图床", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onClick: 打开图床");


                openImageHosting(this);



              //  dialog.dismiss();
            }
        });

        // 确认插入网络图像
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "确认操作", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onClick: 确认");

                if (editTextPictureURL.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "输入为空，请重新输入", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onClick: 输入为空，请重新输入"
                            + editTextPictureURL.getText());

                }
                // 简单的合法性校验
                else if (!editTextPictureURL.getText().toString().substring(0, 4).equals("http")) {

                    Log.d(TAG, "onClick: 非法URL" + editTextPictureURL.getText().toString());
                } else {
                    mEditor.insertImage(editTextPictureURL.getText().toString(), ""); //插入图片到编辑器
                    Log.d(TAG, "onClick: editTextPictureURL.getText()" + editTextPictureURL.getText());
                }

                dialog.dismiss();

            }
        });

        // 取消操作
        buttonCancelOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "取消操作", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onClick: 取消操作");
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    private void getPictureFromCamera(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onClick: DON't HAVE permission to access the sd image.");
            PictureUtil.verifyStoragePermissions(activity, 1);
        } else {
            // Log.d(TAG, "onClick: Have permission to access the SD image.");
            loadSDImage();
        }

    }

    private void loadSDImage() {

        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);

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


    public void saveEditedNote(View view) {
        String title = etTitle.getText().toString();
//        String content = etContent.getText().toString();
        String content = mEditor.getHtml();


        if (TextUtils.isEmpty(title)) {
            ToastUtil.toastShort(this, "标题不能为空");
            return;
        }
        //   Notes note=new Notes();
        note.setTitle(title);
        note.setContents(content);
        note.setTime(MyTimeUtil.getCurrentTime());

        if (mNoteDbOpenHelper == null) {
            mNoteDbOpenHelper = new NoteDbOpenHelper(this);
        }

        // mNoteDbOpenHelper.insertData(note);
        int result = mNoteDbOpenHelper.updateData(note);
        if (result > 0) {
            ToastUtil.toastShort(this, "修改笔记成功");
            finish();   // 成功则关闭当前界面
        } else {
            ToastUtil.toastLong(this, "修改笔记失败");
        }
    }


    public void openImageHosting(View.OnClickListener view) {
        Intent intent = new Intent(this,ImageHostingActivity.class);
        startActivity(intent);
        Log.d(TAG, "openImageHosting: "+this+"打开 ImageHostingActivity");
    }
}