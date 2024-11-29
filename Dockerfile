# Use the Gradle image with JDK 21 to build and run the application.
FROM gradle:8.7.0-jdk17 AS build

WORKDIR /home/gradle/src
COPY . .

RUN gradle assemble

FROM amazoncorretto:17-alpine
# Expose application port
EXPOSE 8080

# Set working directory
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/snippet-service-0.0.1-SNAPSHOT.jar /app/snippet-service-0.0.1-SNAPSHOT.jar
COPY ./newrelic-java/newrelic/newrelic.jar /app/newrelic.jar
COPY ./newrelic-java/newrelic/newrelic.yml /app/newrelic.yml

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production", "-javaagent:/app/newrelic.jar", "/app/snippet-service-0.0.1-SNAPSHOT.jar"]
