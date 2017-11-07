package com.csye6225.demo.controllers;


import com.amazonaws.auth.InstanceProfileCredentialsProvider;

import com.amazonaws.services.sns.AmazonSNS;

import com.amazonaws.services.sns.AmazonSNSClientBuilder;

import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Topic;
import com.csye6225.demo.pojo.UserDetails;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TestLambda {

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String forgotPassword(@RequestBody String username) throws  Exception {
        JsonObject jsonObject = new JsonObject();

      AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
                .withCredentials(new InstanceProfileCredentialsProvider(false))
                .build();

//        AmazonSNSClient snsClient = new AmazonSNSClient(new ClasspathPropertiesFileCredentialsProvider());
//        snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));

        List<Topic> topics = snsClient.listTopics().getTopics();

        for (Topic topic : topics) {
            if (topic.getTopicArn().endsWith("password_reset")) {

                //   PublishRequest publishRequest = new PublishRequest().withTopic(topic.getTopicArn());
                PublishRequest publishRequest = new PublishRequest(topic.getTopicArn(), username);
                //PublishResult publishResult = snsClient.publish(publishRequest);
            }
        }

       // jsonObject.addProperty("message", "Reset Password link emailed ");

        return jsonObject.toString();
    }
}
