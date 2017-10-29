/**
 * <Neha Lalwani>, <001268916>, <lalwani.n@husky.neu.edu>
 * <Nirali Merchant>, <001268909>, <merchant.n@husky.neu.edu>
 * <Chintan Koticha>, <001267049>, <koticha.c@husky.neu.edu>
 * <Apoorva Lakhmani>, <001256312>, <lakhmani.a@husky.neu.edu>
 */

package com.csye6225.demo.controllers;


import com.csye6225.demo.pojo.User;
import com.csye6225.demo.pojo.UserDetails;
import com.csye6225.demo.repo.UserRepository;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

@Controller
public class HomeController {

  private final static Logger logger = LoggerFactory.getLogger(HomeController.class);

  private final String NEW_LINE_SEPARATOR = "\n";
  private final String COMMA_SEPARATOR = ",";

  @Autowired
  private UserRepository userRepo;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @RequestMapping(value ="/", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String welcome(HttpServletRequest request)
  {

    JsonObject jsonObject = new JsonObject();

    if (SecurityContextHolder.getContext().getAuthentication() != null
            && SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
      jsonObject.addProperty("message", "you are not logged in!!!");
    } else {
      jsonObject.addProperty("message", "you are logged in. current time is " + new Date().toString());
    }

    return jsonObject.toString();

  }

  @RequestMapping(value = "/user/register", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
  @ResponseBody
  public String save(@RequestBody UserDetails details, HttpServletRequest request) throws Exception{

    System.out.println("Inside save method");
    System.out.println(details.getUserName());
    System.out.println(details.getPassword());

    JsonObject json = new JsonObject();
    User existingUser = userRepo.findByUserName(details.getUserName());

    User user = new User(details.getUserName(),  bCryptPasswordEncoder.encode(details.getPassword()));
    if(existingUser == null){
      userRepo.save(user);

      //writeUsersToCsv(System.getProperty("user.home") + "/users.csv", details);

      json.addProperty("message", "User added successfully");
      //json.addProperty("sessionId", request.getSession().getId());
    }else{
      json.addProperty("message", "user already exists");
    }

    return json.toString();
  }

  /*private void writeUsersToCsv(String filename, UserDetails user) throws Exception{
    File file = new File(filename);
    System.out.println("file:"+ file.getAbsolutePath());
    FileWriter fileWriter = null;

    try {

      if (file.exists()) {
        fileWriter = new FileWriter(filename, true);

      } else {
        fileWriter = new FileWriter(filename);
      }

      fileWriter.append(user.getUserName());
      fileWriter.append(COMMA_SEPARATOR);
      fileWriter.append(user.getPassword());
      fileWriter.append(NEW_LINE_SEPARATOR);
      System.out.println("CSV file was created successfully !!!");
    } catch (Exception e) {
      System.out.println("Error in CsvFileWriter !!!");
      e.printStackTrace();
    } finally {

      //fileWriter.flush();
      fileWriter.close();

    }

  }*/

  @RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String test() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("message", "authorized for /test");
    return jsonObject.toString();
  }

  @RequestMapping(value = "/testPost", method = RequestMethod.POST, produces = "application/json")
  @ResponseBody
  public String testPost() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("message", "authorized for /testPost");
    return jsonObject.toString();
  }

}
