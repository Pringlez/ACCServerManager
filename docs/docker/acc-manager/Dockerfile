FROM openjdk:17-jdk-slim as builder

MAINTAINER John Walsh <pringlez@github.com>

# Copy Application Jar & Get Layers
WORKDIR accmanager
COPY ../../../service/target/*.jar accmanager/accManager.jar

RUN java -Djarmode=layertools -jar accmanager/accManager.jar extract

FROM openjdk:17-jdk-slim

# Copy Layers
WORKDIR accmanager
COPY --from=builder accmanager/dependencies/ ./
COPY --from=builder accmanager/spring-boot-loader/ ./
COPY --from=builder accmanager/snapshot-dependencies/ ./
COPY --from=builder accmanager/application/ ./

# Start Springboot Application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.JarLauncher"]