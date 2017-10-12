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

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> deleteTask(@PathParam("id") String id) {

        JsonObject jsonObject = new JsonObject();

        Tasks task = taskRepo.findByTaskId(id);
        if (task==null) {
            return new ResponseEntity("Task Id not found", HttpStatus.BAD_REQUEST);
        }
        else{
            try{

                taskRepo.deleteByTaskId(id);
                return new ResponseEntity("No Content", HttpStatus.NO_CONTENT);

            }
            catch(Exception e){

                return new ResponseEntity("Couldnot delete task", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }

    }



/*
    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> updateTasks(@PathParam("id") String id, String description) {

        JsonObject jsonObject = new JsonObject();

        Tasks task = taskRepo.findByTaskId(id);
        if (description.length() > 4096) {
            return new ResponseEntity("Description length exceded the defined length", HttpStatus.BAD_REQUEST);
        }


        return null;
    }*/
}
