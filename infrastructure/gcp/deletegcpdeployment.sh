export ip=$(gcloud compute forwarding-rules describe csye6225-globalforwarding-rule --global | grep IPAddress)
export ip=$(cut -d ":" -f 2 <<< "$ip")
gcloud dns record-sets transaction start -z=csye6225-managed-zone
gcloud dns record-sets transaction remove --zone csye6225-managed-zone --name $2 --ttl 60 --type A "$ip"
gcloud dns record-sets transaction execute -z=csye6225-managed-zone

gcloud deployment-manager deployments delete $1 --async --delete-policy=delete

cbt deletetable csye6225-table
gcloud beta bigtable instances delete csye6225-instance