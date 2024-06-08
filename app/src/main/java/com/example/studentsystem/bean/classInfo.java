package com.example.studentsystem.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDate;

public class classInfo implements Parcelable {
    private String classNum;
    private String className;
    private String teacherNum;
    private LocalDate time;

    public static final Creator<classInfo> CREATOR = new Creator<classInfo>() {
        @Override
        public classInfo createFromParcel(Parcel in) {
            return new classInfo(in);
        }

        @Override
        public classInfo[] newArray(int size) {
            return new classInfo[size];
        }
    };

    public classInfo(){

    }

    protected classInfo(Parcel in) {
        this.classNum = in.readString();
        this.className = in.readString();
        this.teacherNum = in.readString();
        this.time = (LocalDate) in.readSerializable();
    }

    public String getClassNum() {
        return classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTeacherNum() {
        return teacherNum;
    }

    public void setTeacherNum(String teacherNum) {
        this.teacherNum = teacherNum;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(classNum);
        dest.writeString(className);
        dest.writeString(teacherNum);
        dest.writeSerializable(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
