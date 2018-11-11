FROM hrzx/maven:0.0.1 as builder
COPY . /build/
WORKDIR /build
RUN mvn package -DskipTests=true

FROM hrzx/java:latest
VOLUME /tmp
ARG JAR_FILE
COPY --from=builder /build/${JAR_FILE} /app.jar
ADD run-java.sh /
RUN chmod +x /run-java.sh
EXPOSE 8080
ENTRYPOINT ["/run-java.sh"]
