package com.example.myandroidnotes;

import androidx.annotation.NonNull;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Notes extends LitePalSupport implements Serializable  {



        private long id;
//        private Integer item_imageID;
        private String title;
        private String contents;
        private String time;

    public Notes() {

    }

    public Notes(String title, String contents, String time) {
         //   this.item_imageID = item_imageID;
            this.title = title;
            this.contents = contents;
            this.time = time;
        }


        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

//        public Integer getItem_imageID() {
//            return item_imageID;
//        }
//
//        public void setItem_imageID(Integer item_imageID) {
//            this.item_imageID = item_imageID;
//        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }


    @NonNull
    @Override
    public String toString() {
        return "Note{"+
                "title='"+title+'\''+
                ", content='"+contents+'\''+
                ", createdTime='"+time+'\''+
                ", id="+id+'\''+
                '}';
    }
}
