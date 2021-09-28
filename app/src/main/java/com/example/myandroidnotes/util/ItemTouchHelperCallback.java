package com.example.myandroidnotes.util;


import android.app.Service;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myandroidnotes.adapter.MyAdapter;

/**
 * 使用 ItemTouchHelper 需要一个 Callback，
 * 该 Callback 是 ItemTouchHelper.Callback 的子类，
 * 所以我们需要新建一个类比如 ItemTouchHelperCallback
 * 继承自 ItemTouchHelper.Callback。
 * 可以重写其数个方法来满足需求。
 *
 * @ClassName: ItemTouchHelperCallback
 * @Description: java类作用描述
 * @Author: wangxianwen
 * @Date: 2021/9/14 22:02
 */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private static final String TAG = "ItemTouchHelperCallback";
    private static final float ALPHA_FULL = 1.0f;
    private ItemTouchHelperAdapter mAdapter;


    public ItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        this.mAdapter = adapter;
    }


    /**
     * 是否开启长按拖动，是则 return true
     * 若针对一些item想要不可拖拽，则 return false 并 重写 onItemLongClick
     *
     * @param
     * @return
     * @method
     * @description 描述一下方法的作用
     * @date: 2021/9/14 22:14
     * @author: wangxianwen
     */
    @Override
    public boolean isLongPressDragEnabled() {
        // 支持长按进入拖动操作
        return true;
    }

    /**
     * Item是否可以滑动
     * Returns: True if ItemTouchHelper should start swiping an item when user swipes a pointer over the View,
     * false otherwise. Default value is true.
     *
     * @param
     * @return
     * @method
     * @description 描述一下方法的作用
     * @date: 2021/9/14 22:47
     * @author: wangxianwen
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    /**
     * 指定可以支持的拖放和滑动的方向，上下为拖动（drag），左右为滑动（swipe）
     *
     * @param
     * @return
     * @method
     * @description 描述一下方法的作用
     * @date: 2021/9/14 22:51
     * @author: wangxianwen
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

        // 分别设置 列表与网格的拖拽与滑动

        // 列表布局 能够上下拖动拖拽与滑动
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            Log.d(TAG, "getMovementFlags: LinearLayoutManager布局");
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        // 网格布局设置能够上下左右四个方向拖动 不需要滑动
        else if (recyclerView.getLayoutManager() instanceof GridLayoutManager
                || recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            Log.d(TAG, "getMovementFlags: " + recyclerView.getLayoutManager().toString() + "布局");
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        Log.d(TAG, "getMovementFlags: " + "出现异常布局");
        return 0;
    }


    /**
     * 从旧位置滑动到新位置
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return 布尔值
     * @date 2021/9/14 23:14
     * @author wangxianwen
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        // 类型判断
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            Log.d(TAG, "onMove: " + "ViewHolder类型不相同" + "viewHolder.getItemViewType() = " + viewHolder.getItemViewType()
                    + "target.getItemViewType() = " + target.getItemViewType());
            return false;
        }
        Log.d(TAG, "onMove: target.getItemViewType() = " + target.getItemViewType());
        Log.d(TAG, "onMove: viewHolder.getAdapterPosition()= " + viewHolder.getAdapterPosition() + "  target.getAdapterPosition() = " + target.getAdapterPosition());
        // 正常情况则通知 adapter 有移动事件发生
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;

    }

    /**
     * 滑动结束消失时调用
     *
     * @param
     * @return
     * @description 描述一下方法的作用
     * @method
     * @date: 2021/9/14 23:20
     * @author: wangxianwen
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // 自定义滑动动画
            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);


        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }

    }

    /**
     * 当 ItemTouchHelper 滑动或拖动的 ViewHolder 发生更改时调用。
     *
     * @param
     * @return
     * @description 描述一下方法的作用
     * @method
     * @date: 2021/9/14 23:36
     * @author: wangxianwen
     */
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {


        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            Log.d(TAG, "onSelectedChanged: actionState != ItemTouchHelper.ACTION_STATE_IDLE , actionState = " + actionState);

            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                Log.d(TAG, "onSelectedChanged: viewHolder instanceof ItemTouchHelperViewHolder");
                ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                // 选中状态回调
                itemViewHolder.onItemSelected();
            } else {
                Log.d(TAG, "onSelectedChanged: viewHolder 不是 instanceof ItemTouchHelperViewHolder");
                Log.d(TAG, "onSelectedChanged: viewHolder= " + viewHolder.getClass());

            }

            super.onSelectedChanged(viewHolder, actionState);


        }

    }

    /**
     * 拖拽或者滑动结束后调用
     *
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        // 清除背景颜色设置
        viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);

        viewHolder.itemView.setAlpha(ALPHA_FULL);

        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            Log.d(TAG, "clearView: 此处是 ItemTouchHelperViewHolder");
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            // 未选中状态回调
            itemViewHolder.onItemClear();
        } else {
            Log.d(TAG, "clearView: 此处不是 ItemTouchHelperViewHolder");

        }


    }
}
