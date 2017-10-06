#! /bin/bash

# <CHINTAN KOTICHA>, <001267049>, <koticha.c@husky.neu.edu>
# <NIRALI MERCHANT>, <001268909>, <merchant.n@husky.neu.edu>
# <APOORVA LAKHMANI>, <001256312>, <lakhmani.a@husky.neu.edu>
# <NEHA LALWANI>, <001268916>, <lalwani.n@husky.neu.edu>

export VPC_ID=$(aws ec2 describe-vpcs --query "Vpcs[0].VpcId" --output text)
	
#Create Security Group
aws ec2 create-security-group --group-name csye6225-fall2017-webapp --description "CSYE6225-WEBSERVER-SG" --vpc-id $VPC_ID

# Use the VPC ID to get subnet id
export SUBNET_ID=$(aws ec2 describe-subnets --filters "Name=vpc-id, Values=$VPC_ID" --query "Subnets[0].SubnetId" --output text)

#Store Security id	
export sg_id=$(aws ec2 describe-security-groups --filters "Name=group-name, 		Values=csye6225-fall2017-webapp" --query "SecurityGroups[0].GroupId" --output text)
export sg_name=$(aws ec2 describe-security-groups --filters "Name=group-name, 		Values=csye6225-fall2017-webapp" --query "SecurityGroups[0].GroupName" --output text)
	
#Change inbound rules
aws ec2 authorize-security-group-ingress --group-name csye6225-fall2017-webapp --protocol tcp --port 22 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-name csye6225-fall2017-webapp --protocol tcp --port 80 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-name csye6225-fall2017-webapp --protocol tcp --port 443 --cidr 0.0.0.0/0

#EC2 instance settings
export instance_id=$(aws ec2 run-instances --image-id ami-cd0f5cb6 --count 1 --instance-type t2.micro --key-name csye6225-aws --security-group-ids $sg_id --subnet-id $SUBNET_ID --block-device-mappings "[{\"DeviceName\":\"/dev/sdf\",\"Ebs\":{\"VolumeSize\":16,\"VolumeType\":\"gp2\"}}]" --disable-api-termination --query "Instances[0].InstanceId" --output text)
echo $instance_id

#Set accidental protection to true
#aws autoscaling set-instance-protection --instance-ids $instance_id --auto-scaling-group-name sg_name --protected-from-scale-in

#wait for instance to be in running-state
aws ec2 wait --region us-east-1 instance-running --instance-ids $instance_id
echo "Instance Created" 

#find public IP
export ip_address=$(aws ec2 describe-instances --filters "Name=instance-id,Values=$instance_id" --query "Reservations[0].Instances[0].PublicIpAddress" --output text) 
echo $ip_address

#create record set
aws route53 change-resource-record-sets --hosted-zone-id Z20JRH6EDAVT --change-batch "{\"Changes\":[{\"Action\": \"UPSERT\",\"ResourceRecordSet\":{\"Name\":\"ec2.csye6225-fall2017-kotichac.me.\",\"Type\":\"A\",\"TTL\":60,\"ResourceRecords\":[{\"Value\":\"$ip_address\"}]}}]}"
echo "Record set created/updated"
