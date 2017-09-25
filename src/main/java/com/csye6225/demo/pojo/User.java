package com.csye6225.demo.pojo;

import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;


@Entity
public class User implements Persistable {


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


    @Override
    public Serializable getId() {
        return userName;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
