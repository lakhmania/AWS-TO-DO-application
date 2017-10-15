package com.csye6225.demo.pojo;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;
import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
public class Tasks implements Persistable {


    public Tasks(){
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID taskId;

    @Column(length = 5000)
    private String description;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private Set<TaskAttachments> taskAttachments;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<TaskAttachments> getTaskAttachments() {
        return taskAttachments;
    }

    public void setTaskAttachments(Set<TaskAttachments> taskAttachments) {
        this.taskAttachments = taskAttachments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Serializable getId() {
        return taskId;
    }


    @Override
    public boolean isNew() {
        return true;
    }




}
