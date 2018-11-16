#!/bin/bash
container_stop=$(docker ps | grep spring-boot-pay | awk '{ print $1 }')
if [ -n "$container_stop" ]
  then
    sudo docker stop $container_stop #停止容器
    if [ $? -ne 0 ]
      then
        exit 2
    fi
   echo "stop spring-boot-pay container success..."
fi
container_del=$(docker ps -a | grep spring-boot-pay | awk '{ print $1 }')
if [ -n "$container_del" ]
  then
    sudo docker rm $container_del #删除容器
    if [ $? -ne 0 ]
      then
        exit 3
    fi
    echo "delete spring-boot-pa container success.."
fi
image_del=$(sudo docker images | grep hrxz/spring-boot-pay | awk '{ print $3 }')
if [ -n "$image_del" ]
  then
    sudo docker rmi $image_del
    if [ $? -ne 0 ]
      then
        exit 4
    fi
    echo "delete image success..."
fi
git pull
if [ $? -ne 0 ]
  then
    exit 5
fi
docker build --no-cache -t hrxz/spring-boot-pay:0.0.1 \
--build-arg JAR_FILE=target/spring-boot-pay.jar .
if [ $? -ne 0 ]
  then
    exit 7
fi
docker-compose up -d
if [ $? -eq 0 ]
  then
    echo "============================SUCCESS=================================================="
fi
docker rmi -f $(sudo docker images -f "dangling=true" -q)