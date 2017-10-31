package com.csye6225.demo.controllers;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.csye6225.demo.pojo.Tasks;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class DeleteAttachmentFromS3Bucket {
    public String deleteFile(String keyName){

        String bucketName = System.getProperty("bucket.name");
        System.out.println("Delete file from bucket, bucket name is :" + System.getProperty("bucket.name"));

        /*Assigns Temporary credentials to IAM role
        * InstanceProfileCredentialsProvider : false does not refresh the credentials
        */
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new InstanceProfileCredentialsProvider(false))
                .build();
        try{

            System.out.println("Deleting file from S3 bucket!!");
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
            return "deleted";
        }catch(AmazonServiceException ase){
            System.out.println("bucket name: " + bucketName);
            System.out.println("Request made to s3 bucket failed");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
            return null;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
