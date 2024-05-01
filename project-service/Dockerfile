#Here we install OS image in our container
FROM eclipse-temurin:17-jdk-alpine
#Here we specify the author ( me:) ) and in the next line the version of image
MAINTAINER Egor Chervonikov
LABEL version="1.0"
#Here we create direction to hold externally added files inside container
VOLUME /tmp
#Here we copy jar-file of from our project inside container
COPY /target/project-service-0.0.1-SNAPSHOT.jar project-service.jar
#Here we specify entrypoint, that allows us to configure a container that will run as an executable.
#So when we run container, command java with argument -jar project-service.jar will be executed
ENTRYPOINT ["java", "-jar", "/project-service.jar"]