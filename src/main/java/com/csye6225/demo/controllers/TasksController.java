package com.csye6225.demo.controllers;

import com.csye6225.demo.pojo.Tasks;
import com.csye6225.demo.repo.TasksRepository;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;


@RequestMapping("/tasks")
@Controller
public class TasksController {

    @Autowired
    private TasksRepository taskRepo;


    @RequestMapping( method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> createTask(HttpServletRequest request) {
       // JsonObject jsonObject = new JsonObject();
        String taskDescription = request.getParameter("description");
        if(taskDescription.length()>4096){
            return new ResponseEntity<>("Description length should not exceed 4096", HttpStatus.BAD_REQUEST);
        }

        try {
            UUID id = UUID.randomUUID();
            Tasks task = new Tasks();
            task.setDescription(taskDescription);
            task.setTaskId(id.toString());
            taskRepo.save(task);
        }
        catch (Exception e){
            return new ResponseEntity<>("Task Creation failed",HttpStatus.INTERNAL_SERVER_ERROR );
        }
        return new ResponseEntity<>("Task Created Succesfully ", HttpStatus.OK);

    }

}
