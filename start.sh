#!/bin/sh

docker-compose down

./gradlew clean assemble

docker-compose up --build
