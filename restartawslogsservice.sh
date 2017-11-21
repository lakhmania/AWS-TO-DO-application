#!/bin/bash

sudo cp awslogs.conf /var/awslogs/etc
sudo service awslogs stop
sleep 60
sudo service awslogs start
sleep 60
sudo service awslogs status