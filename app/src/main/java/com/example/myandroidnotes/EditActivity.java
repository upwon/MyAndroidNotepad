package com.example.myandroidnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.myandroidnotes.util.MyTimeUtil;
import com.example.myandroidnotes.util.ToastUtil;

public class EditActivity extends AppCompatActivity {

    private Notes note;
    private EditText etTitle;
    private EditText etContent;

    private NoteDbOpenHelper mNoteDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        iniData();

    }

    // 获取传递过来的数据
    private void iniData() {
        Intent intent = getIntent();
        note = (Notes) intent.getSerializableExtra("note");
        if (note != null) {
            etTitle.setText(note.getTitle());
            etContent.setText(note.getContents());
        }

    }

    public   void saveEditedNote(View view){
        String title=etTitle.getText().toString();
        String content=etContent.getText().toString();

        if(TextUtils.isEmpty(title)){
            ToastUtil.toastShort(this,"标题不能为空");
            return ;
        }
     //   Notes note=new Notes();
        note.setTitle(title);
        note.setContents(content);
        note.setTime(MyTimeUtil.getCurrentTime());

        if(mNoteDbOpenHelper==null)
        {
            mNoteDbOpenHelper=new NoteDbOpenHelper(this);
        }

        int result= mNoteDbOpenHelper.updateData(note);   // mNoteDbOpenHelper.insertData(note);
        if(result>0){
            ToastUtil.toastShort(this,"修改笔记成功");
            finish();   // 成功则关闭当前界面
        }
        else{
            ToastUtil.toastLong(this,"修改笔记失败");
        }
    }



}