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
COPY --from=build ${JAR_FILE} /mc-post.jar

EXPOSE 8089
MAINTAINER popov

ENTRYPOINT ["java","-jar","/mc-post.jar"]
