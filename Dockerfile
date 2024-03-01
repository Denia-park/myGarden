FROM openjdk:17-jdk-alpine3.14
ARG JAR_FILE=my-garden-be/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", \
 "-javaagent:/pinpoint-agent/pinpoint-bootstrap-2.5.3.jar", \
 "-Dpinpoint.agentId=aws-ec2-1", \
 "-Dpinpoint.applicationName=MyGarden", \
 "-Dspring.profiles.active=prod", \
 "-jar", "/app.jar"]
