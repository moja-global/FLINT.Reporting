# See: https://www.baeldung.com/spring-boot-docker-images
FROM eclipse-temurin:17.0.1_12-jre-alpine as builder
MAINTAINER Anthony Kwaje <kwajeanthony@gmail.com>
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:17.0.1_12-jre-alpine
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
RUN true
COPY --from=builder spring-boot-loader/ ./
RUN true
COPY --from=builder application/ ./
ENTRYPOINT ["java", "-Dspring.datasource.url=jdbc:postgresql://${POSTGRES_HOST}:5432/cover_types", "org.springframework.boot.loader.JarLauncher"]
