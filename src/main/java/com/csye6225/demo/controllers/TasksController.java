package com.csye6225.demo.controllers;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.csye6225.demo.repo.TaskAttachmentRepository;
import com.csye6225.demo.repo.TasksRepository;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TasksController {

    @Autowired
    private TasksRepository taskrepo;

    @Autowired
    private TaskAttachmentRepository taskAttachmentRepo;

    @RequestMapping(value = "/tasks/{id}/attachments", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity addAttachments(HttpServletRequest request, RequestBody attachment[]) {
        JsonObject json = new JsonObject();

        return null;
    }

}
