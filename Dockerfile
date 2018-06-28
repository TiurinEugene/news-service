FROM openjdk:alpine
VOLUME /tmp
ARG JAR_FILE=build/libs/news-service-1.0.0.jar
COPY $JAR_FILE app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=${ENV}","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
