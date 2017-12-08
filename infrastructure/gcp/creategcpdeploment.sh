#!/usr/bin/env bash

gcloud deployment-manager deployments create my-deployment \
--template vm_template.jinja \
--properties zone:us-east1-b,machineType:n1-standard-1,containerImage:ubuntu-1604-lts,\
tag:network-tag,serviceAccountId:csye6225,roleId:aCustomRole,title:MyTitle,description:Mydescription,stage:ALPHA \
storage:csye6225-storage,port:8080,service:http,region:us-east1

