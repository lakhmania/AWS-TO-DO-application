export NAME=$(aws route53 list-hosted-zones --query "HostedZones[0].Name" --output text)
export NAME1=${NAME:0:${#NAME}-1}
export S3_BUCKET_NAME="code-deploy."$NAME1

aws s3 rm s3://$S3_BUCKET_NAME --recursive

aws cloudformation delete-stack --stack-name $1
