#! /bin/bash

# <CHINTAN KOTICHA>, <001267049>, <koticha.c@husky.neu.edu>
# <NIRALI MERCHANT>, <001268909>, <merchant.n@husky.neu.edu>
# <APOORVA LAKHMANI>, <001256312>, <lakhmani.a@husky.neu.edu>
# <NEHA LALWANI>, <001268916>, <lalwani.n@husky.neu.edu>

#Disabling the termination protection
echo $1
aws ec2 modify-instance-attribute --instance-id $1 --no-disable-api-termination
echo "Application termination disabled!"

#terminate ec2 instance
aws ec2 terminate-instances --instance-ids $1

#wait till terminated
aws ec2 wait --region us-east-1 instance-terminated --instance-ids $1

echo "instance terminated"

#fetch the security group
export SG_ID=$(aws ec2 describe-security-groups --filters "Name=group-name, Values=csye6225-fall2017-webapp" --query "SecurityGroups[0].GroupId" --output text)

echo $SG_ID

#delete the security group
aws ec2 delete-security-group --group-id $SG_ID

echo "Security group deleted!"
