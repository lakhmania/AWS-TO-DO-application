#!/bin/bash

sudo cp awslogs.conf /var/awslogs/etc
sudo service awslogs stop
sudo service awslogs start