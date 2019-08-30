package com.tdyh.android.tdmvp.bean;

/**
 * Created by gaozh on 2019/8/30.<p>
 */
public class UserInfo {
    private String userName;
    private String sex;
    private String addr;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userName='" + userName + '\'' +
                ", sex='" + sex + '\'' +
                ", addr='" + addr + '\'' +
                '}';
    }
}
