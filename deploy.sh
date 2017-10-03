#!/usr/bin/env bash
mvn clean install -f /media/yaruliy/data/Study/JavaWS/lora/pom.xml
RED='\033[0;36m'
printf "${RED}maven build completed...\n"
cp /media/yaruliy/data/Study/JavaWS/lora/target/lora-0.0.1-SNAPSHOT.jar ~
mv ~/lora-0.0.1-SNAPSHOT.jar ~/lora.jar
printf "${RED}work with local files is completed...\n"
scp -i ~/aws/r.pem ~/lora.jar ubuntu@ec2-34-210-69-19.us-west-2.compute.amazonaws.com:~
rm ~/lora.jar
ssh -i ~/aws/r.pem ubuntu@ec2-34-210-69-19.us-west-2.compute.amazonaws.com 'sh ~/replace.sh'
