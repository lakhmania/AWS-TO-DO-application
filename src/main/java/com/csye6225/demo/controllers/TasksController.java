/**
 * <Neha Lalwani>, <001268916>, <lalwani.n@husky.neu.edu>
 * <Nirali Merchant>, <001268909>, <merchant.n@husky.neu.edu>
 * <Chintan Koticha>, <001267049>, <koticha.c@husky.neu.edu>
 * <Apoorva Lakhmani>, <001256312>, <lakhmani.a@husky.neu.edu>
 */

package com.csye6225.demo.controllers;


import com.csye6225.demo.pojo.Description;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.csye6225.demo.pojo.TaskAttachments;
import com.csye6225.demo.pojo.Tasks;
import com.csye6225.demo.pojo.User;
import com.csye6225.demo.repo.TaskAttachmentRepository;
import com.csye6225.demo.repo.TasksRepository;
import com.csye6225.demo.repo.UserRepository;
import org.json.simple.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.Charset;
import java.util.stream.Collectors;

@RequestMapping("/tasks")
@Controller
public class TasksController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TaskAttachmentRepository taskAttachmentRepo;

    @Autowired
    private TasksRepository taskRepo;


    @RequestMapping( method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> createTask(@RequestBody Description desc, HttpServletRequest request) {

        String auth = request.getHeader("Authorization");
        JsonObject json = new JsonObject();

        if (auth != null && auth.startsWith("Basic")) {
            String base64Credentials = auth.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));

            String[] values = credentials.split(":", 2);
            User user = userRepo.findByUserName(values[0]);
            String description = desc.getDescription();
                if(description.length()>4096){
                    json.addProperty("message","Description length should not exceed 4096");
                    return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
                }

                try {
                    UUID id = UUID.randomUUID();
                    Tasks task = new Tasks();
                    task.setDescription(description);
                    task.setTaskId(id.toString());
                    task.setUser(user);
                    user.getTasks().add(task);

                    userRepo.save(user);
                }
                catch (Exception e){
                    json.addProperty("message","Description length should not exceed 4096");
                    return new ResponseEntity<>(json.toString(),HttpStatus.INTERNAL_SERVER_ERROR );
                }
                json.addProperty("message","Task Created Succesfully");
                return new ResponseEntity<>(json.toString(), HttpStatus.OK);

        }
        else{

            json.addProperty("message","You are not logged in!!");
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }




    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> deleteTask(@PathVariable(value="id") String id,HttpServletRequest request) {

        JsonObject json = new JsonObject();

        String auth = request.getHeader("Authorization");

        if (auth != null && auth.startsWith("Basic")) {
            String base64Credentials = auth.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));

            String[] values = credentials.split(":", 2);

            Tasks task = taskRepo.findByTaskId(id);
            if (task==null) {
                json.addProperty("message","Task Id not found");
                return new ResponseEntity(json.toString(), HttpStatus.BAD_REQUEST);
            }
            else{
                try{
                    User taskUser = task.getUser();
                    if(values[0].equals(taskUser.getUserName())){
                        taskRepo.deleteByTaskId(id);
                        json.addProperty("message","Task deleted successfully");
                        System.out.print(json.toString());
                        return new ResponseEntity(json.toString(), HttpStatus.OK);
                    }
                    else{
                        json.addProperty("message","Given Task Id doesnot belong to you!!");
                        return new ResponseEntity(json.toString(), HttpStatus.BAD_REQUEST);
                    }

                }
                catch(Exception e){
                    System.out.println(e);
                    json.addProperty("message","Couldnot delete task");
                    return new ResponseEntity(json.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
                }

            }

        }
        else{

            json.addProperty("message","You are not logged in!!");
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }
    }



    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> updateTasks(@PathVariable("id") String id, @RequestBody String description, HttpServletRequest request) {

     JsonObject json = new JsonObject();
        Tasks task = taskRepo.findByTaskId(id);

        User taskUser = task.getUser();

        String auth = request.getHeader("Authorization");

        if (auth != null && auth.startsWith("Basic")) {
            String base64Credentials = auth.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                 Charset.forName("UTF-8"));

            final String[] values = credentials.split(":", 2);

            if(!values[0].equals(taskUser.getUserName())){
                json.addProperty("message","Not an authorized user");
                 return new ResponseEntity(json.toString(),HttpStatus.FORBIDDEN);
            }
        }
        else{

            json.addProperty("message","You are not logged in!!");
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }


        if (description.length() > 4096) {
            json.addProperty("message","description more than 4096 characters");
            return new ResponseEntity(json.toString(),HttpStatus.BAD_REQUEST);
        }

        taskRepo.updateTaskDescription(description,task.getTaskId());
        json.addProperty("message","description updated");
        return new ResponseEntity(json.toString(),HttpStatus.OK);
}
  


    @RequestMapping( method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<String> getTasks(HttpServletRequest request) {

        //Take user object from Basic Authentication
        String auth = request.getHeader("Authorization");

        List<Tasks> tasks = new ArrayList();
        if (auth != null && auth.startsWith("Basic")) {
            String base64Credentials = auth.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));

            final String[] values = credentials.split(":", 2);
            System.out.println("User is ::::: " + values[0]);
            System.out.println(" Password is ::::: " + values[1]);

            User user = userRepo.findByUserName(values[0]);
            if(user==null){
                JsonObject json = new JsonObject();
                json.addProperty("message","User does not exists!!");
                return new ResponseEntity(json.toString(), HttpStatus.BAD_REQUEST);
            }

            tasks = taskRepo.findTasksByUser(user);
        }
        else{
            JsonObject json = new JsonObject();
            json.addProperty("message","You are not logged in!!");
            return new ResponseEntity(json.toString(), HttpStatus.BAD_REQUEST);
        }


        JSONArray jsonArray = new JSONArray();


        for (Tasks task : tasks) {
            JSONObject json = new JSONObject();

            json.put("id", task.getId());
            json.put("url", task.getDescription());
            JSONArray attachmentArr = new JSONArray();
            for (TaskAttachments attachments : task.getTaskAttachments()) {

                JSONObject attachmentObj = new JSONObject();
                attachmentObj.put("id", attachments.getId().toString());
                attachmentObj.put("url", attachments.getFileName());
                attachmentArr.add(attachmentObj);
            }

            json.put("attachments", attachmentArr);
            jsonArray.add(json);

        }
        return new ResponseEntity(jsonArray.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/attachments", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    ResponseEntity<String> getAttachments(@PathVariable(value = "id") String taskId, HttpServletRequest request) {

        Tasks task = taskRepo.findTasksByTaskId(taskId);

        String auth = request.getHeader("Authorization");

        String[] values = null;
        String username = null;
        JSONArray jsonArray = null;
        if (auth != null && auth.startsWith("Basic")) {
            String base64Credentials = auth.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));

            values = credentials.split(":", 2);
            username = values[0];
        }

        if (username.equals(task.getUser().getUserName())) {
            List<TaskAttachments> attachments = taskAttachmentRepo.findByTask(task);
            jsonArray = new JSONArray();
            for (TaskAttachments attachment : attachments) {
                JSONObject json = new JSONObject();
                json.put("id", attachment.getId().toString());
                json.put("url", attachment.getFileName());
                jsonArray.add(json);
            }
            return  new ResponseEntity<>(jsonArray.toString(), HttpStatus.ACCEPTED);
        }
        else
        {
            return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }


    @RequestMapping(value = "/tasks/{id}/attachments", method = RequestMethod.POST, produces = "application/json",consumes = "multipart/form-data")
    @ResponseBody
    public ResponseEntity<String> addAttachments(HttpServletRequest request, @PathVariable("id") String id,@RequestParam("files") MultipartFile[] uploadfiles) {
        JsonObject json = new JsonObject();
        //Tasks tasks = taskrepo.findById(id);
        Tasks task = taskRepo.findTasksByTaskId(id);
        if(task==null){
            json.addProperty("message","No such task found!!");
            return  new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }
        String auth = request.getHeader("Authorization");

        String[] values = null;
        String username = null;
        JSONArray jsonArray = null;
        if (auth != null && auth.startsWith("Basic")) {
            String base64Credentials = auth.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));

            values = credentials.split(":", 2);
            username = values[0];
        }

        if (username.equals(task.getUser().getUserName())) {
            //List<TaskAttachments> attachments = taskAttachmentRepo.findByTask(task);
            String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename()).filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
            try{
                saveUploadedFiles(Arrays.asList(uploadfiles),task);
            }catch(Exception e){
                System.out.println(""+e.getMessage());
            }

