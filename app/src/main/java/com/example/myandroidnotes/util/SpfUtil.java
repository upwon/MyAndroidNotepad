package com.example.myandroidnotes.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @ProjectName: SharedPreferences 工具类
 * @Package: com.example.myandroidnotes.util
 * @ClassName:
 * @Description: java类作用描述
 * @Author: wangxianwen
 * @CreateDate: 2021/9/10 22:27
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/9/10 22:27
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class SpfUtil {
    private static String SPF_NAME = "noteSpf";

    /**
     * @method  saveString
     * @description 存储键值对至SharedPreferences
     * @date: 2021/9/10 22:28
     * @author: wangxianwen
     * @param
     * @return
     */
    public static void saveString(Context context, String key, String value) {
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(key, value);
        edit.apply();
    }

    /**
     * @method
     * @description 从SharedPreferences获取 键值对的value
     * @date: 2021/9/10 22:29
     * @author: wangxianwen
     * @param
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        return spf.getString(key, "");
    }

    public static void saveInt(Context context, String key, int value) {
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        return spf.getInt(key, -1);
    }

    public static int getIntWithDefault(Context context, String key, int defValue) {
        SharedPreferences spf = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        return spf.getInt(key, defValue);
    }

}
