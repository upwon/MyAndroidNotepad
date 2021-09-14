package com.example.myandroidnotes.util;

/**
 * 该接口用于通知 ViewHolder 现有回调
 * @ProjectName:
 * @Package: com.example.myandroidnotes.util
 * @ClassName:
 * @Description: java类作用描述
 * @Author: wangxianwen
 * @CreateDate: 2021/9/14 23:46
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/9/14 23:46
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public interface ItemTouchHelperViewHolder {

    /**
     * Called when the {@link ItemTouchHelper} first registers an item as being moved or swiped.
     * Implementations should update the item view to indicate it's active state.
     */
    void onItemSelected();

    /**
     * Called when the {@link ItemTouchHelper} has completed the move or swipe, and the active item
     * state should be cleared.
     */
    void onItemClear();

}
