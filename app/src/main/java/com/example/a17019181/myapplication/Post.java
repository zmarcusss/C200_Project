package com.example.a17019181.myapplication;

public class Post {
    private String middleMax;
    private String time;
    private String middleDegree;


     public Post() {

     }

    public Post(String content) {
        this.middleMax = content;
    }


    public String getMiddleMax() {
        return middleMax;
    }

    public void setMiddleMax(String middleMax) {
        this.middleMax = middleMax;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMiddleDegree() {
        return middleDegree;
    }

    public void setMiddleDegree(String middleDegree) {
        this.middleDegree = middleDegree;
    }
}
