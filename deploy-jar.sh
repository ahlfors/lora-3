#!/usr/bin/env bash
cp target/lora-0.0.1-SNAPSHOT.jar ~
mv ~/lora-0.0.1-SNAPSHOT.jar ~/lora.jar
scp -i ~/aws/r.pem ~/lora.jar ubuntu@ec2-34-210-69-19.us-west-2.compute.amazonaws.com:~