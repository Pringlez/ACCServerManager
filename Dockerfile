FROM openjdk:8-jdk-alpine

# Built application jar
ARG JAR_FILE=service/target/*.jar

# App working directory
WORKDIR /opt/app

# Copy application jar
COPY ${JAR_FILE} app.jar

# Start springboot application
ENTRYPOINT ["java","-jar","app.jar"]