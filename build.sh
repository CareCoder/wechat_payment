#!/bin/bash
PROJECT=wechat_payment
if ! [[ -x "$(command -v git)" ]]
  then
     apt-get update
     apt-get -y install git
fi
file=$(ls -l | grep ${PROJECT} | awk '{print $9}')
if [[ -z "$file" ]]
  then
    git clone https://github.com/CareCoder/wechat_payment.git
    if [[ $? -ne 0 ]]
      then
        exit 1
    fi
    cd ${PROJECT}
    mvn package -DskipTests=true
    rm -rf /app.jar
    mv target/spring-boot-pay.jar /app.jar
  else
    cd ${PROJECT}
    git pull
    mvn package -DskipTests=true
    rm -rf /app.jar
    mv target/spring-boot-pay.jar /app.jar
fi