package com.example.myandroidnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.myandroidnotes.DB.NoteDbOpenHelper;
import com.example.myandroidnotes.util.MyTimeUtil;
import com.example.myandroidnotes.util.ToastUtil;
import com.yyp.editor.RichEditor;
import com.yyp.editor.widget.EditorOpMenuView;

public class AddActivity extends AppCompatActivity {

    private EditText etTitle,etContent;
    private RichEditor mEditor;
    private EditorOpMenuView mEditorOpMenuView;


    private NoteDbOpenHelper mNoteDbOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etTitle=findViewById(R.id.et_title);
       // etContent=findViewById(R.id.et_content);
        mEditor=findViewById(R.id.et_content);
        mEditorOpMenuView=findViewById(R.id.editor_op_menu_view);
    }




    public void add(View view) {
            String title=etTitle.getText().toString();
            String content=etContent.getText().toString();

            if(TextUtils.isEmpty(title)){
                ToastUtil.toastShort(this,"标题不能为空");
                return ;
            }

            Notes note=new Notes();
            note.setTitle(title);
            note.setContents(content);
            note.setTime(MyTimeUtil.getCurrentTime());


           // boolean result=NoteDbOpenHelper.insertData(note);
           // boolean result= note.save();   // mNoteDbOpenHelper.insertData(note);
            if(mNoteDbOpenHelper==null)
            {
                mNoteDbOpenHelper=new NoteDbOpenHelper(this);
            }
            boolean result= mNoteDbOpenHelper.insertData(note);   // mNoteDbOpenHelper.insertData(note);


            if(result){
                ToastUtil.toastShort(this,"新增笔记成功");
                finish();   // 成功则关闭当前界面
            }
            else{
                ToastUtil.toastLong(this,"新增笔记失败");
            }
    }



}