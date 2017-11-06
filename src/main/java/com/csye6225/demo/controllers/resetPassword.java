package com.csye6225.demo.controllers;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.ListTopicsResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Topic;
import com.csye6225.demo.pojo.UserDetails;
import com.google.gson.JsonObject;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class resetPassword {

    @RequestMapping(value ="/resetPassword", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public void welcome(@RequestBody UserDetails details,HttpServletRequest request)
    {


        AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
                .withCredentials(new InstanceProfileCredentialsProvider(false))
                .build();

        List<Topic> topicArns = new ArrayList<>();

        ListTopicsResult result = snsClient.listTopics();
        topicArns.addAll(result.getTopics());
        String topicArn=null;
        for(Topic topic:topicArns){
            if(topic.getTopicArn().endsWith("password_reset")){
                topicArn = topic.getTopicArn();
            }
        }
            //publish to an SNS topic
    String msg = "My text published to SNS topic with email endpoint";
    PublishRequest publishRequest = new PublishRequest(topicArn, details.getUserName());
    PublishResult publishResult = snsClient.publish(publishRequest);
    }

}
