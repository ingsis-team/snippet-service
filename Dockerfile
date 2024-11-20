# Use the Gradle image with JDK 21 to build and run the application.
FROM gradle:8.10.0-jdk-21-and-22

# Set working directory
WORKDIR /home/gradle/src

# Copy project files
COPY . /home/gradle/src

# Install PostgreSQL client
RUN apt-get update && apt-get install -y postgresql-client

# Build the application
RUN gradle assemble

# Expose application port
EXPOSE 8080

# Copy New Relic files
RUN mkdir -p /usr/local/newrelic
ADD ./newrelic-java/newrelic/newrelic.jar /usr/local/newrelic/newrelic.jar
ADD ./newrelic-java/newrelic/newrelic.yml /usr/local/newrelic/newrelic.yml

# Run the application
ENTRYPOINT ["java", "-javaagent:/usr/local/newrelic/newrelic.jar", "-jar", "/home/gradle/src/build/libs/snippet-service-0.0.1-SNAPSHOT.jar"]
