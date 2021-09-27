package com.example.myandroidnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myandroidnotes.DB.NoteDbOpenHelper;
import com.example.myandroidnotes.adapter.MyAdapter;
import com.example.myandroidnotes.util.ItemTouchHelperCallback;
import com.example.myandroidnotes.util.SpfUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yyp.editor.RichEditor;
import com.yyp.editor.widget.EditorOpMenuView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author 12447
 */
public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    /**
     * 记录布局为列表视图还是网格布局
     */
    private int currentListLayoutMode = MODE_LINEAR;
    private RecyclerView mRecyclerView;
    private ItemTouchHelper itemTouchHelper;
    private FloatingActionButton mButtonAdd;
    private List<Notes> mNotes;
    private MyAdapter mAdapter;
    private NoteDbOpenHelper mNoteDbOpenHelper;
    public static final String KEY_LAYOUT_MODE = "key_layout_mode";
    public static final int MODE_LINEAR = 0;
    public static final int MODE_GRID = 1;



    /**
     * 初始化View->初始化数据->初始化事件
     * @param savedInstanceState
     * @author  wangxianwen
     * @date 2021/9/8 19:48
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
     * 重写onResume 保证打开软件或新增笔记后 数据刷新
     * @date 2021/9/10 19:51
     * @author wangxianwen
     */
    @Override
    protected void onResume() {
        super.onResume();
        refreshDataFromDb();
        setListLayout();
    }



    /**
     * 从数据库中获取所有数据  并让Adapter去通知UI刷新示数据
     * @date  2021/9/27 19:44
     * @author wangxianwen
     */
    private void refreshDataFromDb() {
        mNoteDbOpenHelper = new NoteDbOpenHelper(this);
        mNotes = mNoteDbOpenHelper.queryAllNotes();
        mAdapter.refreshDate(mNotes);
    }


    private void initView() {

        mRecyclerView = findViewById(R.id.recycler_view);

    }

    /**
     * @param
     * @return
     * @method
     * @description 初始化数据库操作对象
     * @date: 2021/9/10 20:06
     * @author: wangxianwen
     */
    private void initData() {

        mNotes = new ArrayList<Notes>();

        mNoteDbOpenHelper = new NoteDbOpenHelper(this);


    }

    /**
     * @param
     * @return
     * @method initEvents
     * @description 初始化事件，完成事件绑定
     * @date: 2021/9/10 20:06
     * @author: wangxianwen
     */
    private void initEvents() {
        // 构造 Adapter 对象
        mAdapter = new MyAdapter(this, mNotes);
        // 将mRecyclerView绑定到 Adapter上
        mRecyclerView.setAdapter(mAdapter);

        setListLayout();

    }

    /**
     * @param
     * @return
     * @method setListLayout
     * @description 设置布局
     * @date: 2021/9/10 20:07
     * @author: wangxianwen
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
     * @param
     * @return
     * @method setToLinearList
     * @description 设置为列表布局
     * @date: 2021/9/10 20:09
     * @author: wangxianwen
     */
    @SuppressLint("NotifyDataSetChanged")
    private void setToLinearList() {

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter.setViewType(MyAdapter.TYPE_LINEAR_LAYOUT);
        mAdapter.notifyDataSetChanged();


        ItemTouchHelperCallback itemTouchHelperCallback = new ItemTouchHelperCallback(mAdapter);
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }


    /**
     * @param
     * @return
     * @method setToGridList
     * @description 设置为网格布局
     * @date: 2021/9/10 20:10
     * @author: wangxianwen
     */
    @SuppressLint("NotifyDataSetChanged")
    private void setToGridList() {

        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter.setViewType(MyAdapter.TYPE_GRID_LAYOUT);
        mAdapter.notifyDataSetChanged();

        ItemTouchHelperCallback itemTouchHelperCallback = new ItemTouchHelperCallback(mAdapter);
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }


    /**
     * @param
     * @return
     * @method addNotes
     * @description 启动添加笔记的界面
     * @date: 2021/9/10 20:08
     * @author: wangxianwen
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
     * @param
     * @return
     * @method onOptionsItemSelected
     * @description 菜单设置
     * @date: 2021/9/10 20:08
     * @author: wangxianwen
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
            // 列表视图
            case R.id.menu_linear:
                setToLinearList();
                // 设为列表布局 之后会持久化保存布局模式
                currentListLayoutMode = MODE_LINEAR;
                // 持久化到SharedPreferences
                SpfUtil.saveInt(this, KEY_LAYOUT_MODE, currentListLayoutMode);
                return true;
            // 网格视图
            case R.id.menu_grid:
                setToGridList();
                // 设为列表布局 之后会持久化保存布局模式
                currentListLayoutMode = MODE_GRID;
                // 持久化到SharedPreferences
                SpfUtil.saveInt(this, KEY_LAYOUT_MODE, currentListLayoutMode);
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


}