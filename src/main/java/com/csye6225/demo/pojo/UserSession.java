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