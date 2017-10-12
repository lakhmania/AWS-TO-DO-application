package com.csye6225.demo.pojo;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class TaskAttachments implements Persistable {

    public TaskAttachments(){}
    public TaskAttachments(String fileName){
        this.fileName = fileName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int taskAttachmentId;

    @Column
    private String fileName;

    @ManyToOne
    @JoinColumn(name="taskId")
    private Tasks task;

    public int getTaskAttachmentId() {
        return taskAttachmentId;
    }

    public void setTaskAttachmentId(int taskAttachmentId) {
        this.taskAttachmentId = taskAttachmentId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Tasks getTask() {
        return task;
    }

    public void setTask(Tasks task) {
        this.task = task;
    }

    @Override
    public Serializable getId() {
        return taskAttachmentId;
    }

    @Override
    public boolean isNew() {
        return true;
    }

}
