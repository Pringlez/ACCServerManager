FROM openjdk:8-jdk-alpine

# Built application jar
ARG JAR_FILE=service/target/acc-manager-service-0.3.0.jar

# App working directory
WORKDIR /opt/app

# Copy application jar
COPY ${JAR_FILE} app.jar

# Start springboot application
ENTRYPOINT ["java","-Dspring.profiles.active=local,h2","-jar","app.jar"]