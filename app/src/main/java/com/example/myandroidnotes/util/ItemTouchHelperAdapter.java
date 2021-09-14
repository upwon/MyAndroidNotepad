package com.example.myandroidnotes.util;

public interface ItemTouchHelperAdapter {

    /**
     * 长按时的移动事件
     *
     * @param fromPosition 开始移动的 item position
     * @return toPosition  移动到 item position 位置
     * @method
     * @description 描述一下方法的作用
     * @date: 2021/9/14 22:32
     * @author: wangxianwen
     */
    boolean onItemMove(int fromPosition, int toPosition);


    /**
     * Called when an item has been dismissed by a swipe.
     *
     * @param position The position of the item dismissed.
     * @return
     * @method
     * @description 描述一下方法的作用
     * @date: 2021/9/14 22:41
     * @author: wangxianwen
     */
    void onItemDismiss(int position);

}
