# Use an official OpenJDK runtime as a parent image
FROM openjdk:20-bullseye

COPY pom.xml usr/app/pom.xml
COPY src usr/app/src
ADD target/projectx-0.0.1-SNAPSHOT.jar usr/app/projectx-0.0.1-SNAPSHOT.jar


EXPOSE 8080

ENTRYPOINT "java" "-jar" "usr/app/projectx-0.0.1-SNAPSHOT.jar"