//            if (StringUtils.isEmpty(uploadedFileName)) {
//                json.addProperty("message","Please select a file!");
//                return new ResponseEntity(json.toString(),HttpStatus.BAD_REQUEST);
//            }

            json.addProperty("message","Saved the file(s)!");
            return new ResponseEntity(json.toString(), HttpStatus.OK);
        }
        else
        {
            return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/tasks/{id}/attachments/{idAttachments}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> deleteAttachments(HttpServletRequest request, @PathVariable("idAttachments") String attachmentid, @PathVariable("id") String id) {
        JsonObject json = new JsonObject();

        Tasks task = taskRepo.findTasksByTaskId(id);
        if(task==null){
            json.addProperty("message","No such task found!!");
            return  new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }

        String auth = request.getHeader("Authorization");
        String[] values = null;
        String username = null;
        JSONArray jsonArray = null;
        if (auth != null && auth.startsWith("Basic")) {
            String base64Credentials = auth.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));

            values = credentials.split(":", 2);
            username = values[0];
        }

        if (username.equals(task.getUser().getUserName())) {
            try{
                int aid = 0;
                try {
                    aid = Integer.parseInt(attachmentid);
                }catch(Exception e){
                    json.addProperty("message","Attachment Id can only be numbers!!");
                    return new ResponseEntity(json.toString(),HttpStatus.BAD_REQUEST);
                }
                TaskAttachments ta = taskAttachmentRepo.findTaskAttachmentsByTaskAttachmentId(aid);
                if(ta==null){
                    json.addProperty("message","No such attachments with the Id!!");
                    return new ResponseEntity(json.toString(),HttpStatus.BAD_REQUEST);
                }
                String path = ta.getFileName();
                Files.deleteIfExists(Paths.get(path));
                taskAttachmentRepo.delete(aid);
                taskRepo.save(task);
            }catch(Exception e){
                System.out.println(""+e.getMessage());
            }

            json.addProperty("message","Deleted the attachment!");
            return new ResponseEntity(json.toString(), HttpStatus.OK);
        }
        else
        {
            return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    private void saveUploadedFiles(List<MultipartFile> files,Tasks tasks) throws IOException {

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue; //next pls
            }
            try {

                Path rootPath = Paths.get("//home//chintankoticha//CSYE6225//uploads//");
                File dir = new File(rootPath + File.separator);
                if (!dir.exists()){
                    dir.mkdirs();
                }

                try{
                    Files.copy(file.getInputStream(), rootPath.resolve(file.getOriginalFilename()));
                }catch(Exception e){
                    System.out.println(""+e.getMessage());
                }
                System.out.println("Server File Location=" + rootPath.resolve(file.getOriginalFilename()).toString());
                TaskAttachments ta = new TaskAttachments();

                taskRepo.save(tasks);
                ta.setFileName(rootPath.resolve(file.getOriginalFilename()).toString());
                ta.setTask(tasks);
                taskAttachmentRepo.save(ta);
            } catch (Exception e) {
                System.out.println("You failed to upload " + e.getMessage());
            }
        }
    }
    }

