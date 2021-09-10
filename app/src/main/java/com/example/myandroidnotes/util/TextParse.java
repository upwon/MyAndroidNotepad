package com.example.myandroidnotes.util;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: TextParse
 * @Description: 从HTML中解析文本
 * @Author: wangxianwen
 * @Date: 2021/9/10 19:53
 */

public class TextParse {

    /**
     * @method getPlainTextFromHTML
     * @description 从HTML提取出出文本 改方法只能简单地去掉空格，制表符，回车，换行
     * @date: 2021/9/10 20:01
     * @author: wangxianwen
     * @param
     * @return
     */
    public static String  getPlainTextFromHTML(String inputHTML){

        String regMatchEnter="\\s*|\t|\r|\n";

        Pattern p = Pattern.compile(regMatchEnter);
        Matcher m = p.matcher(inputHTML);
        String outputText=m.replaceAll("");

        return outputText;
    }

    /**
     * @method  parsePlainTextFromHTML
     * @description 从HTML解析文本  使用Jsoup库
     * @date: 2021/9/10 22:31
     * @author: wangxianwen
     * @param
     * @return
     */
    public static String  parsePlainTextFromHTML(String inputHTML) {
        Document d = Jsoup.parse(inputHTML);
        String text = d.text();
        return text;
    }

    /**
     * @method  Html2Text
     * @description 从html中提取纯文本
     * @date: 2021/9/10 20:02
     * @author: wangxianwen
     * @param
     * @return
     */
    public static String Html2Text(String inputString) {
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;
        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            textStr = htmlStr;
        } catch (Exception e) {System.err.println("Html2Text: " + e.getMessage()); }
        //剔除空格行
        textStr=textStr.replaceAll("[ ]+", " ");
        textStr=textStr.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
        return textStr;// 返回文本字符串
    }

}
