FROM eclipse-temurin:17-jdk-alpine
MAINTAINER Egor Chervonikov
LABEL version="1.0"
WORKDIR /app
COPY /target/config-service-0.0.1-SNAPSHOT.jar /app/config-service.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "config-service.jar"]