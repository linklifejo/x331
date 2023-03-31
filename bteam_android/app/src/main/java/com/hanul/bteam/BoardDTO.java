package com.hanul.bteam;

import java.io.Serializable;

// 1. DB에 있는 테이블을 기본으로 하여 DTO를 만든다
public class BoardDTO implements Serializable {
    String title, memo,mountin_name,course_name,filename,pathname;
    int no, location_no,course_no,resId;

    public BoardDTO(String title, String memo, String mountin_name, String course_name, String filename, String pathname, int no, int location_no, int course_no, int resId) {
        this.title = title;
        this.memo = memo;
        this.mountin_name = mountin_name;
        this.course_name = course_name;
        this.filename = filename;
        this.pathname = pathname;
        this.no = no;
        this.location_no = location_no;
        this.course_no = course_no;
        this.resId = resId;
    }
//xxxxxxxxxxxxxxxxx
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMountin_name() {
        return mountin_name;
    }

    public void setMountin_name(String mountin_name) {
        this.mountin_name = mountin_name;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPathname() {
        return pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getLocation_no() {
        return location_no;
    }

    public void setLocation_no(int location_no) {
        this.location_no = location_no;
    }

    public int getCourse_no() {
        return course_no;
    }

    public void setCourse_no(int course_no) {
        this.course_no = course_no;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
