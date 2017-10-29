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

    private static String bucketName = "code-deploy.csye6225-fall2017-lakhmania.me";
    //private static String keyName = "AKIAJBFCIXAWGLT7TFWA";
    //private static String filename = "/home/apoorva/csye6225/scripts_backup/create-csye6225-cloudformation-stack.sh";

    public void uploadFile(MultipartFile multipartfile){
        //AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
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
    {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
