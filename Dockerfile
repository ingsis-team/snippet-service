FROM gradle:8.10.0-jdk-21-and-22 AS build

COPY . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle clean assemble

FROM openjdk:21-jdk-slim
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

RUN mkdir -p /usr/local/newrelic

ADD ./newrelic-java/newrelic/newrelic.jar /usr/local/newrelic/newrelic.jar
ADD ./newrelic-java/newrelic/newrelic.yml /usr/local/newrelic/newrelic.yml

ENTRYPOINT ["java", "-jar","-javaagent:/usr/local/newrelic/newrelic.jar","-Dspring.profiles.active=production","/app/spring-boot-application.jar"]