package com.example.myandroidnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.myandroidnotes.DB.NoteDbOpenHelper;
import com.example.myandroidnotes.util.MyTimeUtil;
import com.example.myandroidnotes.util.ToastUtil;
import com.yyp.editor.RichEditor;
import com.yyp.editor.bean.MaterialsMenuBean;
import com.yyp.editor.interfaces.OnEditorFocusListener;
import com.yyp.editor.interfaces.OnMaterialsItemClickListener;
import com.yyp.editor.interfaces.OnTextChangeListener;
import com.yyp.editor.widget.EditorOpMenuView;

public class EditActivity extends AppCompatActivity {

    private Notes note;
    private EditText etTitle;
    private EditText etContent;

    private NoteDbOpenHelper mNoteDbOpenHelper;

    private RichEditor mEditor;
    private EditorOpMenuView mEditorOpMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

    /*    etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);*/

        etTitle = findViewById(R.id.et_title);
        // etContent=findViewById(R.id.et_content);
        mEditor = findViewById(R.id.et_content);
        mEditorOpMenuView = findViewById(R.id.editor_op_menu_view);


        mEditor.setEditorFontSize(16); //设置文字大小
        mEditor.setPadding(10, 10, 10, 10); //设置编辑器内边距
        mEditor.setBackgroundColor(getResources().getColor(R.color._ffffff)); //设置编辑器背景色
        mEditor.hideWhenViewFocused((EditText) findViewById(R.id.et_title)); //设置焦点变化监听
        mEditor.setOnEditorFocusListener(new OnEditorFocusListener() {
            @Override
            public void onEditorFocus(boolean isFocus) {
                mEditorOpMenuView.displayMaterialsMenuView(false); //编辑器重获焦点，素材菜单要隐藏
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
                        mEditor.insertImage("https://tvax3.sinaimg.cn/large/003pPIslgy1gu9kz1s96xj60y70j5aio02.jpg", ""); //插入图片到编辑器
                        break;
                    case MATERIALS_VIDEO: //从素材视频库选择 最大3个
                        mEditor.insertVideoFrame("视频封面地址",
                                123, "视频名字", 32); //插入视频到编辑器
                        break;
                    case MATERIALS_TXT: //从素材文字库选择 最大1个
                        mEditor.insertHtml("新增文本内容"); //插入文本到编辑器
                        break;
                    case LOCAL_IMAGE:   // 本地選取圖片
                        mEditor.insertImage("https://tvax2.sinaimg.cn/large/ba920825gy1grdb8wqvaaj21s80to13l.jpg", "");
                        break;

                }
            }
        });


        iniData();

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


}