#!/bin/bash

sudo rm awslogs.conf
sudo cp awslogs.conf /var/awslogs/etc
sudo systemctl restart awslogs.service