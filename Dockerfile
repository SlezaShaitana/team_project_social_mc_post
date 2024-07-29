#
# Build stage
#
FROM maven:3.6.3-openjdk-17-slim AS build
WORKDIR /home/app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

#
# Package stage
#
FROM openjdk:17.0.2-jdk-slim-buster
ARG JAR_FILE=/home/app/target/*.jar
WORKDIR /opt/app
COPY --from=build ${JAR_FILE} /mc-post.jar

#ARG JAR_FILE=target/mc-post.jar

#COPY ${JAR_FILE} /mc-post.jar
EXPOSE 8080
MAINTAINER popov

ENTRYPOINT ["java","-jar","/mc-post.jar"]
