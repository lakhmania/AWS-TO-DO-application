/**
 * <Neha Lalwani>, <001268916>, <lalwani.n@husky.neu.edu>
 * <Nirali Merchant>, <001268909>, <merchant.n@husky.neu.edu>
 * <Chintan Koticha>, <001267049>, <koticha.c@husky.neu.edu>
 * <Apoorva Lakhmani>, <001256312>, <lakhmani.a@husky.neu.edu>
 */

package com.csye6225.demo.pojo;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User implements Persistable {

    public User(){}

    public User(String userName, String password) {

        this.userName = userName;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column
    private String userName;

    @Column
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Tasks> tasks;

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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<Tasks> getTasks() {
        return tasks;
    }

    public void setTasks(List<Tasks> tasks) {
        this.tasks = tasks;
    }

    @Override
    public Serializable getId() {
        return userId;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}