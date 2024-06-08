package com.example.studentsystem.bean;

import java.time.LocalDateTime;

public class courseInfo {
    private String tid;
    private String cid;
    private LocalDateTime time;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
