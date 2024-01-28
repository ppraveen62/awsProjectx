# Use an official OpenJDK runtime as a parent image
FROM openjdk:17

COPY pom.xml usr/app/pom.xml
COPY src usr/app/src
COPY target/projectx-0.0.1-SNAPSHOT.jar usr/app/projectx-0.0.1-SNAPSHOT.jar


EXPOSE 8080

ENTRYPOINT "java" "-jar" "usr/app/projectx-0.0.1-SNAPSHOT.jar"

