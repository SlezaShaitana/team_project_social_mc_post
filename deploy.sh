#!/bin/bash

# Pull new changes
#git pull

# Prepare Jar
mvn clean
mvn package -DskipTests

# Ensure, that docker-compose stopped
docker-compose stop

# Start new deployment
docker-compose up --build -d