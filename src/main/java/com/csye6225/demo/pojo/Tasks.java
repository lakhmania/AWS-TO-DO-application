package com.csye6225.demo.pojo;

import org.springframework.data.domain.Persistable;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Entity
public class Tasks implements Persistable {


    public Tasks(){}

    public Tasks(String taskId, String description) {

        this.taskId = taskId;
        this.description = description;

    }

    @Id
    private String taskId;

    @Column
    private String description;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private Set<TaskAttachments> taskAttachments;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
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

    @Override
    public Serializable getId() {
        return taskId;
    }


    @Override
    public boolean isNew() {
        return true;
    }




}
