#!/usr/bin/env bash

gcloud deployment-manager deployments create my-igm \
    --template vm_template.jinja \
    --properties zone:us-east1-b,machineType:n1-standard-1,containerImage:ubuntu-1604-lts,storage:csye6225-storage,port:80,service:http,region:us-east1,sslCertificateName:csye6225certificate,httpsport:443,tag:network-tag,instancename:$1,dnsName:$2

gcloud beta bigtable instances create csye6225-instance --cluster=csye6225-cluster --cluster-zone=us-central1-b --instance-type=PRODUCTION --cluster-num-nodes=3 --cluster-storage-type=SSD --description=csye6225-instance
echo -e "instance = csye6225-instance" > ~/.cbtrc
cbt createtable csye6225-table 10,20
cbt createfamily csye6225-table cf1
cbt setgcpolicy csye6225-table cf1 maxage=1200s

export ip=$(gcloud compute forwarding-rules describe csye6225-globalforwarding-rule --global | grep IPAddress)
export ip=$(cut -d ":" -f 2 <<< "$ip")
gcloud dns record-sets transaction start -z=csye6225-managed-zone
gcloud dns record-sets transaction add -z=$2 --name=$2 --type=A --ttl=60 $ip
gcloud dns record-sets transaction execute -z=csye6225-managed-zone

