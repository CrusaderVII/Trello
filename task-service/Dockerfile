#Here we install OS image in our container
FROM eclipse-temurin:17-jdk-alpine
#Here we specify the author ( me:) ) and in the next line the version of image
MAINTAINER Egor Chervonikov
LABEL version="1.0"
#Here we create working direction inside container
WORKDIR /app
#Here we copy jar-file of from our project inside container
COPY /target/task-service-0.0.1-SNAPSHOT.jar /app/task-service.jar
#Here we add metadata to the image to describe that the container is listening on port 8080
EXPOSE 8080
#Here we specify entrypoint, that allows us to configure a container that will run as an executable.
#So when we run container, command java with argument -jar project-service.jar will be executed.
#Docker combine 3 entrypoints in 1 command
ENTRYPOINT ["java", "-jar", "task-service.jar"]