FROM gradle:8.10.0-jdk-21-and-22

# Switch to the root user to install necessary packages.
USER root

# Update system and install PostgreSQL client, useful for running database commands if needed.
RUN apt-get update && apt-get install -y postgresql-client

# Copy all project files from the host to the container's working directory.
COPY . /home/gradle/src
WORKDIR /home/gradle/src

# Build the application, which will create the JAR file in the build/libs directory.
RUN gradle assemble

# Expose port 8080 so the application is accessible on this port.
EXPOSE 8080

# New Relic integration starts here

# Create a directory in the container to store New Relic files.
# This is where the New Relic agent and configuration file will be located.
RUN mkdir -p /usr/local/newrelic

# Copy the New Relic agent JAR file (newrelic.jar) from the project into the container.
# This file enables monitoring of the Java application by New Relic.
ADD ./newrelic-java/newrelic/newrelic.jar /usr/local/newrelic/newrelic.jar

# Copy the New Relic configuration file (newrelic.yml) from the project into the container.
# This file contains settings like the New Relic license key and app name, which are needed for the agent to report data.
ADD ./newrelic-java/newrelic/newrelic.yml /usr/local/newrelic/newrelic.yml

# New Relic integration ends here

# Run the application using the Java command.
# The -javaagent option specifies the path to the New Relic agent, enabling monitoring when the application runs.
ENTRYPOINT ["java","-jar","-javaagent:/usr/local/newrelic/newrelic.jar","/home/gradle/src/build/libs/snippet-service-0.0.1-SNAPSHOT.jar"]
