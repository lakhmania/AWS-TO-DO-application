#!/usr/bin/env bash

export TRAVIS_UPLOAD_TO_S3_POLICY_NAME=Travis-Upload-To-S3
export TRAVIS_CODE_DEPLOY_POLICY_NAME=Travis-Code-Deploy
export ACCOUNT_ID=$(aws sts get-caller-identity --query "Account" --output text)
export REGION=us-east-1
export APPLICATION_NAME=csye6225-webapp
export RESOURCE1="arn:aws:codedeploy:"$REGION":"$ACCOUNT_ID":application:"$APPLICATION_NAME
export RESOURCE2="arn:aws:codedeploy:"$REGION":"$ACCOUNT_ID":deploymentconfig:CodeDeployDefault.OneAtATime"
export RESOURCE3="arn:aws:codedeploy:"$REGION":"$ACCOUNT_ID":deploymentconfig:CodeDeployDefault.HalfAtATime"
export RESOURCE4="arn:aws:codedeploy:"$REGION":"$ACCOUNT_ID":deploymentconfig:CodeDeployDefault.AllAtOnce"

aws cloudformation create-stack --stack-name $1 --template-body file://travis-policies.json --parameters   ParameterKey=Resource1,ParameterValue=$RESOURCE1 ParameterKey=Resource2,ParameterValue=$RESOURCE2 ParameterKey=Resource3,ParameterValue=$RESOURCE3 ParameterKey=Resource4,ParameterValue=$RESOURCE4  ParameterKey=TravisUploadToS3PolicyName,ParameterValue=$TRAVIS_UPLOAD_TO_S3_POLICY_NAME ParameterKey=TravisCodeDeployPolicyName,ParameterValue=$TRAVIS_CODE_DEPLOY_POLICY_NAME  --capabilities CAPABILITY_IAM --capabilities CAPABILITY_NAMED_IAM

