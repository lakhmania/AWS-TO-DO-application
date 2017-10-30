package com.csye6225.demo.controllers;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

public class UploadAttachmentToS3Bucket {

    public void uploadFile(MultipartFile multipartfile){

        String bucketName = System.getProperty("bucket.name");
        System.out.println("bucket name is :" + System.getProperty("bucket.name"));

        /*Assigns Temporary credentials to IAM role
        * InstanceProfileCredentialsProvider : false does not refresh the credentials
        */
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new InstanceProfileCredentialsProvider(false))
                .build();
        try{

            System.out.println("Uploading file to s3 bucket");
            File filename = convertFromMultipart(multipartfile);
            s3Client.putObject(new PutObjectRequest(bucketName,filename.getName(),filename));

        }catch(AmazonServiceException ase){
            System.out.println("bucket name: " + bucketName);
            System.out.println("Request made to s3 bucket failed");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public File convertFromMultipart(MultipartFile file) throws Exception
    /**
     * This method converts a multipart file to File format
     * @param file : Task Attachment
     */
    {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
