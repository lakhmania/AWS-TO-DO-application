#CLOUDFORMATIONSTACK -- stack name
aws cloudformation create-stack --stack-name $1 --template-body file://ec2-instance-securitygroup-cloudformation-stack.json
