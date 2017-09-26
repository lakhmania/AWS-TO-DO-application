package com.csye6225.demo.pojo;

import javax.persistence.Column;
import javax.persistence.Id;

@Entity
public class User {


    @Id
    String userName;

    @Column
    String password;

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


}
