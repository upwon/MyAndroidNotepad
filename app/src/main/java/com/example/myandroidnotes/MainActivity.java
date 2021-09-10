package com.example.myandroidnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myandroidnotes.DB.NoteDbOpenHelper;
import com.example.myandroidnotes.adapter.MyAdapter;
import com.example.myandroidnotes.util.SpfUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yyp.editor.RichEditor;
import com.yyp.editor.widget.EditorOpMenuView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private int currentListLayoutMode = MODE_LINEAR;    // 记录布局为列表视图还是
    private RecyclerView mRecyclerView;
    private FloatingActionButton mButtonAdd;
    private List<Notes> mNotes;
    private MyAdapter mAdapter;
    private NoteDbOpenHelper mNoteDbOpenHelper;
    public static final String KEY_LAYOUT_MODE = "key_layout_mode";
    public static final int MODE_LINEAR = 0;
    public static final int MODE_GRID = 1;
    
  
    /**
     * @method
     * @description 初始化View->初始化数据->初始化事件
     * @date: 2021/9/8 19:48
     * @author: wangxianwen
     * @param 
     * @return 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initEvents();

    }

    /**
     * @method
     * @description 重写onResume 保证打开软件或新增笔记后 数据刷新
     * @date: 2021/9/10 19:51
     * @author: wangxianwen
     * @param 
     * @return 
     */
    @Override
    protected void onResume() {
        super.onResume();
        refreshDataFromDb();
        setListLayout();
    }

   /**
    * @method  refreshDataFromDb
    * @description  从数据库中获取所有数据  并让Adapter去通知UI刷新示数据
    * @date: 2021/9/10 20:05
    * @author: wangxianwen
    * @param
    * @return
    */
    private void refreshDataFromDb() {
        mNoteDbOpenHelper = new NoteDbOpenHelper(this);
        mNotes = mNoteDbOpenHelper.queryAllNotes();
        //  Collections.reverse(mNotes);
        mAdapter.refreshDate(mNotes);
    }




    private void initView() {

        mRecyclerView = findViewById(R.id.recycler_view);

    }

    /**
     * @method
     * @description 初始化数据库操作对象
     * @date: 2021/9/10 20:06
     * @author: wangxianwen
     * @param
     * @return
     */
    private void initData() {

        mNotes = new ArrayList<Notes>();

//        for (int i = 0; i < 20; i++) {
//            Notes note=new Notes();
//            note.setTitle("这是标题"+i);
//            note.setContents("包括新增一张表，新增表中的一个字段，删除表中的一个字段，删除表等，每一次的升级操作litepal.xml文件中数据库的版本号都必须加");
//            note.setTime(getCurrentTime());
//
//            mNotes.add(note);
//        }

        mNoteDbOpenHelper = new NoteDbOpenHelper(this);


    }

    /**
     * @method  initEvents
     * @description 初始化事件，完成事件绑定
     * @date: 2021/9/10 20:06
     * @author: wangxianwen
     * @param
     * @return
     */
    private void initEvents() {
        mAdapter = new MyAdapter(this, mNotes);     // 构造 Adapter 对象

        mRecyclerView.setAdapter(mAdapter);     // 将mRecyclerView绑定到 Adapter上
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(linearLayoutManager);
        setListLayout();

    }

    /**
     * @method setListLayout
     * @description 设置布局
     * @date: 2021/9/10 20:07
     * @author: wangxianwen
     * @param
     * @return
     */
    private void setListLayout() {
        currentListLayoutMode = SpfUtil.getIntWithDefault(this, KEY_LAYOUT_MODE, MODE_LINEAR);
        Log.d(TAG, "setListLayout: " + currentListLayoutMode);
        if (currentListLayoutMode == MODE_LINEAR) {
            setToLinearList();
        } else {
            setToGridList();

        }
    }

    /**
     * @method  getCurrentTime
     * @description 获取当前时间信息
     * @date: 2021/9/10 20:07
     * @author: wangxianwen
     * @param
     * @return
     */
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);

    }


    /**
     * @method  addNotes
     * @description 启动添加笔记的界面
     * @date: 2021/9/10 20:08
     * @author: wangxianwen
     * @param
     * @return
     */
    public void addNotes(View view) {
        Intent intent = new Intent(this, AddActivity.class);

        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();


        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 点击提交后才查
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 实时输入就查询
            @Override
            public boolean onQueryTextChange(String newText) {
                if (mNoteDbOpenHelper == null) {
                    mNoteDbOpenHelper = new NoteDbOpenHelper(MainActivity.this);
                }
                mNotes = mNoteDbOpenHelper.queryByTitle(newText);

                mAdapter.refreshDate(mNotes); // 刷新

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @method  onOptionsItemSelected
     * @description 菜单设置
     * @date: 2021/9/10 20:08
     * @author: wangxianwen
     * @param
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.menu_linear:  // 列表视图
                setToLinearList();
                currentListLayoutMode = MODE_LINEAR;  // 设为列表布局 之后会持久化保存布局模式
                SpfUtil.saveInt(this, KEY_LAYOUT_MODE, currentListLayoutMode);    // 持久化到SharedPreferences
                return true;
            case R.id.menu_grid:    // 网格视图
                setToGridList();
                currentListLayoutMode = MODE_GRID;  // 设为列表布局 之后会持久化保存布局模式
                SpfUtil.saveInt(this, KEY_LAYOUT_MODE, currentListLayoutMode);    // 持久化到SharedPreferences
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (currentListLayoutMode == MODE_LINEAR) {
            MenuItem item = menu.findItem(R.id.menu_linear);
            item.setChecked(true);
        } else {
            menu.findItem(R.id.menu_grid).setChecked(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * @method  setToLinearList
     * @description 设置为列表布局
     * @date: 2021/9/10 20:09
     * @author: wangxianwen
     * @param
     * @return
     */
    private void setToLinearList() {

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter.setViewType(MyAdapter.TYPE_LINEAR_LAYOUT);
        mAdapter.notifyDataSetChanged();
    }


    /**
     * @method  setToGridList
     * @description 设置为网格布局
     * @date: 2021/9/10 20:10
     * @author: wangxianwen
     * @param
     * @return
     */
    private void setToGridList() {

        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter.setViewType(MyAdapter.TYPE_GRID_LAYOUT);
        mAdapter.notifyDataSetChanged();

    }


}