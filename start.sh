#!/bin/bash
image=$(sudo docker images | grep hrxz/spring-boot-pay | awk '{ print $3 }')
if [[ -z "$image" ]]
  then
    docker build --no-cache -t hrxz/spring-boot-pay:0.0.1 \
        --build-arg JAR_FILE=target/spring-boot-pay.jar .
     docker rmi -f $(sudo docker images -f "dangling=true" -q)
fi
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
docker-compose up -d
if [ $? -eq 0 ]
  then
    echo "============================SUCCESS=================================================="
fi