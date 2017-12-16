export DNS_NAME=$(aws route53 list-hosted-zones --query "HostedZones[0].Name" --output text)
export NAME=${DNS_NAME%????}

gsutil rm gs://storage-$NAME/*

gcloud deployment-manager deployments delete my-igm --async --delete-policy=delete

cbt deletetable csye6225-table
gcloud beta bigtable instances delete csye6225-instance