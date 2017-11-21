#!/bin/bash
sudo rm /home/ubuntu/awslogs.conf
sudo cp awslogs.conf /var/awslogs/etc
sudo service awslogs restart
sleep 120
sudo service awslogs start