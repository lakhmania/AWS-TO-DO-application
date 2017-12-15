gcloud dns record-sets transaction start -z=create-instances-managed-zone
export ip=$(gcloud compute forwarding-rules describe create-instances-global-forwarding-rule --global | grep IPAddress)
export ip=$(cut -d ":" -f 2 <<< "$ip")
echo $ip

export DNS_NAME=$(aws route53 list-hosted-zones --query "HostedZones[0].Name" --output text)
export NAME=${DNS_NAME%????}

gcloud dns record-sets transaction remove -z create-instances-managed-zone --name $DNS_NAME --ttl 60 --type A $ip
gcloud dns record-sets transaction execute -z=create-instances-managed-zone
gcloud deployment-manager deployments delete finalpresentation
gsutil rm gs://storage-$NAME/*


gcloud deployment-manager deployments delete my-igm --async --delete-policy=delete

cbt deletetable csye6225-table
gcloud beta bigtable instances delete csye6225-instance