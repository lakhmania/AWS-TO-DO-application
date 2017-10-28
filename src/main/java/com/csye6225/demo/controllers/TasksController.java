
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
import java.io.FileWriter;
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

    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String COMMA_SEPARATOR = ",";

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TaskAttachmentRepository taskAttachmentRepo;

    @Autowired
    private TasksRepository taskRepo;


    /**
     * This methods creates the task for a user
     * @param desc - Task description
     * @param request : HTTP Request
     * @return : Http status with message
     */


    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> createTask(@RequestBody Description desc, HttpServletRequest request) {

        String auth = request.getHeader("Authorization");
        JsonObject json = new JsonObject();


        if (auth != null && auth.startsWith("Basic")) {

            String password = null;
            String base64Credentials = auth.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));

            String[] values = credentials.split(":", 2);
            User user = userRepo.findByUserName(values[0]);
            password = values[1];
            String description = desc.getDescription();
            if (description.length() > 4096) {
                json.addProperty("message", "Description length should not exceed 4096");
                return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
            }

            try {
                Tasks task = new Tasks();
                task.setDescription(description);
                task.setUser(user);
                user.getTasks().add(task);
                userRepo.save(user);
                System.out.print(task.getId());
                //writeCsvFile(System.getProperty("user.home") + "/savedTasks.csv", task, password);
                json.addProperty("id", task.getTaskId().toString());
                json.addProperty("description", task.getDescription());
                return new ResponseEntity<>(json.toString(), HttpStatus.OK);

            } catch (Exception e) {
                System.out.print(e);
                json.addProperty("message", "Error creating task");
                return new ResponseEntity<>(json.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {

            json.addProperty("message", "You are not logged in!!");
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * This method accepts a task ID and deletes a task belonging to a particular user
     * @param id - Task ID
     * @param request : HTTP Request
     * @return : Http status with message
     */


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> deleteTask(@PathVariable(value = "id") String id, HttpServletRequest request) {

        JsonObject json = new JsonObject();

        String auth = request.getHeader("Authorization");
        List<Tasks> tasks = (List<Tasks>) taskRepo.findAll();

        if (auth != null && auth.startsWith("Basic")) {
            String base64Credentials = auth.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));

            String[] values = credentials.split(":", 2);
            UUID  uid  = null ;
            try {
                 uid = UUID.fromString(id);
            }
            catch (Exception e)
            {
                json.addProperty("message", "Task Id malformed");
                return new ResponseEntity(json.toString(), HttpStatus.BAD_REQUEST);
            }
            Tasks task = taskRepo.findByTaskId(uid);
            if (task == null) {
                json.addProperty("message", "Task Id not found");
                return new ResponseEntity(json.toString(), HttpStatus.BAD_REQUEST);
            } else {
                try {
                    User taskUser = task.getUser();
                    if (values[0].equals(taskUser.getUserName())) {
                        taskRepo.delete(task);
                        json.addProperty("message", "Task deleted successfully");
                        System.out.print(json.toString());
                        return new ResponseEntity(json.toString(), HttpStatus.OK);
                    } else {
                        json.addProperty("message", "Given Task Id doesnot belong to you!!");
                        return new ResponseEntity(json.toString(), HttpStatus.BAD_REQUEST);
                    }

                } catch (Exception e) {
                    System.out.println(e);
                    json.addProperty("message", "Couldnot delete task");
                    return new ResponseEntity(json.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
                }

            }

        } else {

            json.addProperty("message", "You are not logged in!!");
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
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
            if (user == null) {
                JsonObject json = new JsonObject();
                json.addProperty("message", "User does not exists!!");
                return new ResponseEntity(json.toString(), HttpStatus.BAD_REQUEST);
            }

            tasks = taskRepo.findTasksByUser(user);
        } else {
            JsonObject json = new JsonObject();
            json.addProperty("message", "You are not logged in!!");
            return new ResponseEntity(json.toString(), HttpStatus.BAD_REQUEST);
        }


        JSONArray jsonArray = new JSONArray();


        for (Tasks task : tasks) {
            JSONObject json = new JSONObject();

            json.put("id", task.getId().toString());
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

        UUID uid = UUID.fromString(taskId);
        Tasks task = taskRepo.findTasksByTaskId(uid);

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
            return new ResponseEntity<>(jsonArray.toString(), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }



  /**
     * This methods updates the task description for the given task id with the description passed in request
     * @param id - Task ID
     * @param description - Task description
     * @param request : HTTP Request
     * @return : Http status with message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> updateTasks(@PathVariable("id") String id, @RequestBody Description description, HttpServletRequest request) {

        JsonObject json = new JsonObject();

        String auth = request.getHeader("Authorization");

        if (auth != null && auth.startsWith("Basic")) {

            UUID uid = UUID.fromString(id);

            Tasks task = taskRepo.findByTaskId(uid);

            if (task == null) {
                json.addProperty("message", "TaskId doesn't exists");
                return new ResponseEntity(json.toString(), HttpStatus.BAD_REQUEST);
            }

            User taskUser = task.getUser();

            String base64Credentials = auth.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));

            final String[] values = credentials.split(":", 2);

            if (!values[0].equals(taskUser.getUserName())) {
                json.addProperty("message", "Not an authorized user");
                return new ResponseEntity(json.toString(), HttpStatus.FORBIDDEN);
            }

            if (description.getDescription().length() > 4096) {
                json.addProperty("message", "description more than 4096 characters");
                return new ResponseEntity(json.toString(), HttpStatus.BAD_REQUEST);
            }

            taskRepo.updateTaskDescription(description.getDescription(), task.getTaskId());
            json.addProperty("id", id);
            json.addProperty("description", description.getDescription());
            return new ResponseEntity(json.toString(), HttpStatus.OK);

        } else {

            json.addProperty("message", "You are not logged in!!");
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }
    }

   /* public static void writeCsvFile(String fileName, Tasks task, String password) throws Exception {

        File file = new File(fileName);
        System.out.println("file:" + file.getAbsolutePath());
        FileWriter fileWriter = null;
        System.out.println(password);


        try {

            if (file.exists()) {
                fileWriter = new FileWriter(fileName, true);

            } else {

                fileWriter = new FileWriter(fileName);
            }

            fileWriter.append(task.getId().toString());
            fileWriter.append(COMMA_SEPARATOR);
            fileWriter.append(task.getUser().getUserName());
            fileWriter.append(COMMA_SEPARATOR);
            fileWriter.append(password);
            fileWriter.append(NEW_LINE_SEPARATOR);
            System.out.println("CSV file was created successfully !!!");
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            fileWriter.flush();
            fileWriter.close();

        }

    }

    public static void writeCsvFile(String fileName, TaskAttachments taskAttachments, String password) throws Exception {

        File file = new File(fileName);
        System.out.println("file:" + file.getAbsolutePath());
        FileWriter fileWriter = null;

        try {

            if (file.exists()) {
                fileWriter = new FileWriter(fileName, true);

            } else {

                fileWriter = new FileWriter(fileName);
            }

            fileWriter.append(String.valueOf(taskAttachments.getTaskAttachmentId()));
            fileWriter.append(COMMA_SEPARATOR);
            fileWriter.append(taskAttachments.getTask().getId().toString());
            fileWriter.append(COMMA_SEPARATOR);
            fileWriter.append(taskAttachments.getTask().getUser().getUserName());
            fileWriter.append(COMMA_SEPARATOR);
            fileWriter.append(password);
            fileWriter.append(NEW_LINE_SEPARATOR);
            System.out.println("CSV file was created successfully !!!");
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            fileWriter.flush();
            fileWriter.close();
        }

    }*/

    /**
     * This methods attaches the attachments for the given task id passed in request
     * @param request : HTTP Request
     * @param id : Task ID
     * @param uploadfiles : Attachments files
     * @return : Http status with message
     */
    @RequestMapping(value = "/{id}/attachments", method = RequestMethod.POST, produces = "application/json", consumes = "multipart/form-data")
    @ResponseBody
    public ResponseEntity<String> addAttachments(HttpServletRequest request, @PathVariable("id") String id, @RequestParam("files") MultipartFile[] uploadfiles) {
        JsonObject json = new JsonObject();
        //Tasks tasks = taskrepo.findById(id);
        UUID uid = UUID.fromString(id);
        Tasks task = taskRepo.findByTaskId(uid);
        if (task == null) {
            json.addProperty("message", "No such task found!!");
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }
        String auth = request.getHeader("Authorization");

        String[] values = null;
        String username = null;
        JSONArray jsonArray = null;
        if (auth != null && auth.startsWith("Basic")) {
            String password;
            String base64Credentials = auth.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));

            values = credentials.split(":", 2);
            username = values[0];
            password = values[1];

            if (username.equals(task.getUser().getUserName())) {
                //List<TaskAttachments> attachments = taskAttachmentRepo.findByTask(task);
                String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename()).filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
                try {
                    String error = null;
                    error = saveUploadedFiles(Arrays.asList(uploadfiles), task, password);
                    if (error.equalsIgnoreCase("error")) {
                        error = null;
                        json.addProperty("error", "An error occured while uploading files!!");
                        json.addProperty("probable", "Maybe the file already exists!!");
                        return new ResponseEntity(json.toString(), HttpStatus.BAD_REQUEST);
                    }
                } catch (Exception e) {
                    System.out.println("" + e.getMessage());
                }

                if (StringUtils.isEmpty(uploadedFileName)) {
                    json.addProperty("message", "Please select a file!");
                    return new ResponseEntity(json.toString(), HttpStatus.BAD_REQUEST);
                }

                json.addProperty("message", "Saved the file(s)!");
                return new ResponseEntity(json.toString(), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            json.addProperty("message", "You are not logged in!!");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * This methods deletes the attachments for the given task id and attachment id passed in request
     * @param request : HTTP Request
     * @param attachmentid : Attachment ID
     * @param id : Task ID
     * @return : Http status with message
     */
    @RequestMapping(value = "/{id}/attachments/{idAttachments}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> deleteAttachments(HttpServletRequest request, @PathVariable("idAttachments") String attachmentid, @PathVariable("id") String id) {
        JsonObject json = new JsonObject();

        UUID uid = UUID.fromString(id);
        Tasks task = taskRepo.findByTaskId(uid);
        if (task == null) {
            json.addProperty("message", "No such task found!!");
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
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


            if (username.equals(task.getUser().getUserName())) {
                try {
                    int aid = 0;
                    try {
                        aid = Integer.parseInt(attachmentid);
                    } catch (Exception e) {
                        json.addProperty("message", "Attachment Id can only be numbers!!");
                        return new ResponseEntity(json.toString(), HttpStatus.BAD_REQUEST);
                    }
                    TaskAttachments ta = taskAttachmentRepo.findTaskAttachmentsByTaskAttachmentId(aid);
                    if (ta == null) {
                        json.addProperty("message", "No such attachments with the Id!!");
                        return new ResponseEntity(json.toString(), HttpStatus.BAD_REQUEST);
                    }
                    String path = ta.getFileName();
                    Files.deleteIfExists(Paths.get(path));
                    taskAttachmentRepo.delete(aid);
                    taskRepo.save(task);
                } catch (Exception e) {
                    System.out.println("" + e.getMessage());
                }

                json.addProperty("message", "Deleted the attachment!");
                return new ResponseEntity(json.toString(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            json.addProperty("message", "You are not logged in!!");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    private String saveUploadedFiles(List<MultipartFile> files, Tasks tasks,String password) throws IOException {
        UploadAttachmentToS3Bucket uploadToS3 = new UploadAttachmentToS3Bucket();

        for (MultipartFile file : files) {

            uploadToS3.uploadFile(file);

            if (file.isEmpty()) {
                continue;
            }
            try {
                Path rootPath = Paths.get(System.getProperty("java.io.tmpdir"));
                File dir = new File(rootPath + File.separator + "" + tasks.getUser().getUserName() + "" + tasks.getId().toString());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                rootPath = Paths.get(dir.getAbsolutePath());
                try {
                    Files.copy(file.getInputStream(), rootPath.resolve(file.getOriginalFilename()));
                } catch (Exception e) {
                    System.out.println("" + e.getMessage());
                    return ("error");
                }
                System.out.println("Server File Location=" + rootPath.resolve(file.getOriginalFilename()).toString());
                TaskAttachments ta = new TaskAttachments();
                taskRepo.save(tasks);
                ta.setFileName(rootPath.resolve(file.getOriginalFilename()).toString());
                ta.setTask(tasks);
                taskAttachmentRepo.save(ta);
                //writeCsvFile(System.getProperty("user.home") + "/savedTasksAttachments.csv", ta, password);
            } catch (Exception e) {
                System.out.println("You failed to upload " + e.getMessage());
                return ("error");
            }
        }
        return ("");
    }
}


