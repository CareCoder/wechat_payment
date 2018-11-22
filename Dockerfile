FROM registry.cn-shenzhen.aliyuncs.com/songhuan/hub:java8

COPY sources.list /etc/apt/sources.list
ADD run-java.sh /
ADD build.sh /
RUN chmod +x /run-java.sh /build.sh \
    && mkdir -p /data/img /opt/code /mvnRepositor \
WORKDIR /opt/code
EXPOSE 8080
ENTRYPOINT /build.sh;/run-java.sh
