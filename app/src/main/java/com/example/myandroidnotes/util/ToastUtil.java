package com.example.myandroidnotes.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @ProjectName:
 * @Package: com.example.myandroidnotes.util
 * @ClassName:
 * @Description: Toast
 * @Author: wangxianwen
 * @CreateDate: 2021/9/11 14:52
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/9/11 14:52
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class ToastUtil {

    public static void toastShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
