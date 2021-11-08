FROM adoptopenjdk/openjdk11:alpine-jreww

# Directory & user setup
RUN mkdir /opt/application
RUN addgroup --system accmanager && adduser -S -s /bin/false -G accmanager accmanager

# Copy application jar
COPY service/target/*.jar /opt/application/Manager.jar

# Start springboot application
ENTRYPOINT ["java","-Dspring.profiles.active=local,h2","-jar","/opt/application/accManager.jar"]