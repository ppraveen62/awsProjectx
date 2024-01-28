# Use an official OpenJDK runtime as a parent image
FROM openjdk:20-bullseye

COPY pom.xml usr/app/pom.xml
COPY src usr/app/src

RUN mvn -f  /usr/src/app/pom.xml clean package -DskipTests=true
FROM openjdk:20-bullseye
COPY --from=build /usr/src/app/target/partner-0.0.1-SNAPSHOT.jar /usr/app/partner-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT "java" "-jar" "usr/app/projectx-0.0.1-SNAPSHOT.jar"

