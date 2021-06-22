# See: https://www.baeldung.com/spring-boot-docker-images
FROM adoptopenjdk:11-jre-hotspot as builder
MAINTAINER Anthony Kwaje <kwajeanthony@gmail.com>
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM adoptopenjdk:11-jre-hotspot
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "-Dspring.datasource.url=jdbc:postgresql://${POSTGRES_HOST}:5432/flux_types", "org.springframework.boot.loader.JarLauncher"]
