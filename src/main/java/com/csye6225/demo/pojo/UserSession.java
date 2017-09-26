package com.csye6225.demo.pojo;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;

public class UserSession implements Persistable {

    @Id
    @ManyToOne
    @JoinColumn(name="id")
    private User user;

    @Column
    private String sessionId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public Serializable getId() {
        return null;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
