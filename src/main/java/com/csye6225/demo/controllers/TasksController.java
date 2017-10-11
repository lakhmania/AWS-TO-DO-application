package com.csye6225.demo.controllers;

import com.csye6225.demo.pojo.Tasks;
import com.csye6225.demo.repo.TasksRepository;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.websocket.server.PathParam;

@Controller
public class TasksController {

    @Autowired
    private TasksRepository taskRepo;

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> updateTasks(@PathParam("id") String id, String description) {

        JsonObject jsonObject = new JsonObject();

        Tasks task = taskRepo.findByTaskId(id);
        if (description.length() > 4096) {
            return new ResponseEntity("Description length exceded the defined length", HttpStatus.BAD_REQUEST);
        }

        return null;
    }
}
