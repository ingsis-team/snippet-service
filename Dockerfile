# Build Stage
FROM gradle:8.10.0-jdk-21-and-22 AS build

COPY . /home/gradle/src
WORKDIR /home/gradle/src

# Build the project and verify that the JAR file is created
RUN gradle clean build && ls -l /home/gradle/src/build/libs

# Runtime Stage
FROM openjdk:21-jdk-slim
EXPOSE 8080

# Create application directory
RUN mkdir /app

# Copy the JAR file explicitly by name to ensure correct reference
COPY --from=build /home/gradle/src/build/libs/snippet-service-0.0.1-SNAPSHOT.jar /app/spring-boot-application.jar

# Verify that the JAR file was copied to /app
RUN ls -l /app

# Set up New Relic configuration files
RUN mkdir -p /usr/local/newrelic
ADD ./newrelic-java/newrelic/newrelic.jar /usr/local/newrelic/newrelic.jar
ADD ./newrelic-java/newrelic/newrelic.yml /usr/local/newrelic/newrelic.yml

# Define the entrypoint
ENTRYPOINT ["java", "-javaagent:/usr/local/newrelic/newrelic.jar", "-Dspring.profiles.active=production", "-jar", "/app/spring-boot-application.jar"]
