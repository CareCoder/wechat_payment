FROM registry.cn-shenzhen.aliyuncs.com/songhuan/hub:java8

COPY . /build/
WORKDIR /build
RUN mvn package -DskipTests=true
VOLUME /tmp
ARG JAR_FILE
RUN mv /build/${JAR_FILE} /app.jar
ADD run-java.sh /
RUN chmod +x /run-java.sh \
    && mkdir -p /data/img
EXPOSE 8080
ENTRYPOINT ["/run-java.sh"]
