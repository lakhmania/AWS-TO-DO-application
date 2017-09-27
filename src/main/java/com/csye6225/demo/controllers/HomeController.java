package com.csye6225.demo.controllers;


import com.csye6225.demo.pojo.User;
import com.csye6225.demo.pojo.UserSession;
import com.csye6225.demo.repo.UserRepository;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Date;

@Controller
public class HomeController {

  private final static Logger logger = LoggerFactory.getLogger(HomeController.class);

  @Autowired
  private UserRepository userRepo;


  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String welcome() {

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("message", "you are logged in. current time is " + new Date().toString());

    return jsonObject.toString();
  }

  @RequestMapping(value = "/register", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String save(HttpServletRequest request) {
    JsonObject json = new JsonObject();

    String auth = request.getHeader("Authorization");
    HttpSession session = request.getSession();
    if (auth != null && auth.startsWith("Basic")) {
      String base64Credentials = auth.substring("Basic".length()).trim();
      String credentials = new String(Base64.getDecoder().decode(base64Credentials),
              Charset.forName("UTF-8"));

      final String[] values = credentials.split(":", 2);

      System.out.println("User is ::::: " + values[0]);
      System.out.println(" Password is ::::: " + values[1]);

      UserSession userSession = new UserSession();
      userSession.setSessionId(request.getSession().getId());

      User user = new User(values[0],  bCryptPasswordEncoder.encode(values[1]));
      user.getUserSessions().add(userSession);
      userSession.setUser(user);

      try {

        User existingUser = userRepo.findByUserName(values[0]);
        if(existingUser == null){
          userRepo.save(user);
          json.addProperty("message", "User added successfully");
          json.addProperty("sessionId", request.getSession().getId());
        }else{
          json.addProperty("message", "user already exists");
        }

      } catch (DataIntegrityViolationException e) {
        json.addProperty("message", "user already exists");
      }
    }

    return json.toString();
  }

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
