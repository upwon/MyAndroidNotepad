package com.example.myandroidnotes.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.myandroidnotes.Notes;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class NoteDbOpenHelper { //   extends LitePalSupport{

    private Notes mNote;


    public NoteDbOpenHelper(Context mContext) {
        LitePal.initialize(mContext);
    }

    public void initDataBase(Context mContext) {
        LitePal.initialize(mContext);
    }

    public void CreateDataBase() {
        //创建数据库
        SQLiteDatabase db = LitePal.getDatabase();
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public  boolean insertData(Notes note) {
//        mNote = note;
//        return mNote.save();

        return note.save();
    }


    public int deleteFromDbById(long id) {


        int result = LitePal.deleteAll(Notes.class, "id=?", String.valueOf(id));
        return result;

//        try {
//            // 第一种：以问号传参，字符串参数不需要手动加单引号
//            // 注意 LitePal 的数据字段默认都会全部转为小写
//            int rowsAffected =  LitePal.deleteAll(Notes.class,"id=?", String.valueOf(id) );
//            if (rowsAffected == -1) {
//                Toast.makeText(this, "删除失败，主键不存在", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
//        }


    }

    public int updateData(Notes note) {

    /*    //创建Note对象
        Note note=new Note();
        note.setContent(editview.getText().toString());
        //获取当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        note.setTime(simpleDateFormat.format(date));
        //更新内容和时间
        Intent edit_page=getIntent();
        //获取主页面(活动)传递到的值
        String ss=edit_page.getStringExtra("data_edit");
        //从数据库匹配没修改之前的item值，然后更新新的数据
        note.updateAll("content = ?",ss);
        finish();
        return super.onOptionsItemSelected(item);
        */


        Notes tempNote = new Notes();

        tempNote.setTitle(note.getTitle());
        tempNote.setContents(note.getContents());

        //获取当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        tempNote.setTime(simpleDateFormat.format(date));

        int result = tempNote.updateAll("id = ?", String.valueOf(note.getId()));


        return result;

    }

    public List<Notes> queryAllNotes()
    {
        List<Notes> allNotes=LitePal.order("time desc").find(Notes.class);

        return  allNotes;

    }

    public List<Notes> queryByTitle(String title)
    {
        List<Notes> resultNotes=LitePal.where("title like ?","%"+title+"%").order("time desc").find(Notes.class);

        return resultNotes;

    }

}
