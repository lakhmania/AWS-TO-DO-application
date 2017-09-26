package com.csye6225.demo.pojo;
import org.springframework.data.domain.Persistable;
import javax.persistence.*;
import java.io.Serializable;

@Entity
public class UserSession implements Persistable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int userSessionId;

    @ManyToOne
    @JoinColumn(name="userId")
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