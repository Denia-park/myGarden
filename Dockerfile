FROM openjdk:17-jdk-alpine3.14
ARG JAR_FILE=../../home/runner/work/myGarden/myGarden/my-garden-be/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod", "/app.jar"]
