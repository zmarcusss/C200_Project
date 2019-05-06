package com.example.a17019181.myapplication;

public class userInformation {
    public String condition;
    public String mobile_no;
    public String userName;

    public userInformation(String condition, String mobile_no, String userName) {
        this.condition = condition;
        this.mobile_no = mobile_no;
        this.userName = userName;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
