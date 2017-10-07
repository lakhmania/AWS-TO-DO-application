#CLOUDFORMATIONSTACK -- stack name
export VPC_ID=$(aws ec2 describe-vpcs --query "Vpcs[0].VpcId" --output text)

export SUBNET_ID=$(aws ec2 describe-subnets --filters "Name=vpc-id, Values=$VPC_ID" --query "Subnets[0].SubnetId" --output text)

export z_id=$(aws route53 list-hosted-zones --query 'HostedZones[0].Id' --output text)
z_id=${z_id#*e/}

export GROUP_NAME=csye6225-fall2017-$1-webapp

export NAME=$(aws route53 list-hosted-zones --query "HostedZones[0].Name" --output text)

aws cloudformation create-stack --stack-name $1 --template-body file://ec2-instance-securitygroup-cloudformation-stack.json --parameters ParameterKey=ImageId,ParameterValue=ami-cd0f5cb6 ParameterKey=VpcId,ParameterValue=$VPC_ID ParameterKey=SubnetId,ParameterValue=$SUBNET_ID ParameterKey=HostedZoneId,ParameterValue=$z_id ParameterKey=InstanceType,ParameterValue=t2.micro ParameterKey=KeyName,ParameterValue=csye6225-aws ParameterKey=GroupName,ParameterValue=$GROUP_NAME ParameterKey=Name,ParameterValue=$NAME
