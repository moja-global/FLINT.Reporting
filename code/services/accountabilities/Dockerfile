# See: https://github.com/spotify/dockerfile-maven
FROM openjdk:17-jdk-alpine
MAINTAINER Anthony Kwaje <kwajeanthony@gmail.com>

ENTRYPOINT ["/usr/bin/java", "-jar","-Dspring.datasource.url=jdbc:postgresql://${POSTGRES_HOST}:5432/accountabilities","/usr/share/service/service.jar"]
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/service/service.jar