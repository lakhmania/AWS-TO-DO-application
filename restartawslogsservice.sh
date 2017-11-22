#!/bin/bash

sudo rm awslogs.conf
cd /var/awslogs/etc
sudo rm awslogs.conf
cd ..
sudo cp awslogs.conf /var/awslogs/etc
sudo systemctl restart awslogs.service