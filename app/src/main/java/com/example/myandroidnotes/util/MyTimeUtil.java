package com.example.myandroidnotes.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyTimeUtil {
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date=new Date();
        return sdf.format(date);

    }
}
