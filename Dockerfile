FROM openjdk:17
EXPOSE 8083
COPY target/post-service-1.0-SNAPSHOT.jar post-service-docker.jar

ENTRYPOINT ["java", "-jar","/post-service-docker.jar"]