gcloud deployment-manager deployments delete $1 --async --delete-policy=delete

cbt deletetable csye6225-table
gcloud beta bigtable instances delete csye6225-instance