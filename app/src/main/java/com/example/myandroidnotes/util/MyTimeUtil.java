package com.example.myandroidnotes.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ProjectName:
 * @Package: com.example.myandroidnotes.util
 * @ClassName: MyTimeUtil
 * @Description: 与时间相关的操作类
 * @Author: wangxianwen
 * @CreateDate: 2021/9/10 22:25
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/9/10 22:25
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class MyTimeUtil {

    /**
     * @method  getCurrentTime
     * @description 获取当前时间 格式为 yyyy年MM月dd日 HH:mm:ss
     * @date: 2021/9/10 22:25
     * @author: wangxianwen
     */
    public static String getCurrentTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);

    }
}
