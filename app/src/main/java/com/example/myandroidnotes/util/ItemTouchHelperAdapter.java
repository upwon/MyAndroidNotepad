package com.example.myandroidnotes.util;


/**
 * 数据操作的抽象接口 | Adaapter 会实现该接口
 *
 * @author wangxianwen
 * @version 1.0
 * @ProjectName
 * @Package com.example.myandroidnotes.util
 * @ClassName
 * @CreateDate 2021/9/28 21:25
 * @UpdateUser 更新者
 * @UpdateDate 2021/9/28 21:25
 * @UpdateRemark 更新内容
 */
public interface ItemTouchHelperAdapter {


    /**
     * 长按时的移动事件
     *
     * @param fromPosition 开始移动的 item position
     * @param toPosition   移动到 item position 位置
     * @return
     * @description 描述一下方法的作用
     * @method
     * @date 2021/9/28 21:19
     * @author wangxianwen
     */
    boolean onItemMove(int fromPosition, int toPosition);


    /**
     * Called when an item has been dismissed by a swipe.
     *
     * @param position 操作的item
     * @date 2021/9/14 22:41
     * @author wangxianwen
     */
    void onItemDismiss(int position);

}
