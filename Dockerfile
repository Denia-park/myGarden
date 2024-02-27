FROM openjdk:17-alpine
#ARG JAR_FILE=my-garden-be/build/libs/*.jar
#COPY ${JAR_FILE} app.jar
COPY tempText.txt tempText.txt
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod", "/app.jar"]
