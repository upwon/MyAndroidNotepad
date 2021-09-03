# MyAndroidNotepad



## 功能简介

一款简单的Android笔记软件，实现笔记的添加、编辑、删除等操作，支持关键字实时搜索笔记；支持列表布局以及网格布局用以呈现笔记。



### 笔记界面

![](https://cdn.jsdelivr.net/gh/upwon/MyPicture@master/imgimage-20210903200003606.png)



### 笔记的增加

![](https://tva1.sinaimg.cn/large/003pPIslgy1gu3pj7vd5zg60c90p6b2a02.gif)



### 笔记的删除与修改

![](https://tva2.sinaimg.cn/large/003pPIslgy1gu3pmbz90qg60c80p64qq02.gif)



### 笔记的查询

![](https://tvax2.sinaimg.cn/large/003pPIslgy1gu3pp4h9xug60c80p61kx02.gif)



## 数据存储





笔记内容持久化保存至本地 SQLite ，软件属性设置利用 SharedPreferences 保存键值对数据，软件启动后会读取属性配置，从而保证设置不会恢复默认。



> - 想要保存的相对较小键值对集合，则应使用 `SharedPreferences` API。`SharedPreferences` 对象指向包含键值对的文件，并提供读写这些键值对的简单方法。每个 `SharedPreferences` 文件均由框架进行管理，可以是私有文件，也可以是共享文件。
> - 结构化数据保存至数据库
> - 媒体文件或其他较大文件可保存至手机存储卡或云端，然后在数据库中存放其路径
>
> 





