#Disabling the termination protection
echo $1
aws ec2 modify-instance-attribute --instance-id $1 --no-disable-api-termination

#terminate ec2 instance
aws ec2 terminate-instances --instance-ids $1

#wait till terminated
aws ec2 wait --region us-east-1 instance-terminated --instance-ids $1

echo "instance terminated"

#fetch the security group
export SECURITY_GROUP=$(aws ec2 describe-security-groups --filters "Name=group-name, Values=csye6225-fall2017-webapp" --query "SecurityGroups[0].GroupId" --output text)

echo $SECURITY_GROUP

#delete the security group
aws ec2 delete-security-group --group-id $SECURITY_GROUP

echo "Security group deleted"
