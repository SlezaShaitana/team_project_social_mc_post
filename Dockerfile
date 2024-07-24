#
# Build stage
#
FROM maven:3.6.3-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml package
#
# Package stage
#
FROM openjdk:17.0.2-jdk-slim-buster
ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} mc-post.jar
EXPOSE 8080
MAINTAINER popov
COPY target/*.jar mc-post.jar
ENTRYPOINT ["java","-jar","/mc-post.jar"]
