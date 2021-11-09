FROM adoptopenjdk/openjdk11:alpine-jre

# Directory & User Setup
RUN mkdir /opt/application
RUN addgroup --system accmanager && adduser -S -s /bin/false -G accmanager accmanager

# Copy Application Jar
COPY service/target/*.jar /opt/application/accManager.jar

# Start Springboot Application
ENTRYPOINT ["java","-Dspring.profiles.active=local,h2","-jar","/opt/application/accManager.jar"]