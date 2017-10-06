#get VPC ID
export VPC_ID=$(aws ec2 describe-vpcs --query "Vpcs[0].VpcId" --output text)

echo $VPC_ID

#create security group
aws ec2 create-security-group --group-name csye6225-fall2017-webapp --description "CSYE6225-WEBSERVER-SG" --vpc-id $VPC_ID

echo "Group Created"

export SG_ID=$(aws ec2 describe-security-groups --filters "Name=group-name, Values=csye6225-fall2017-webapp" --query "SecurityGroups[0].GroupId" --output text)

echo $SG_ID

#view rules of sg
aws ec2 describe-security-groups --group-name csye6225-fall2017-webapp

#change inbound rules
aws ec2 authorize-security-group-ingress --group-name csye6225-fall2017-webapp --protocol tcp --port 22 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-name csye6225-fall2017-webapp --protocol tcp --port 80 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-name csye6225-fall2017-webapp --protocol tcp --port 443 --cidr 0.0.0.0/0

echo "inbound rules modified"
