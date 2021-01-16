// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   User.java

package ssm.zmh.webchat.model.user;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class User
        implements Serializable,Cloneable {

    public User(String mail, String userName, String password, String userToken) {
        this.mail = mail;
        this.userName = userName;
        this.password = password;
        this.userToken = userToken;
    }

    public User() {
    }
    public User(int userId,String userTargetNumber){
        this.userId = userId;
        this.userTargetNumber = userTargetNumber;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserToken() {
        return userToken;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID() {
        userUUID = UUID.randomUUID().toString().replace("-", "");
    }

    public String getUserTargetNumber() {
        return userTargetNumber;
    }

    public void setUserTargetNumber(String userTargetNumber) {
        this.userTargetNumber = userTargetNumber;
    }

    public User clone()
            throws CloneNotSupportedException {
        return (User) super.clone();
    }


    private int userId = -1;
    private String mail;
    private String userName;
    private String password;
    private String userUUID;
    private String userTargetNumber;
    private Date createDate;
    private String userToken;

}
