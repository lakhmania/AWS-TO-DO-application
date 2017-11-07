package com.csye6225.demo.controllers;


import com.amazonaws.auth.InstanceProfileCredentialsProvider;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
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
                snsClient.publish(publishRequest);
                //PublishResult publishResult = snsClient.publish(publishRequest);
            }
        }

       // jsonObject.addProperty("message", "Reset Password link emailed ");

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

//        context.getLogger().log("Trying to insert item");
        Table table = dynamoDB.getTable("csye6225");
        String id = "kdjfkdjf0";
        try {
            table.putItem(new Item().withPrimaryKey("id", id));
            System.out.println("PutItem succeeded: " + id );
//            context.getLogger().log("PutItem succeeded: " + id);
        }
        catch (Exception e) {
            System.err.println("Unable to add id: " + id);
//            context.getLogger().log("Unable to add id: " + id);
//            context.getLogger().log(e.getMessage());
            System.err.println(e.getMessage());


        }



        return jsonObject.toString();
    }
}
