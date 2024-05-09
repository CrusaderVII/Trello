FROM eclipse-temurin:17-jdk-alpine
MAINTAINER Egor Chervonikov
LABEL version="1.0"
WORKDIR /app
COPY /target/discovery-service-0.0.1-SNAPSHOT.jar /app/discovery-service.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "discovery-service.jar"]