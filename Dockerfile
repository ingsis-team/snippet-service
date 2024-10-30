FROM gradle:8.10.0-jdk-21-and-22 AS build

COPY . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle clean assemble

FROM openjdk:21-jdk-slim
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production", "/app/spring-boot-application.jar"]
