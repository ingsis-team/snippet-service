FROM gradle:8.5-jdk17 AS build

COPY . /home/gradle/src
WORKDIR /home/gradle/src

# Ensure that the Java 17 toolchain is available
RUN gradle clean assemble

FROM amazoncorretto:17-alpine
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production", "/app/spring-boot-application.jar"]
